package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "extension", propOrder = {
    "join"
})
public class Extension<T extends AbstractObjectType<T>> {

    @XmlElements({
        @XmlElement(type = Join.class),
        @XmlElement(name = "joinTable", type = JoinTable.class)
    })
    protected AbstractJoin join;
    @XmlAttribute(required=true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected T base;
    
    protected Extension() {
    	
    }

    public AbstractJoin getJoin() {
        return join;
    }

    public void setJoin(AbstractJoin value) {
        this.join = value;
    }

    public boolean isSetJoin() {
        return (this.join!= null);
    }

    public T getBase() {
        return base;
    }

    public void setBase(T value) {
        this.base = value;
    }

    public boolean isSetBase() {
        return (this.base!= null);
    }

}
