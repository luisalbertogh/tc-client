/**
 * Parameters node.
 */
package com.db.volcker.sdlc.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author garcluia
 *
 */
@XmlRootElement(name = "parameters")
public class Parameters {
    List<Property> properties;

    /**
     * @return the properties
     */
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * @param properties
     *            the properties to set
     */
    @XmlElement(name = "property")
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    /**
     * Add property.
     * 
     * @param prop
     */
    public void addProperty(Property prop) {
        if (this.properties == null) {
            this.properties = new ArrayList<Property>();
        }

        this.properties.add(prop);
    }
}
