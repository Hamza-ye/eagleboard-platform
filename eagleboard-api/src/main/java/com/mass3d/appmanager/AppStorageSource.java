package com.mass3d.appmanager;

public enum AppStorageSource
{
    // NB! LOCAL AppStorageSource represents the old way of storing apps.
    // This only exists to avoid breaking existing installation of apps
    // installed prior to 2.28. Post 2.28 all apps will be installed using JCLOUDS
    // If DHIS2 is used to install.
    LOCAL,
    JCLOUDS
}
