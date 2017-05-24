/*
 * 3D City Database - The Open Source CityGML Database
 * http://www.3dcitydb.org/
 * 
 * (C) 2013 - 2015,
 * Chair of Geoinformatics,
 * Technische Universitaet Muenchen, Germany
 * http://www.gis.bgu.tum.de/
 * 
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 * 
 * virtualcitySYSTEMS GmbH, Berlin <http://www.virtualcitysystems.de/>
 * M.O.S.S. Computer Grafik Systeme GmbH, Muenchen <http://www.moss.de/>
 * 
 * The 3D City Database Importer/Exporter program is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 */
package org.citydb.plugins.ade_manager.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.citydb.api.controller.ViewController;
import org.citydb.api.event.Event;
import org.citydb.api.event.EventHandler;
import org.citydb.api.registry.ObjectRegistry;
import org.citydb.config.language.Language;
import org.citydb.database.DatabaseConnectionPool;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.database.schema.mapping.SchemaMappingException;
import org.citydb.database.schema.mapping.SchemaMappingValidationException;
import org.citydb.database.schema.util.SchemaMappingUtil;
import org.citydb.gui.ImpExpGui;
import org.citydb.log.Logger;
import org.citydb.plugins.ade_manager.ADEManagerPlugin;
import org.citydb.plugins.ade_manager.config.ConfigImpl;
import org.citydb.plugins.ade_manager.database.DBMetadataImportException;
import org.citydb.plugins.ade_manager.database.DBMetadataImporter;
import org.citydb.plugins.ade_manager.database.DBUtil;
import org.citydb.plugins.ade_manager.gui.components.ADERow;
import org.citydb.plugins.ade_manager.gui.components.ADETableModel;
import org.citydb.util.gui.GuiUtil;

@SuppressWarnings("serial")
public class ADEManagerPanel extends JPanel implements EventHandler {
	
	private final ViewController viewController;
	private final DatabaseConnectionPool dbPool;
	
	// predefined value
	protected static final int BORDER_THICKNESS = 4;
	protected static final int MAX_TEXTFIELD_HEIGHT = 20;
	protected static final int MAX_LABEL_WIDTH = 60;
	
	private final Logger LOG = Logger.getInstance();	

	private ConfigImpl config;
	
	// Gui variables
	private JPanel browseSchemaMappingPanel;
	private JTextField browseSchemaMappingText = new JTextField();
	private JButton browseSchemaMappingButton = new JButton();
	private JButton registerADEButton = new JButton();	
	
	private JPanel adeButtonsPanel;
	private JButton fetchADEsButton = new JButton();	
	private JScrollPane adeTableScrollPanel;
	private JTable adeTable;
	private ADETableModel adeTableModel = new ADETableModel();

	public ADEManagerPanel(ADEManagerPlugin plugin) {	
		config = plugin.getConfig();
		
		viewController = ObjectRegistry.getInstance().getViewController();
		dbPool = DatabaseConnectionPool.getInstance();
		
		initGui();
		addListeners();
	}

