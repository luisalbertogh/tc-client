/**
 * TeamCity REST API client with Jersey client.
 */
package com.db.volcker.sdlc;

/**
 * @author garcluia
 *
 */
public class TCClient {

    /**
     * Main method.
     * 
     * @param args
     */
    public static void main(String[] args) {
        /* Check argument list */
        if (args.length < 3) {
            System.err.println(
                    "Usage: java -jar tc-client.jar <username:password> <operation> [<parameters> | -f:<inputfile>]");
            System.err.println("<parameters>: param1=value1;param2=value2;prop1:value1|prop2:value2;param3=value3");
            System.err.println("-f:<inputfile>: -f:input.csv");
            System.err.println("Available operations: ");
            System.err.println(
                    "- Get XML build configuration: java -jar tc-client.jar <username:password> get_build_conf <conf_id>");
            System.err.println("- Create or update build configuration. This is for a snapshot:");
            System.err.println(
                    "java -jar tc-client.jar pepe.perez@db.com:mypass123 edit_build_conf \"name=Conf Name;projectid=G1727Volcker_ReleaseCandidates;"
                            + "templateid=G1727Volcker_ReleaseCandidates_VolckerBasicBuildTemplate;vcsrootid=G1727Volcker_732731VolckerSvn1;"
                            + "checkoutrules=+:sdlc/%teamcity.build.branch%=>.;parameters=teamcity.build.branch:dev_TEST\"");
            System.err.println(
                    "- List XML build configurations within a project: java -jar tc-client.jar <username:password> list_builds <project_id>");
            System.err.println(
                    "- Delete a configuration: java -jar tc-client.jar <username:password> delete_build <build_id>");
            System.err.println(
                    "- Perform snapshots housekeeping: java -jar tc-client.jar <username:password> snapshots_hk <project_id>");
            System.exit(0);
        }

        /* Print arguments */
        System.out.println("Invoking: ");
        for (int i = 1; i < args.length; i++) {
            System.out.println("Argument " + i + ": " + args[i]);
        }

        /* Init invoker */
        TCInvoker tcinvoker = new TCInvoker();

        /* Run invoker */
        tcinvoker.run(args[0], args[1], args[2]);
    }
}
