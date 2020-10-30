package com.mass3d.appmanager;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import com.mass3d.cache.Cache;
import org.springframework.core.io.Resource;

public interface AppStorageService
{

    String MANIFEST_FILENAME = "manifest.webapp";
    String APPS_DIR = "apps";

    /**
     * Looks trough the appropriate directory of apps to find all installed apps
     * using the AppStorageService
     *
     * @return A map of all app names and apps found
     */
    Map<String, App> discoverInstalledApps();

    /**
     * Returns a map of namespaces and the apps resesrving them.
     * 
     * @return a map of namespaces and the apps reserving them
     */
    Map<String,App> getReservedNamespaces();

    /**
     * Installs an app using the AppServiceStore.
     * 
     * @param file the zip file containing the app
     * @param filename The name of the file
     * @param appCache The app cache
     * @return The status of the installation
     */
    App installApp(File file, String filename, Cache<App> appCache);

    /**
     * Deletes an app from the AppHubService.
     * 
     * @param app the app to delete
     * @return true if app is deleted, false if something fails
     */
    boolean deleteApp(App app);

    /**
     * Looks up and returns a resource representing the page for the app requested. If
     * the resource is not found, return null.
     * 
     * @param app the app to look up
     * @param pageName the name of the page to look up
     * @return The resource representing the page, or null if not found
     */
    Resource getAppResource(App app, String pageName)
        throws IOException;
}
