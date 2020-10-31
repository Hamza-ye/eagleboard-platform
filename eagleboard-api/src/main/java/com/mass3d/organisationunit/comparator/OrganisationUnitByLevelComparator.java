package com.mass3d.organisationunit.comparator;

import java.util.Comparator;
import com.mass3d.organisationunit.OrganisationUnit;

public class OrganisationUnitByLevelComparator
    implements Comparator<OrganisationUnit>
{
    public static final Comparator<OrganisationUnit> INSTANCE = new OrganisationUnitByLevelComparator();

    @Override
    public int compare( OrganisationUnit o1, OrganisationUnit o2 )
    {
        return ( (Integer) o1.getLevel() ).compareTo( o2.getLevel() );
    }
}
