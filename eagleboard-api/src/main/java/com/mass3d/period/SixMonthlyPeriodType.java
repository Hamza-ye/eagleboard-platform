package com.mass3d.period;

import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.DateTimeUnit;
import org.joda.time.DateTimeConstants;

/**
 * PeriodType for six-monthly Periods. A valid six-monthly Period has startDate
 * set to either January 1st or July 1st, and endDate set to the last day of the
 * fifth month after the startDate.
 *
 * @version $Id: SixMonthlyPeriodType.java 2971 2007-03-03 18:54:56Z torgeilo $
 */
public class SixMonthlyPeriodType
    extends SixMonthlyAbstractPeriodType
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 5709134010793412705L;

    private static final String ISO_FORMAT = "yyyySn";

    private static final String ISO8601_DURATION = "P6M";

    private static final int BASE_MONTH = DateTimeConstants.JANUARY;

    /**
     * The name of the SixMonthlyPeriodType, which is "SixMonthly".
     */
    public static final String NAME = "SixMonthly";

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
            case 1:
                return dateTimeUnit.getYear() + "S1";
            case 7:
                return dateTimeUnit.getYear() + "S2";
            default:
                throw new IllegalArgumentException( String
                    .format( "Month not valid [1,7]: %d", month ) );
        }
    }

    /**
     * n refers to the semester, can be [1-2].
     */
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
