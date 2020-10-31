package com.mass3d.organisationunit.comparator;

import java.util.Comparator;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.organisationunit.OrganisationUnit;

public class OrganisationUnitParentCountComparator
    implements Comparator<IdentifiableObject>
{
    @Override
    public int compare( IdentifiableObject organisationUnit1, IdentifiableObject organisationUnit2 )
    {
        Integer parents1 = ((OrganisationUnit) organisationUnit1).getAncestors().size();
        Integer parents2 = ((OrganisationUnit) organisationUnit2).getAncestors().size();

        return parents1.compareTo( parents2 );
    }
}
