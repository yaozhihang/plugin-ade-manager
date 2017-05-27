
package org.citydb.plugins.ade_manager.gui.components.schemaTable;

public class SchemaRow implements Comparable<SchemaRow> {
	public int rownum;
	public String namespace = "";

	public SchemaRow() {
		this.rownum = -1;
		this.namespace = "";
	}

	public SchemaRow(String namespace) {
		this.namespace = namespace;
	}

	public String getValue(int col) {
		switch (col) {
		case 0:
			return namespace;
		default:
			return "";
		}
	}

	public void setValues(String namespace) {
		this.namespace = namespace;

	}

	public void setValue(int col, Object obj) {

		switch (col) {
		case 0:
			namespace = (String) obj;
			return;
		}

	}

	@Override
	public int compareTo(SchemaRow o) {
		if (!(o instanceof SchemaRow))
			return 0;
		if (((SchemaRow) o).rownum > this.rownum)
			return -1;
		if (((SchemaRow) o).rownum < this.rownum)
			return 1;
		return 0;

	}

}
