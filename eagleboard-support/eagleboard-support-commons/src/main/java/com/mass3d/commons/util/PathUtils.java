package com.mass3d.commons.util;

/**
 * Utility class with methods for managing file system paths.
 *
 */
public class PathUtils
{
    private static final char PACKAGE_SEPARATOR = '.';

    private static final char FILE_SEPARATOR = '/';

    /**
     * Converts the fully qualified class name to a UNIX path.
     * @param clazz the class, separated by '.'.
     * @return the UNIX path, separated by '/'.
     */
    public static String getClassPath( Class<?> clazz )
    {
        return clazz.getName().replace( PACKAGE_SEPARATOR, FILE_SEPARATOR );
    }

    /**
     * Converts the fully qualified class name to a UNIX path.
     * @param clazzName the class name, separated by '.'.
     * @return the UNIX path, separated by '/'.
     */
    public static String getClassPath( String clazzName )
    {
        return clazzName.replace( PACKAGE_SEPARATOR, FILE_SEPARATOR );
    }

    /**
     * Gets the parent path of the given UNIX path.
     * @param path the path.
     * @return the parent path.
     */
    public static String getParent( String path )
    {
        int i = path.lastIndexOf( FILE_SEPARATOR );

        if ( i == -1 )
        {
            return null;
        }

        return path.substring( 0, i );
    }

    /**
     * Adds a child to the given UNIX path.
     * @param parent the parent path.
     * @param child the child path to add.
     * @return the path with the child appended.
     */
    public static String addChild( String parent, String child )
    {
        if ( parent.endsWith( String.valueOf( FILE_SEPARATOR ) ) )
        {
            return parent + child;
        }
        else
        {
            return parent + FILE_SEPARATOR + child;
        }
    }
}
