package com.mass3d.hierarchy;

/**
 * An Exception class that can be used for illegal operations on a hierarchy.
 *
 * @version $Id: HierarchyViolationException.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class HierarchyViolationException
    extends RuntimeException
{
    public HierarchyViolationException( String message )
    {
        super( message );
    }
}
