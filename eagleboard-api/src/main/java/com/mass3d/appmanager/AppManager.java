package com.mass3d.appmanager;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import com.mass3d.user.User;
import org.springframework.core.io.Resource;

public interface AppManager
{
    String ID = AppManager.class.getName();

    String BUNDLED_APP_PREFIX = "dhis-web-";

    ImmutableSet<String> BUNDLED_APPS = ImmutableSet.of(
        // Javascript apps
        "app-management",
        "cache-cleaner",
        "capture",
        "dashboard",
        "data-administration",
        "data-visualizer",
        "data-quality",
        "datastore",
        "event-reports",
        "event-visualizer",
        "import-export",
        "interpretation",
        "maintenance",
        "maps",
        "menu-management",
        "messaging",
        "pivot",
        "reports",
        "scheduler",
        "settings",
        "tracker-capture",
        "translations",
        "usage-analytics",
        "user",
        "user-profile",

        // Struts apps
        "approval",
        "dataentry",
        "maintenance-mobile" );

    /**
     * Returns a list of all installed apps.
     *
     * @param contextPath the context path of this instance.
     * @return list of installed apps
     */
    List<App> getApps(String contextPath);

    /**
     * Returns a list of installed apps.
     *
     * @param appType the app type filter.
     * @param max the max number of apps to return.
     * @return a list of apps.
     */
    List<App> getApps(AppType appType, int max);

    App getApp(String appName);

    /**
     * Returns a list of all installed apps with AppType equal the given Type
     *
     * @return list of installed apps with given AppType
     */
    List<App> getAppsByType(AppType appType, Collection<App> apps);

    /**
     * Returns a list of all installed apps with name equal the given name and
     * operator. Currently supports eq and ilike.
     *
     * @return list of installed apps with given name
     */
    List<App> getAppsByName(String name, Collection<App> apps, String operator);

    /**
     * Returns a list of all installed apps with shortName equal the given name and
     * operator. Currently supports eq and ilike.
     *
     * @return list of installed apps with given name
     */
    List<App> getAppsByShortName(String shortName, Collection<App> apps, String operator);

    /**
     * Returns a list of all installed apps which are either bundled or not bundled
     * operator. Currently supports eq.
     *
     * @return list of installed apps with given isBundled property
     */
    List<App> getAppsByIsBundled(boolean isBundled, Collection<App> apps);

    /**
     * Return a list of all installed apps with given filter list Currently support
     * filtering by AppType and name
     *
     * @param filter
     * @return Return a list of all installed apps with given filter list
     */
    List<App> filterApps(List<String> filter, String contextPath);

    /**
     * Returns the app with the given key (folder name).
     *
     * @param key the app key.
     * @param contextPath the context path of this instance.
     * @return the app with the given key.
     */
    App getApp(String key, String contextPath);

    /**
     * Returns apps which are accessible to the current user.
     *
     * @param contextPath the context path of this instance.
     * @return apps which are accessible to the current user.
     */
    List<App> getAccessibleApps(String contextPath);

    /**
     * Installs the app.
     *
     * @param file the app file.
     * @param fileName the name of the app file.
     * @throws IOException if the app manifest file could not be read.
     */
    AppStatus installApp(File file, String fileName);

    /**
     * Indicates whether the app with the given name exist.
     *
     * @param appName the name of the app-
     * @return true if the app exists.
     */
    boolean exists(String appName);

    /**
     * Deletes the given app.
     *
     * @param app the app to delete.
     * @param deleteAppData decide if associated data in dataStore should be deleted
     *        or not.
     */
    void deleteApp(App app, boolean deleteAppData);

    /**
     * Reload list of apps.
     */
    void reloadApps();

    /**
     * Returns the url of the app repository
     *
     * @return url of app hub
     */
    String getAppHubUrl();

    /**
     * Indicates whether the given app is accessible to the current user.
     *
     * @param app the app.
     * @return true if app is accessible.
     */
    boolean isAccessible(App app);

    /**
     * Indicates whether the given app is accessible to the given user.
     *
     * @param app the app.
     * @param user the user.
     * @return true if app is accessible.
     */
    boolean isAccessible(App app, User user);

    /**
     * Returns the app associated with the namespace, or null if no app is
     * associated.
     *
     * @param namespace the namespace to check
     * @return App or null
     */
    App getAppByNamespace(String namespace);

    /**
     * Looks up and returns the file associated with the app and pageName, if it
     * exists
     *
     * @param app the app to look up files for
     * @param pageName the page requested
     * @return the Resource representing the file, or null if no file was found
     */
    Resource getAppResource(App app, String pageName)
        throws IOException;

    /**
     * Sets the app status to DELETION_IN_PROGRESS.
     *
     * @param app The app that has to be marked as deleted.
     * @return true if the status was changed in this method.
     */
    boolean markAppToDelete(App app);

}
