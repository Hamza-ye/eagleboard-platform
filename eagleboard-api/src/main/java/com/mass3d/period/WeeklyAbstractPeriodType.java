package com.mass3d.period;

import com.google.common.collect.Lists;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.DateInterval;
import com.mass3d.calendar.DateIntervalType;
import com.mass3d.calendar.DateTimeUnit;

public abstract class WeeklyAbstractPeriodType extends CalendarPeriodType
{
    protected final String name;

    protected final int startOfWeek;

    protected final String isoFormat;

    protected final String isoDuration;

    protected final int frequencyOrder;

    protected final String sqlInterval;

    protected final String weekPrefix;

    protected WeeklyAbstractPeriodType( String name, int startOfWeek, String isoFormat, String isoDuration,
        int frequencyOrder, String sqlInterval, String weekPrefix )
    {
        this.name = name;
        this.startOfWeek = startOfWeek;
        this.isoFormat = isoFormat;
        this.isoDuration = isoDuration;
        this.frequencyOrder = frequencyOrder;
        this.sqlInterval = sqlInterval;
        this.weekPrefix = weekPrefix;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public int getStartOfWeek()
    {
        return startOfWeek;
    }

    @Override
    public String getIso8601Duration()
    {
        return isoDuration;
    }

    @Override
    public int getFrequencyOrder()
    {
        return frequencyOrder;
    }

    @Override
    public String getSqlInterval()
    {
        return sqlInterval;
    }

    @Override
    public String getIsoFormat()
    {
        return isoFormat;
    }

    @Override
    public Period createPeriod( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        DateTimeUnit start = adjustToStartOfWeek( new DateTimeUnit( dateTimeUnit ), calendar );
        DateTimeUnit end = new DateTimeUnit( start );
        end = calendar.plusDays( end, calendar.daysInWeek() - 1 );

        return toIsoPeriod( start, end, calendar );
    }

    @Override
    public DateTimeUnit getDateWithOffset( DateTimeUnit dateTimeUnit, int offset, Calendar calendar )
    {
        return calendar.plusWeeks( dateTimeUnit, offset );
    }

    /**
     * Generates weekly Periods for the whole year in which the given Period's
     * startDate exists.
     */
    @Override
    public List<Period> generatePeriods( DateTimeUnit start )
    {
        Calendar calendar = getCalendar();
        List<Period> periods = new ArrayList<>();
        start = new DateTimeUnit( start ); // create clone so we don't modify the original start DT

        start.setMonth( 1 );
        start.setDay( 4 );
        start = adjustToStartOfWeek( start, calendar );

        for ( int i = 0; i < calendar.weeksInYear( start.getYear() ); i++ )
        {
            DateInterval interval = calendar.toInterval( start, DateIntervalType.ISO8601_WEEK );
            periods.add( new Period( this, interval.getFrom().toJdkDate(), interval.getTo().toJdkDate() ) );

            start = calendar.plusWeeks( start, 1 );
        }

        return periods;
    }

    /**
     * Generates the last 52 weeks where the last one is the week which the
     * given date is inside.
     */
    @Override
    public List<Period> generateRollingPeriods( DateTimeUnit end, Calendar calendar )
    {
        List<Period> periods = Lists.newArrayList();
        DateTimeUnit iterationDateTimeUnit = adjustToStartOfWeek( end, calendar );
        iterationDateTimeUnit = calendar.minusDays( iterationDateTimeUnit, 357 );

        for ( int i = 0; i < 52; i++ )
        {
            periods.add( createPeriod( iterationDateTimeUnit, calendar ) );
            iterationDateTimeUnit = calendar.plusWeeks( iterationDateTimeUnit, 1 );
        }

        return periods;
    }

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        int year;
        int week;

        if ( calendar.isIso8601() )
        {
            LocalDate date = LocalDate
                .of( dateTimeUnit.getYear(), dateTimeUnit.getMonth(), dateTimeUnit.getDay() );
            WeekFields weekFields = WeekFields.of( PeriodType.MAP_WEEK_TYPE.get( getName() ), 4 );

            year = date.get( weekFields.weekBasedYear() );
            week = date.get( weekFields.weekOfWeekBasedYear() );
        }
        else
        {
            dateTimeUnit = adjustToStartOfWeek( dateTimeUnit, calendar );
            week = calendar.week( dateTimeUnit );

            if ( week == 1 && dateTimeUnit.getMonth() == calendar.monthsInYear() )
            {
                dateTimeUnit.setYear( dateTimeUnit.getYear() + 1 );
            }

            year = dateTimeUnit.getYear();
        }

        return String.format( "%d%s%d", year, weekPrefix, week );
    }

    @Override
    public Date getRewindedDate( Date date, Integer rewindedPeriods )
    {
        Calendar cal = getCalendar();

        date = date != null ? date : new Date();
        rewindedPeriods = rewindedPeriods != null ? rewindedPeriods : 1;

        DateTimeUnit dateTimeUnit = createLocalDateUnitInstance( date );
        dateTimeUnit = cal.minusWeeks( dateTimeUnit, rewindedPeriods );

        return cal.toIso( dateTimeUnit ).toJdkDate();
    }

    public DateTimeUnit adjustToStartOfWeek( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        int weekday = calendar.weekday( dateTimeUnit );

        if ( weekday > startOfWeek )
        {
            dateTimeUnit = calendar.minusDays( dateTimeUnit, weekday - startOfWeek );
        }
        else if ( weekday < startOfWeek )
        {
            dateTimeUnit = calendar.minusDays( dateTimeUnit, weekday + (frequencyOrder - startOfWeek) );
        }

        return dateTimeUnit;
    }
}
