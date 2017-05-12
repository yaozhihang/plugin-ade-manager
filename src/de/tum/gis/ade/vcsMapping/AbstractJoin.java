package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "abstractJoin")
@XmlSeeAlso({
    Join.class,
    JoinTable.class
})
public abstract class AbstractJoin {

	
}
