/**
 * VCS root entry node
 */
package com.db.volcker.sdlc.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author garcluia
 *
 */
@XmlRootElement(name = "vcs-root-entry")
public class VcsRootEntry {
    String id;
    VcsRoot root;
    String checkOutRule;

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
     * @return the root
     */
    public VcsRoot getRoot() {
        return root;
    }

    /**
     * @param root
     *            the root to set
     */
    @XmlElement(name = "vcs-root")
    public void setRoot(VcsRoot root) {
        this.root = root;
    }

    /**
     * @return the checkOutRule
     */
    public String getCheckOutRule() {
        return checkOutRule;
    }

    /**
     * @param checkOutRule
     *            the checkOutRule to set
     */
    @XmlElement(name = "checkout-rules")
    public void setCheckOutRule(String checkOutRule) {
        this.checkOutRule = checkOutRule;
    }
}
