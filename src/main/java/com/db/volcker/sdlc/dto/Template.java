/**
 * Tempalte node.
 */
package com.db.volcker.sdlc.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author garcluia
 *
 */
@XmlRootElement(name = "template")
public class Template {
    String id;
    Boolean templateFlag;

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

    /**
     * @return the templateFlag
     */
    public Boolean getTemplateFlag() {
        return templateFlag;
    }

    /**
     * @param templateFlag
     *            the templateFlag to set
     */
    @XmlAttribute
    public void setTemplateFlag(Boolean templateFlag) {
        this.templateFlag = templateFlag;
    }
}
