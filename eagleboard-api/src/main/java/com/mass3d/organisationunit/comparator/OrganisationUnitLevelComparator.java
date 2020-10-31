package com.mass3d.organisationunit.comparator;

import java.util.Comparator;
import com.mass3d.organisationunit.OrganisationUnitLevel;

/**
 * @version $Id$
 */
public class OrganisationUnitLevelComparator
    implements Comparator<OrganisationUnitLevel>
{
    public static final Comparator<OrganisationUnitLevel> INSTANCE = new OrganisationUnitLevelComparator();

    @Override
    public int compare( OrganisationUnitLevel level1, OrganisationUnitLevel level2 )
    {
        return level1.getLevel() - level2.getLevel();
    }
}
