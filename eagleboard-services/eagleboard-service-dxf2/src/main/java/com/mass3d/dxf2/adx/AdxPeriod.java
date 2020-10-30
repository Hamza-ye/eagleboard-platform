package com.mass3d.dxf2.adx;

import java.util.Calendar;
import java.util.Date;
import com.mass3d.period.BiMonthlyPeriodType;
import com.mass3d.period.DailyPeriodType;
import com.mass3d.period.FinancialAprilPeriodType;
import com.mass3d.period.FinancialJulyPeriodType;
import com.mass3d.period.FinancialOctoberPeriodType;
import com.mass3d.period.MonthlyPeriodType;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;
import com.mass3d.period.QuarterlyPeriodType;
import com.mass3d.period.SixMonthlyAprilPeriodType;
import com.mass3d.period.SixMonthlyPeriodType;
import com.mass3d.period.WeeklyPeriodType;
import com.mass3d.period.YearlyPeriodType;
import com.mass3d.util.DateUtils;

/**
 * ADXPeriod
 *
 * A simple wrapper class for parsing ISO 8601 <date>/<duration> period types
 *
 */
public class AdxPeriod
{
    public enum Duration
    {
        P1D, // daily
        P7D,// weekly
        P1M, // monthly
        P2M, // bi-monthly
        P3M, // quarterly
        P6M, // 6monthly (including 6monthlyApril)
        P1Y  // yearly, financialApril, financialJuly, financialOctober
    }

    public static Period parse( String periodString ) 
        throws AdxException
    {
        String[] tokens = periodString.split( "/" );
        
        if ( tokens.length != 2 )
        {
            throw new AdxException( periodString + " not in valid <date>/<duration> format" );
        }

        try
        {
            Period period;
            PeriodType periodType = null;
            Date startDate = DateUtils.getMediumDate( tokens[0] );
            Calendar cal = Calendar.getInstance();
            cal.setTime( startDate );
            Duration duration = Duration.valueOf( tokens[1] );

            switch ( duration )
            {
                case P1D:
                    periodType = new DailyPeriodType();
                    break;
                case P7D:
                    periodType = new WeeklyPeriodType();
                    break;
                case P1M:
                    periodType = new MonthlyPeriodType();
                    break;
                case P2M:
                    periodType = new BiMonthlyPeriodType();
                    break;
                case P3M:
                    periodType = new QuarterlyPeriodType();
                    break;
                case P6M:
                    switch ( cal.get( Calendar.MONTH ) )
                    {
                        case 0:
                            periodType = new SixMonthlyPeriodType();
                            break;
                        case 6:
                            periodType = new SixMonthlyAprilPeriodType();
                            break;
                        default:
                            throw new AdxException( periodString + "is invalid sixmonthly type" );
                    }
                case P1Y:
                    switch ( cal.get( Calendar.MONTH ) )
                    {
                        case 0:
                            periodType = new YearlyPeriodType();
                            break;
                        case 3:
                            periodType = new FinancialAprilPeriodType();
                            break;
                        case 6:
                            periodType = new FinancialJulyPeriodType();
                            break;
                        case 9:
                            periodType = new FinancialOctoberPeriodType();
                            break;
                        default:
                            throw new AdxException( periodString + "is invalid yearly type" );
                    }
            }

            if ( periodType != null )
            {
                period = periodType.createPeriod( startDate );
            } 
            else
            {
                throw new AdxException( "Failed to create period type from " + duration );
            }

            return period;

        }
        catch ( IllegalArgumentException ex )
        {
            throw new AdxException( tokens[1] + " is not a supported duration type" );
        }
    }

    public static String serialize( Period period )
    {
        return DateUtils.getMediumDateString( period.getStartDate() ) + "/"
            + period.getPeriodType().getIso8601Duration();
    }
}
