package com.mass3d.common;

/**
 * Defines the selection of organisation units.
 * 
 * <ul>
 * <li>SELECTED: specified units only.</li>
 * <li>CHILDREN: immediate children of specified units, including specified units.</li>
 * <li>DESCENDANTS: all units in sub-hierarchy of specified units, including specified units.</li>
 * <li>ALL: all units in system.</li>
 * </ul>
 * 
 */
public enum OrganisationUnitSelectionMode
{
    SELECTED, CHILDREN, DESCENDANTS, ACCESSIBLE, CAPTURE,  ALL
}
