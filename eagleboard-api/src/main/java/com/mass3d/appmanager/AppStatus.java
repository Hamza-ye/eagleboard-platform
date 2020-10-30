package com.mass3d.appmanager;

public enum AppStatus
{
    OK( "ok" ),
    INVALID_BUNDLED_APP_OVERRIDE( "invalid_bundled_app_override" ),
    INVALID_CORE_APP( "invalid_core_app" ),
    NAMESPACE_TAKEN( "namespace_defined_in_manifest_is_in_use" ),
    INVALID_ZIP_FORMAT( "zip_file_could_not_be_read" ),
    MISSING_MANIFEST( "missing_manifest" ),
    INVALID_MANIFEST_JSON( "invalid_json_in_app_manifest_file" ),
    INSTALLATION_FAILED( "app_could_not_be_installed_on_file_system" ),
    NOT_FOUND( "app_could_not_be_found" ),
    MISSING_SYSTEM_BASE_URL( "system_base_url_is_not_defined" ),
    APPROVED( "approved" ),
    PENDING( "pending" ),
    NOT_APPROVED( "not_approved" ),
    DELETION_IN_PROGRESS( "deletion_in_progress" );

    private String message;

    AppStatus( String message )
    {
        this.message = message;
    }

    public boolean ok()
    {
        return this == OK;
    }

    public String getMessage()
    {
        return message;
    }
}
