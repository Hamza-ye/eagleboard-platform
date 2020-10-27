package com.mass3d.period;

import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.DateTimeUnit;
import org.joda.time.DateTimeConstants;

public class SixMonthlyNovemberPeriodType
    extends SixMonthlyAbstractPeriodType
{

    private static final long serialVersionUID = 234137239008575913L;

    private static final String ISO_FORMAT = "yyyyNovSn";

    private static final String ISO8601_DURATION = "P6M";

    private static final int BASE_MONTH = DateTimeConstants.NOVEMBER;

    public static final String NAME = "SixMonthlyNov";

    // -------------------------------------------------------------------------
    // PeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public int getBaseMonth()
    {
        return BASE_MONTH;
    }
    
    @Override
    public Period createPeriod( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        DateTimeUnit start = new DateTimeUnit( dateTimeUnit );

        int baseMonth = getBaseMonth();
        int year = start.getYear();
        int month = baseMonth;
        
        if ( start.getMonth() < 5 )
        {
            month = baseMonth;
            year = year - 1;
        }
        
        if ( start.getMonth() >= 5 && start.getMonth() <= 10 ) 
        {
            month = baseMonth - 6;
        }
        
        start.setYear( year );
        start.setMonth( month );
        start.setDay( 1 );        

        DateTimeUnit end = new DateTimeUnit( start );
        end = calendar.plusMonths( end, 5 );
        end.setDay( calendar.daysInMonth( end.getYear(), end.getMonth() ) );

        return toIsoPeriod( start, end, calendar );
    }
    

    // -------------------------------------------------------------------------
    // CalendarPeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        int month = dateTimeUnit.getMonth();

        if ( dateTimeUnit.isIso8601() )
        {
            month = calendar.fromIso( dateTimeUnit ).getMonth();
        }

        switch ( month )
        {
            case 11:
                return dateTimeUnit.getYear() + 1 + "NovS1";
            case 5:
                return dateTimeUnit.getYear() + "NovS2";
            default:
                throw new IllegalArgumentException( "Month not valid [11,5]" );
        }
    }

    @Override
    public String getIsoFormat()
    {
        return ISO_FORMAT;
    }

    @Override
    public String getIso8601Duration()
    {
        return ISO8601_DURATION;
    }

}
