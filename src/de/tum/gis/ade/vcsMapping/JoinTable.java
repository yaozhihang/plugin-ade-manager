package de.tum.gis.ade.vcsMapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "joinTable", propOrder = {
    "join"
})
public class JoinTable
    extends AbstractJoin
{

    @XmlElement(required = true)
    protected List<Join> join;
    @XmlAttribute(required = true)
    protected String table;
    
    protected JoinTable() {
    	
    }

    public List<Join> getJoin() {
        if (join == null) {
            join = new ArrayList<Join>();
        }
        return this.join;
    }

    public boolean isSetJoin() {
        return ((this.join!= null)&&(!this.join.isEmpty()));
    }

    public void unsetJoin() {
        this.join = null;
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

    public void setJoin(List<Join> join) {
        this.join = join;
    }
    
}
