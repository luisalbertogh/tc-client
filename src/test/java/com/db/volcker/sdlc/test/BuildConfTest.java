/**
 * 
 */
package com.db.volcker.sdlc.test;

import org.junit.Test;

import com.db.volcker.sdlc.TCInvoker;
import com.db.volcker.sdlc.dto.Parameters;
import com.db.volcker.sdlc.dto.Project;
import com.db.volcker.sdlc.dto.Property;
import com.db.volcker.sdlc.dto.SimpleBuildConf;
import com.db.volcker.sdlc.dto.Template;
import com.db.volcker.sdlc.dto.VcsRoot;
import com.db.volcker.sdlc.dto.VcsRootEntries;
import com.db.volcker.sdlc.dto.VcsRootEntry;

/**
 * @author garcluia
 *
 */
public class BuildConfTest {

    /**
     * Test snapshot hk.
     */
    @Test
    public void testSnapshotHK() {
        TCInvoker tc = new TCInvoker();
        String snapshotlist = tc.readFile("src/test/resources/tc_output.txt");
        tc.processBuildList(snapshotlist, true);
    }

    /**
     * Test build configuration from parameters string.
     */
    @Test
    public void testBuildConf() {
        SimpleBuildConf sbc = SimpleBuildConf.newInstance(
                "name=SNAPSHOT-TEST;projectid=G1727Volcker_ReleaseCandidates;templateid=G1727Volcker_ReleaseCandidates_VolckerBasicBuildTemplate;"
                        + "vcsrootid=G1727Volcker_732731VolckerSvn1;checkoutrules=+:sdlc/%teamcity.build.branch%=>.;parameters=teamcity.build.branch:dev_TEST");
        System.out.println(sbc.toString());
    }

    /**
     * Test snapshot configuration
     */
    @Test
    public void testSnapshotConf() {
        SimpleBuildConf snapshot = SimpleBuildConf.newInstance(null);

        /* Configuration name */
        snapshot.setName("Volcker [CI] - Test Conf REST API");

        /* Parent project */
        Project p = new Project();
        p.setId("G1727Volcker_ReleaseCandidates");
        snapshot.setProject(p);

        /* Template */
        Template t = new Template();
        t.setId("G1727Volcker_ReleaseCandidates_VolckerBasicBuildTemplate");
        t.setTemplateFlag(true);
        snapshot.setTemplate(t);

        /* VCS root entries */
        VcsRootEntries rootEntries = new VcsRootEntries();
        rootEntries.setCount(1);

        /* Entries */
        VcsRootEntry entry = new VcsRootEntry();
        entry.setId("G1727Volcker_732731VolckerSvn1");
        entry.setCheckOutRule("+:sdlc/%teamcity.build.branch%=>.");
        /* Root */
        VcsRoot root = new VcsRoot();
        root.setId("G1727Volcker_732731VolckerSvn1");
        entry.setRoot(root);

        rootEntries.addEntry(entry);
        snapshot.setRootEntries(rootEntries);

        /* Parameters */
        Parameters params = new Parameters();
        /* Build branch */
        Property prop = new Property();
        prop.setName("teamcity.build.branch");
        prop.setValue("dev_TEST");
        prop.setOwn(true);
        params.addProperty(prop);
        snapshot.setParams(params);

        /* Print */
        System.out.println(snapshot);
    }

}
