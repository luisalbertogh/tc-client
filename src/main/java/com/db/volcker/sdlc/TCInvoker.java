/**
 * Invoke TC REST API.
 */
package com.db.volcker.sdlc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.db.volcker.sdlc.dto.SimpleBuildConf;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * @author garcluia
 *
 */
public class TCInvoker {
    /** Volcker SVN URL */
    private static final String SVN_URL = "https://www.dbcde.com/svn/repos/volcker-source-repository/";
    /** The Jersey client */
    private Client client;

    /**
     * Default constructor.
     */
    public TCInvoker() {
        client = Client.create();
    }

    /**
     * Run invocations.
     * 
     * @param credentials
     *            - TC user name and password
     * @param operation
     *            - The operation name
     * @param parameters
     *            - The parameters set
     */
    public void run(String credentials, String operation, String parameters) {
        String response = "";

        /* Add basic authentication */
        addBasicAuthentication(credentials.split(":")[0], credentials.split(":")[1]);

        /* Get build configuration */
        if (operation.equalsIgnoreCase(TC_URLS.GET_BUILD_CONF.getName())) {
            response = getBuildConf(Long.parseLong(parameters));
        }
        /* Create build configuration */
        else if (operation.equalsIgnoreCase(TC_URLS.EDIT_BUILD_CONF.getName())) {
            /*
             * Create build configuration with input file (only for SNAPSHOTS)
             */
            if (parameters != null && parameters.startsWith("-f")) {
                createBuildConfWithFile(parameters.split(":")[1]);
            }
            /* Create build configuration with input parameters */
            else {
                response = createBuildConf(SimpleBuildConf.newInstance(parameters));
            }
        }
        /* List build configurations */
        else if (operation.equalsIgnoreCase(TC_URLS.LIST_BUILDS.getName())) {
            response = listBuildConfs(parameters);
        }
        /* Delete build configuration */
        else if (operation.equalsIgnoreCase(TC_URLS.DELETE_BUILD.getName())) {
            response = deleteBuildConf(parameters);
        }
        /* Snapshots housekeeping */
        else if (operation.equalsIgnoreCase(TC_URLS.SNAPSHOTS_HK.getName())) {
            response = housekeepSnapshot(parameters);
        }

        /* Print response */
        System.out.println(response);
    }

    /**
     * Perform snapshots housekeeping.
     * 
     * @param projectId
     *            - The project id
     * @return Received response
     */
    private String housekeepSnapshot(String projectId) {
        /* Retrieve snapshot list */
        String snapshotList = listBuildConfs(projectId);
        int deletedSnapshots = 0;
        if (snapshotList != null && !snapshotList.equals("")) {
            deletedSnapshots = processBuildList(snapshotList, false);
        } else {
            System.out.println("[tc-client] Snapshots list is empty.");
        }
        return deletedSnapshots + " has been removed.";
    }

