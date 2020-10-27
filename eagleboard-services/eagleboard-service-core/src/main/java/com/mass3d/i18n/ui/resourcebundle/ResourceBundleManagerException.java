package com.mass3d.i18n.ui.resourcebundle;

/**
 * @version $Id: ResourceBundleManagerException.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class ResourceBundleManagerException
    extends Exception
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 3993400755227581346L;

    public ResourceBundleManagerException( String message )
    {
        super( message );
    }

    public ResourceBundleManagerException( String message, Throwable throwable )
    {
        super( message, throwable );
    }
}
