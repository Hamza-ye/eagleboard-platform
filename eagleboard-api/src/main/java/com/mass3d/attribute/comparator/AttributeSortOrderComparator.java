package com.mass3d.attribute.comparator;

import java.util.Comparator;
import com.mass3d.attribute.Attribute;

public class AttributeSortOrderComparator
    implements Comparator<Attribute>
{
    public static final Comparator<Attribute> INSTANCE = new AttributeSortOrderComparator();

    @Override
    public int compare( Attribute attribute0, Attribute attribute1 )
    {
        if ( attribute0.getSortOrder() == null || attribute0.getSortOrder() == 0 )
        {
            return attribute0.getName().compareTo( attribute1.getName() );
        }
        if ( attribute1.getSortOrder() == null || attribute1.getSortOrder() == 0 )
        {
            return attribute0.getName().compareTo( attribute1.getName() );
        }

        return attribute0.getSortOrder() - attribute1.getSortOrder();
    }
}
