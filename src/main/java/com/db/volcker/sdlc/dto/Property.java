/**
 * Property node.
 */
package com.db.volcker.sdlc.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author garcluia
 *
 */
@XmlRootElement(name = "property")
public class Property {
    String name;
    String value;
    Boolean own;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    @XmlAttribute
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the own
     */
    public Boolean getOwn() {
        return own;
    }

    /**
     * @param own
     *            the own to set
     */
    @XmlAttribute
    public void setOwn(Boolean own) {
        this.own = own;
    }
}
