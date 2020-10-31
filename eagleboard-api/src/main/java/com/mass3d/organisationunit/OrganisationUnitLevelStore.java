package com.mass3d.organisationunit;

import com.mass3d.common.IdentifiableObjectStore;

/**
 * Defines methods for persisting OrganisationUnitLevels.
 *
 */
public interface OrganisationUnitLevelStore
    extends IdentifiableObjectStore<OrganisationUnitLevel>
{
    String ID = OrganisationUnitLevelStore.class.getName();

    /**
     * Deletes all OrganisationUnitLevels.
     */
    void deleteAll();

    /**
     * Gets the OrganisationUnitLevel at the given level.
     *
     * @param level the level.
     * @return the OrganisationUnitLevel at the given level.
     */
    OrganisationUnitLevel getByLevel(int level);
}
