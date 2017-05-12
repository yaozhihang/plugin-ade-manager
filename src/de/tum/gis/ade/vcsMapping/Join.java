package de.tum.gis.ade.vcsMapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "join", propOrder = {
    "condition"
})
public class Join
    extends AbstractJoin
{

    protected List<Condition> condition;
    @XmlAttribute(required = true)
    protected String table;
    @XmlAttribute(required = true)
    protected String fromColumn;
    @XmlAttribute(required = true)
    protected String toColumn;
    
    protected Join() {
    	
    }

    public List<Condition> getConditions() {
    	if (condition == null) {
    		condition = new ArrayList<Condition>();
    	}
        return condition;
    }

    public void setConditions(List<Condition> value) {
        this.condition = value;
    }

    public boolean isSetConditions() {
        return ((this.condition!= null)&&(!condition.isEmpty()));
    }

    public String getTable() {
        return table;
    }

    public void setTable(String value) {
        this.table = value;
    }

    public boolean isSetTable() {
        return (this.table!= null);
    }

    public String getFromColumn() {
        return fromColumn;
    }

    public void setFromColumn(String value) {
        this.fromColumn = value;
    }

    public boolean isSetFromColumn() {
        return (this.fromColumn!= null);
    }

    public String getToColumn() {
        return toColumn;
    }

    public void setToColumn(String value) {
        this.toColumn = value;
    }

    public boolean isSetToColumn() {
        return (this.toColumn!= null);
    }

}
