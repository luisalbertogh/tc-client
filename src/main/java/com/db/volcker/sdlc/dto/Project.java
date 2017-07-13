/**
 * Project node.
 */
package com.db.volcker.sdlc.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author garcluia
 *
 */
@XmlRootElement(name = "project")
public class Project {
    String id;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }
}
