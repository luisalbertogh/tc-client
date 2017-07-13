/**
 * Simple TeamCity build configuration DTO.
 */
package com.db.volcker.sdlc.dto;

import java.io.StringWriter;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author garcluia
 *
 */
@XmlRootElement(name = "buildType")
public final class SimpleBuildConf {
    String name;
    Project project;
    Template template;
    VcsRootEntries rootEntries;
    Parameters params;

    /**
     * Private constructor.
     */
    private SimpleBuildConf() {
        /* EMPTY */
    }

    /**
     * Create a new instance with the passing parameters.
     * 
     * @param parameters
     *            - The simple build configuration parameters.
     * @return A new simple build configuration instance
     */
    public static SimpleBuildConf newInstance(String parameters) {
        SimpleBuildConf sbc = new SimpleBuildConf();

        /* Empty configuration */
        if (parameters == null) {
            return sbc;
        }

        /* Parameters are splitted using ';' */
        StringTokenizer st = new StringTokenizer(parameters, ";");
        while (st.hasMoreTokens()) {
            String param = st.nextToken();
            /* Each param is name=value */
            String[] parts = param.split("=");
            String name = parts[0].trim();
            String value = parts[1].trim();
            /*
             * Just in case there are other '=' in the 'value' field, like in
             * checkout rules
             */
            for (int i = 2; i < parts.length; i++) {
                value += "=" + parts[i].trim();
            }

            /* Set parameters */
            /* Project name */
            if (name.equalsIgnoreCase("name")) {
                sbc.setName(value);
            }
            /* Project id */
            else if (name.equalsIgnoreCase("projectid")) {
                Project p = new Project();
                p.setId(value);
                sbc.setProject(p);
            }
            /* Template id */
            else if (name.equalsIgnoreCase("templateid")) {
                Template t = new Template();
                t.setId(value);
                t.setTemplateFlag(true);
                sbc.setTemplate(t);
            }
            /* Template id */
            else if (name.equalsIgnoreCase("templateid")) {
                Template t = new Template();
                t.setId(value);
                t.setTemplateFlag(true);
                sbc.setTemplate(t);
            }
            /* VCS root id */
            else if (name.equalsIgnoreCase("vcsrootid")) {
                VcsRootEntries vcsentries = new VcsRootEntries();
                vcsentries.setCount(1);
                VcsRootEntry vcsentry = new VcsRootEntry();
                vcsentry.setId(value);
                VcsRoot root = new VcsRoot();
                root.setId(value);
                vcsentry.setRoot(root);
                vcsentries.addEntry(vcsentry);
                sbc.setRootEntries(vcsentries);
            }
            /* Checkout rules */
            else if (name.equalsIgnoreCase("checkoutrules")) {
                VcsRootEntries vcsentries = sbc.getRootEntries();
                VcsRootEntry entry = vcsentries.getEntries().get(0);
                entry.setCheckOutRule(value);
            }
            /* Parameters */
            else if (name.equalsIgnoreCase("parameters")) {
                /* Parameters format is param:value|param:value... */
                StringTokenizer st2 = new StringTokenizer(value, "|");
                Parameters newparams = new Parameters();
                while (st2.hasMoreTokens()) {
                    String p = st2.nextToken();
                    String pname = p.split(":")[0];
                    String pvalue = p.split(":")[1];
                    Property prop = new Property();
                    prop.setName(pname);
                    prop.setValue(pvalue);
                    prop.setOwn(true);
                    newparams.addProperty(prop);
                }
                sbc.setParams(newparams);
            }
        }

        return sbc;
    }

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
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project
     *            the project to set
     */
    @XmlElement(name = "project")
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * @param template
     *            the template to set
     */
    @XmlElement(name = "template")
    public void setTemplate(Template template) {
        this.template = template;
    }

    /**
     * @return the rootEntries
     */
    public VcsRootEntries getRootEntries() {
        return rootEntries;
    }

    /**
     * @param rootEntries
     *            the rootEntries to set
     */
    @XmlElement(name = "vcs-root-entries")
    public void setRootEntries(VcsRootEntries rootEntries) {
        this.rootEntries = rootEntries;
    }

    /**
     * @return the params
     */
    public Parameters getParams() {
        return params;
    }

    /**
     * @param params
     *            the params to set
     */
    @XmlElement(name = "parameters")
    public void setParams(Parameters params) {
        this.params = params;
    }

    /**
     * To string.
     */
    @Override
    public String toString() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SimpleBuildConf.class);
            Marshaller marshall = jaxbContext.createMarshaller();
            marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshall.marshal(this, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        return "";
    }
}
