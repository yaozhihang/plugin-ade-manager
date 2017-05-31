
package org.citydb.plugins.ade_manager.gui.components.adeTable;

public class ADERow implements Comparable<ADERow> {
	public int rownum;

	private String adeid = "";
	private String name = "";
	private String description = "";
	private String version = "";
	private String dbPrefix = "";

	public ADERow() {
		this.rownum = -1;
		this.name = "";
		this.name = "";
		this.description = "";
		this.version = "";
		this.dbPrefix = "";
	}

	public ADERow(String adeid, String name, String description, String version, String dbPrefix) {
		this.adeid = adeid;
		this.name = name;
		this.description = description;
		this.version = version;
		this.dbPrefix = dbPrefix;
	}

	public String getValue(int col) {
		switch (col) {
		case 0:
			return adeid;
		case 1:
			return name;
		case 2:
			return description;
		case 3:
			return version;
		case 4:
			return dbPrefix;			
		default:
			return "";
		}
	}

	public void setValues(String adeid, String name, String description, String version, String dbPrefix) {
		this.adeid = adeid;
		this.name = name;
		this.description = description;
		this.version = version;
		this.dbPrefix = dbPrefix;
	}

	public void setValue(int col, Object obj) {
		switch (col) {
		case 0:
			adeid = (String) obj;		
		case 1:
			name = (String) obj;
			return;
		case 2:
			description = (String) obj;
			return;
		case 3:
			version = (String) obj;
			return;
		case 4:
			dbPrefix = (String) obj;
			return;
		}
	}

	@Override
	public int compareTo(ADERow o) {
		if (!(o instanceof ADERow))
			return 0;
		if (((ADERow) o).rownum > this.rownum)
			return -1;
		if (((ADERow) o).rownum < this.rownum)
			return 1;
		return 0;
	}

}
