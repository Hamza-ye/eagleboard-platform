package com.mass3d.mock;

import java.util.Date;
import java.util.Random;
import com.mass3d.i18n.I18nFormat;
import com.mass3d.period.Period;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @version $Id$
 */
public class MockI18nFormat
    extends I18nFormat
{
    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern( "yyyy-MM-dd" );
    
    @Override
    public String formatPeriod( Period period )
    {
        Random random = new Random();        
        return "Period_" + FORMAT.print( new DateTime( period.getStartDate() ) ) + "_" + random.nextInt( 1000 );
    }
    
    @Override
    public String formatDate( Date date )
    {
        Random random = new Random();
        return "Date_"  + FORMAT.print( new DateTime( date ) ) + "_" + random.nextInt( 1000 );
    }
    
    @Override
    public Date parseDate( String string )
    {
        return string != null ? FORMAT.parseDateTime( string ).toDate() : null;
    }
}
