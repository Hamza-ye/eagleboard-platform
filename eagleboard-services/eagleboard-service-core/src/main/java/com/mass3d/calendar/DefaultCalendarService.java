package com.mass3d.calendar;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import com.mass3d.calendar.impl.Iso8601Calendar;
import com.mass3d.period.Cal;
import com.mass3d.period.PeriodType;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service( "com.mass3d.calendar.CalendarService" )
public class DefaultCalendarService
    implements CalendarService
{
    private SystemSettingManager settingManager;

    private Set<Calendar> calendars;

    @Autowired
    public DefaultCalendarService( SystemSettingManager settingManager, Set<Calendar> calendars )
    {
        checkNotNull( settingManager );
        checkNotNull( calendars );

        this.settingManager = settingManager;
        this.calendars = calendars;
    }

    private Map<String, Calendar> calendarMap = Maps.newHashMap();

    private static final List<DateFormat> DATE_FORMATS = Lists.newArrayList(
        new DateFormat( "yyyy-MM-dd", "yyyy-MM-dd", "yyyy-MM-dd", "yyyy-mm-dd" ),
        new DateFormat( "dd-MM-yyyy", "dd-MM-yyyy", "dd-MM-yyyy", "dd-mm-yyyy" )
    );

    // -------------------------------------------------------------------------
    // CalendarService implementation
    // -------------------------------------------------------------------------

    @PostConstruct
    public void init()
    {
        for ( Calendar calendar : calendars )
        {
            calendarMap.put( calendar.name(), calendar );
        }

        PeriodType.setCalendarService( this );
        Cal.setCalendarService( this );
        DateUnitPeriodTypeParser.setCalendarService( this );
    }

    @Override
    public List<Calendar> getAllCalendars()
    {
        List<Calendar> sortedCalendars = Lists.newArrayList( calendarMap.values() );
        Collections.sort( sortedCalendars, CalendarComparator.INSTANCE );
        return sortedCalendars;
    }

    @Override
    public List<DateFormat> getAllDateFormats()
    {
        return DATE_FORMATS;
    }

    @Override
    public Calendar getSystemCalendar()
    {
        String calendarKey = (String) settingManager.getSystemSetting( SettingKey.CALENDAR );
        String dateFormat = (String) settingManager.getSystemSetting( SettingKey.DATE_FORMAT );

        Calendar calendar = null;

        if ( calendarMap.containsKey( calendarKey ) )
        {
            calendar = calendarMap.get( calendarKey );
        }
        else
        {
            calendar = Iso8601Calendar.getInstance();
        }

        calendar.setDateFormat( dateFormat );

        return calendar;
    }

    @Override
    public DateFormat getSystemDateFormat()
    {
        String dateFormatKey = (String) settingManager.getSystemSetting( SettingKey.DATE_FORMAT );

        for ( DateFormat dateFormat : DATE_FORMATS )
        {
            if ( dateFormat.name().equals( dateFormatKey ) )
            {
                return dateFormat;
            }
        }

        return DATE_FORMATS.get( 0 );
    }
}
