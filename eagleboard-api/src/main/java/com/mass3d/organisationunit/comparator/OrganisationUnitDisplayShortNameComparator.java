package com.mass3d.organisationunit.comparator;

import java.util.Comparator;
import com.mass3d.organisationunit.OrganisationUnit;

public class OrganisationUnitDisplayShortNameComparator
    implements Comparator<OrganisationUnit>
{ 
    public static final Comparator<OrganisationUnit> INSTANCE = new OrganisationUnitDisplayShortNameComparator();

    @Override
    public int compare( OrganisationUnit organisationUnit1, OrganisationUnit organisationUnit2 )
    {
        return organisationUnit1.getDisplayShortName().compareTo( organisationUnit2.getDisplayShortName() );
    }
}
