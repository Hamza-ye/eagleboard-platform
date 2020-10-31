package com.mass3d.organisationunit;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface OrganisationUnitGroupStore
    extends IdentifiableObjectStore<OrganisationUnitGroup>
{
    List<OrganisationUnitGroup> getOrganisationUnitGroupsWithGroupSets();
}
