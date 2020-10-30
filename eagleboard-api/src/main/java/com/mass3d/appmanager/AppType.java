package com.mass3d.appmanager;

public enum AppType
{
    /**
     * Normal app (to be displayed in menus, and rendered through /api/apps/{app-name})
     */
    APP,

    /**
     * Resource, not displayed in any menus. Simple way to have multiple apps using the same JS/CSS bundles etc.
     */
    RESOURCE,

    /**
     * Dashboard widget, can be placed on the main system dashboard as 'widgets' (portlets).
     */
    DASHBOARD_WIDGET,

    /**
     * Tracker dashboard widget, used for tracker capture dashboard.
     */
    TRACKER_DASHBOARD_WIDGET
}
