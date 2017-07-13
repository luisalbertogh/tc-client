/**
 * VCS root entries node.
 */
package com.db.volcker.sdlc.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author garcluia
 *
 */
@XmlRootElement(name = "vcs-root-entries")
public class VcsRootEntries {
    Integer count;
    List<VcsRootEntry> entries;

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    @XmlAttribute
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the entries
     */
    public List<VcsRootEntry> getEntries() {
        return entries;
    }

    /**
     * @param entries
     *            the entries to set
     */
    @XmlElement(name = "vcs-root-entry")
    public void setEntries(List<VcsRootEntry> entries) {
        this.entries = entries;
    }

    /**
     * Add entry.
     * 
     * @param entry
     */
    public void addEntry(VcsRootEntry entry) {
        if (entries == null) {
            entries = new ArrayList<VcsRootEntry>();
        }

        entries.add(entry);
    }
}
