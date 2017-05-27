
package org.citydb.plugins.ade_manager.gui.components.schemaTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SchemaTableModel extends AbstractTableModel {

	private String[] columnNames = null;
	private ArrayList<SchemaRow> rows = new ArrayList<SchemaRow>();

	public SchemaTableModel() {
		updateColumnsTitle();
	}

	public void updateColumnsTitle() {
		columnNames = new String[1];
		columnNames[0] = "Namespace of visited Schemas";

		fireTableStructureChanged();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public int getRowCount() {
		return rows.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public Object getValueAt(int row, int col) {
		return rows.get(row).getValue(col);
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void setValueAt(Object value, int row, int col) {
		rows.get(row).setValue(col, value);
	}

	public void addNewRow(SchemaRow data) {
		data.rownum = rows.size();
		rows.add(data);
		fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
	}

	public void editRow(SchemaRow data) {
		rows.set(data.rownum, data);
		fireTableRowsUpdated(data.rownum, data.rownum);
	}

	public void removeRow(int[] index) {
		if (index == null || index.length < 0)
			return;
		Arrays.sort(index);
		for (int j = index.length - 1; j >= 0; j--) {
			if (index[j] < rows.size()) {
				rows.remove(index[j]);
				for (int i = index[j]; i < rows.size(); i++)
					rows.get(i).rownum = i;
				fireTableRowsDeleted(index[j], index[j]);
			}
		}
	}

	public void move(int index, boolean moveUp) {
		if (index < 0 || index >= rows.size())
			return;
		if (moveUp && index == 0)
			return;
		if (!moveUp && index == rows.size() - 1)
			return;
		int tmp = rows.get(index).rownum;
		rows.get(index).rownum = rows.get(moveUp ? (index - 1) : (index + 1)).rownum;
		rows.get(moveUp ? (index - 1) : (index + 1)).rownum = tmp;
		Collections.sort(rows);
		fireTableDataChanged();
	}

	public SchemaRow getSchemaColumn(int index) {
		if (index < 0 || index >= rows.size())
			return null;
		return rows.get(index);
	}

	public boolean isSeparatorPhraseSuitable(String phrase) {
		return false;
	}

	public ArrayList<SchemaRow> getRows() {
		return rows;
	}

	public void reset() {
		int size = rows.size();
		rows = new ArrayList<SchemaRow>();
		if (size != 0)
			fireTableRowsDeleted(0, size - 1);
	}

}
