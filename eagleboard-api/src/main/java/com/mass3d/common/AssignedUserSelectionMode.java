package com.mass3d.common;

/**
 * Defines the selection of assigned user.
 * 
 * <ul>
 * <li>CURRENT: The current logged in user.</li>
 * <li>PROVIDED: The user provided in the param/payload.</li>
 * <li>NONE: Unassigned.</li>
 * <li>ANY: Assigned to anyone.</li>
 * </ul>
 * 
 */
public enum AssignedUserSelectionMode
{
    CURRENT, PROVIDED, NONE, ANY;
}