    /**
     * Process XML snapshot build list.
     * 
     * @param snapshotList
     * @param offline
     */
    public int processBuildList(String snapshotList, boolean offline) {
        int deletedSnapshots = 0;
        try {
            /* Build XML doc */
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new ByteArrayInputStream(snapshotList.getBytes("UTF-8")));

            /* Get root node */
            Element root = doc.getRootElement();
            List<Element> builds = root.getChildren("buildType");
            for (Element build : builds) {
                String id = build.getAttribute("id").getValue();
                String name = build.getAttribute("name").getValue();
                /* Is conf too old? */
                if (isTooOld(name)) {
                    System.out.println("[tc-client] Snapshot conf " + name + " is too old. Proceed to remove...");
                    if (!offline) {
                        deleteBuildConf(id);
                    }
                    deletedSnapshots++;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("[tc-client] Failed: Snapshot HK failed: " + ex.getMessage());
        }

        return deletedSnapshots;
    }

    /**
     * Determine wether snapshot build is too old.
     * 
     * @param name
     *            - The build configuration
     * @return True or false
     * @throws ParseException
     */
    private boolean isTooOld(String name) {
        try {
            String[] tokens = name.split("- ");
            String date = tokens[2].trim();

            /* Old snapshot date */
            DateFormat df = new SimpleDateFormat("yy.MM.dd");
            /* New date format */
            if (date.indexOf(".") == -1) {
                df = new SimpleDateFormat("yyyyMMdd");
            }

            Date snapDate = df.parse(date);
            Calendar snapCal = Calendar.getInstance();
            snapCal.setTime(snapDate);

            /* Now */
            Calendar cal = Calendar.getInstance();

            /* Minus one month */
            // cal.add(Calendar.MONTH, -1);

            /* Check if snap date is older than current date */
            if (snapCal.before(cal)) {
                return true;
            }
        } catch (Exception ex) {
            System.out.println("ERROR " + name);
            return false;
        }
        return false;
    }

    /**
     * Create a set of build configuration using the CSV input file.
     * 
     * @param filepath
     *            - The input filepath
     */
    private void createBuildConfWithFile(String filepath) {
        System.out.println("[tc-client] Create build configurations with file: " + filepath);

        /* Read from file */
        try (BufferedReader bf = new BufferedReader(new FileReader(filepath))) {
            String line = null;
            while ((line = bf.readLine()) != null) {
                /* Skip comments */
                if (line.startsWith("#")) {
                    continue;
                }

                String[] tokens = line.split(",");

                /* Needed input data */
                String svnurl = tokens[0];
                String fixversion = tokens[2];
                String artifactid = tokens[4];

                /* Create a new configuration request */
                String name = "Volcker [CI] - " + artifactid + " - " + fixversion;
                String baseUrl = svnurl.replace(SVN_URL, "");
                String parameters = "name=" + name + ";projectid=G1727Volcker_ReleaseCandidates;"
                        + "templateid=G1727Volcker_ReleaseCandidates_VolckerBasicBuildTemplate;vcsrootid=G1727Volcker_732731VolckerSvn1;"
                        + "checkoutrules=+:" + baseUrl + "/branches/queued-releases/rel-" + fixversion
                        + "=>.;parameters=teamcity.build.branch:rel-" + fixversion;

                /* Request a build configuration */
                System.out.println(createBuildConf(SimpleBuildConf.newInstance(parameters)));
            }
        } catch (Exception ex) {
            throw new RuntimeException(
                    "[tc-client] Failed: Error reading input file: " + filepath + "\n" + ex.getMessage());
        }
    }

    /**
     * Add basic authentication.
     * 
     * @param username
     *            - The username
     * @param password
     *            - The authentication
     */
    private void addBasicAuthentication(String username, String password) {
        client.addFilter(new HTTPBasicAuthFilter(username, password));
    }

    /**
     * List build configurations from a project.
     * 
     * @param projectId
     *            - The project id
     * @return Received response
     */
    private String listBuildConfs(String projectId) {
        String url = TC_URLS.LIST_BUILDS.getUrl().replace("<project_id>", projectId);
        WebResource wr = client.resource(url);
        ClientResponse response = wr.accept("application/xml").get(ClientResponse.class);
        String resStr = response.getEntity(String.class);

        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new RuntimeException("[tc-client] Failed: HTTP error code: " + response.getStatus() + "\n" + resStr);
        }

        return resStr;
    }

    /**
     * Delete build configuration.
     * 
     * @param buildId
     *            - The build id
     * @return Received response
     */
    private String deleteBuildConf(String buildId) {
        String url = TC_URLS.DELETE_BUILD.getUrl().replace("<build_id>", buildId);
        WebResource wr = client.resource(url);
        wr.delete(ClientResponse.class);
        return "OK";
    }

    /**
     * Create snapshot build configuration.
     * 
     * @param confId
     *            - The configuration id
     * @return Received response
     */
    private String getBuildConf(Long confId) {
        WebResource wr = client.resource(TC_URLS.GET_BUILD_CONF.getUrl() + confId.toString());
        ClientResponse response = wr.accept("application/xml").get(ClientResponse.class);
        String resStr = response.getEntity(String.class);

        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new RuntimeException("[tc-client] Failed: HTTP error code: " + response.getStatus() + "\n" + resStr);
        }

        return resStr;
    }

    /**
     * Create snapshot build configuration.
     * 
     * @param buildConf
     *            - The snapshot build configuration details
     * @return Received response
     */
    private String createBuildConf(SimpleBuildConf buildConf) {
        System.out.println("[tc-client] Create build configuration for: " + buildConf.getName());
        WebResource wr = client.resource(TC_URLS.EDIT_BUILD_CONF.getUrl());
        ClientResponse response = wr.type("application/xml").post(ClientResponse.class, buildConf);

        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new RuntimeException("[tc-client] Failed: HTTP error code: " + response.getStatus() + "\n"
                    + response.getEntity(String.class));
        }

        return Status.OK.toString();
    }

    /**
     * Read the file.
     * 
     * @param filepath
     * @return
     */
    public String readFile(String filepath) {
        StringBuffer sb = new StringBuffer();
        try (BufferedReader bf = new BufferedReader(new FileReader(filepath))) {
            String line = null;
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sb.toString();
    }
}
