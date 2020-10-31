package com.mass3d.validation.comparator;

import java.util.Comparator;
import com.mass3d.validation.ValidationResult;

/**
 * Comparator sorting on the result period.
 * 
 */
public class ValidationResultComparator
    implements Comparator<ValidationResult>
{
    @Override
    public int compare( ValidationResult result1, ValidationResult result2 )
    {
        if ( result1.getPeriod() == null && result2.getPeriod() == null )
        {
            return 0;
        }
        else if ( result1.getPeriod() == null )
        {
            return 1;
        }
        else if ( result2.getPeriod() == null )
        {
            return -1;
        }
        
        return result1.getPeriod().getStartDate().compareTo( result2.getPeriod().getStartDate() );
    }
}