	private void initGui() {	
		// ADE table panel
		adeTable = new JTable(adeTableModel);
		adeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		adeTable.setCellSelectionEnabled(false);
		adeTable.setColumnSelectionAllowed(false);
		adeTable.setRowSelectionAllowed(true);
		adeTable.setRowHeight(20);		
		adeTableScrollPanel = new JScrollPane(adeTable);
		adeTableScrollPanel.setPreferredSize(new Dimension(adeTable.getPreferredSize().width, 150));
		adeTableScrollPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(0, 0, 4, 4)));

		// Input panel
		browseSchemaMappingPanel = new JPanel();
		browseSchemaMappingPanel.setLayout(new GridBagLayout());
		browseSchemaMappingPanel.setBorder(BorderFactory.createTitledBorder(""));
		browseSchemaMappingPanel.add(browseSchemaMappingText, GuiUtil.setConstraints(0,0,1.0,1.0,GridBagConstraints.BOTH,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS));
		browseSchemaMappingPanel.add(browseSchemaMappingButton, GuiUtil.setConstraints(1,0,0.0,0.0,GridBagConstraints.NONE,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS));
		
		adeButtonsPanel = new JPanel();
		adeButtonsPanel.setLayout(new GridBagLayout());
		adeButtonsPanel.add(fetchADEsButton, GuiUtil.setConstraints(0,0,1.0,1.0,GridBagConstraints.BOTH,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS));
		adeButtonsPanel.add(registerADEButton, GuiUtil.setConstraints(1,0,0.0,0.0,GridBagConstraints.NONE,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS));
		
		// assembling all panels above
		JPanel mainScrollView = new JPanel();
		mainScrollView.setLayout(new GridBagLayout());
		
		int index = 0;		
		mainScrollView.add(adeTableScrollPanel, GuiUtil.setConstraints(0,index++,0,0.0,GridBagConstraints.BOTH,BORDER_THICKNESS*2,BORDER_THICKNESS*2,BORDER_THICKNESS*2,BORDER_THICKNESS*2));		
		mainScrollView.add(browseSchemaMappingPanel, GuiUtil.setConstraints(0,index++,0.0,0,GridBagConstraints.BOTH,10,5,5,5));		
		mainScrollView.add(adeButtonsPanel, GuiUtil.setConstraints(0,index++,0.0,0.0,GridBagConstraints.NONE,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS));

		mainScrollView.add(new JPanel(), GuiUtil.setConstraints(0,index++,1.0,1.0,GridBagConstraints.BOTH,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS,BORDER_THICKNESS));
		JScrollPane mainScrollPanel = new JScrollPane(mainScrollView);
		mainScrollPanel.setBorder(BorderFactory.createEmptyBorder());
		mainScrollPanel.setViewportBorder(BorderFactory.createEmptyBorder());
		this.setLayout(new GridBagLayout());	
		this.add(mainScrollPanel, GuiUtil.setConstraints(0,0,1.0,1.0,GridBagConstraints.BOTH,0,0,0,0));
	}

	// localized Labels and Strings
	public void doTranslation() {	
		((TitledBorder)browseSchemaMappingPanel.getBorder()).setTitle("Schema Mapping File");	
		browseSchemaMappingButton.setText(Language.I18N.getString("common.button.browse"));
		registerADEButton.setText("Register ADE into DB");
		fetchADEsButton.setText("Fetch ADEs from DB");		
	}

	public void loadSettings() {
		
	}

	public void setSettings() {
		
	}

	private void addListeners() {
		fetchADEsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread() {
					public void run() {
						fetchADEs();						
					}
				};
				thread.setDaemon(true);
				thread.start();
			}
		});
		
		registerADEButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread() {
					public void run() {
						registerADE();						
					}
				};
				thread.setDaemon(true);
				thread.start();
			}
		});
	}
	
	private void registerADE() {
		String sourceAdeSchemaMappingPath = "resources" + File.separator + "test-ade-schema2.xml";

		SchemaMapping adeSchemaMapping = null;			
		JAXBContext mappingContext = null;	
		
		// read ADE's schema mapping file
		LOG.info("Loading ADE's schema mapping file...");
		try {			
			mappingContext = JAXBContext.newInstance(SchemaMapping.class);	
			SchemaMapping citydbSchemaMapping = SchemaMappingUtil.unmarshal(SchemaMappingUtil.class.getResource("/resources/3dcitydb/3dcitydb-schema.xml"), mappingContext);
			adeSchemaMapping = SchemaMappingUtil.unmarshal(citydbSchemaMapping, new File(sourceAdeSchemaMappingPath), mappingContext);	
		} catch (JAXBException e) {
			LOG.error(e.getMessage());
			return;
		} catch (SchemaMappingException | SchemaMappingValidationException e) {
			LOG.error("The 3DCityDB schema mapping is invalid: " + e.getMessage());
			return;
		} 	

		// import schema content	
		LOG.info("Importing metadata into database...");
		DBMetadataImporter importer;
		try {
			importer = new DBMetadataImporter(dbPool);
			importer.doImport(adeSchemaMapping);
		} catch (SQLException | DBMetadataImportException e) {
			LOG.error(e.getMessage());			
			Throwable cause = e.getCause();
			while (cause != null) {
				LOG.error("Cause: " + cause.getMessage());
				cause = cause.getCause();
			}
			return;
		}			

		LOG.info("Import Finished");
	}
	
	private void fetchADEs() {
		try {
			String[] connectConfirm = {Language.I18N.getString("pref.kmlexport.connectDialog.line1"),
					Language.I18N.getString("pref.kmlexport.connectDialog.line3")};

			if (!dbPool.isConnected() &&
					JOptionPane.showConfirmDialog(getTopLevelAncestor(),
							connectConfirm,
							Language.I18N.getString("pref.kmlexport.connectDialog.title"),
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				((ImpExpGui)viewController).connectToDatabase();
			}

			if (dbPool.isConnected()) {
				adeTableModel.reset();

				for (ADERow row: DBUtil.getADEList(dbPool)) {
					if (row == null) continue; 
					adeTableModel.addNewRow(row);
				}
			}
		} 
		catch (SQLException e) {
			LOG.error("Failed to query registered ADEs from database: " + e.getMessage());
		} 
	}

	@Override
	public void handleEvent(Event event) throws Exception {}

}
