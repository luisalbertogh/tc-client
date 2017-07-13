/**
 * TC URLs.
 */
package com.db.volcker.sdlc;

/**
 * @author garcluia
 *
 */
public enum TC_URLS {
    /** List snapshots */
    SNAPSHOTS_HK("snapshots_hk", "Snapshots housekeeping - Remove old ones"),
    /** List snapshots */
    LIST_BUILDS("list_builds", "http://teamcity.gto.intranet.db.com:8111/app/rest/projects/<project_id>/buildTypes"),
    /** Delete build configuration */
    DELETE_BUILD("delete_build", "http://teamcity.gto.intranet.db.com:8111/app/rest/buildTypes/<build_id>"),
    /** Edit build configuration */
    EDIT_BUILD_CONF("edit_build_conf", "http://teamcity.gto.intranet.db.com:8111/httpAuth/app/rest/buildTypes"),
    /** Get build configuration */
    GET_BUILD_CONF("get_build_conf", "http://teamcity.gto.intranet.db.com:8111/httpAuth/app/rest/builds/");

    String name;
    String url;

    /**
     * Default constructor.
     * 
     * @param name
     * @param url
     */
    TC_URLS(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
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
    public void setName(String name) {
        this.name = name;
    }

}
