package com.mass3d.webapi.utils;

import static java.time.ZonedDateTime.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static com.mass3d.analytics.AnalyticsCacheTtlMode.FIXED;
import static com.mass3d.analytics.AnalyticsCacheTtlMode.PROGRESSIVE;
import static com.mass3d.common.cache.CacheStrategy.CACHE_10_MINUTES;
import static com.mass3d.common.cache.CacheStrategy.CACHE_15_MINUTES;
import static com.mass3d.common.cache.CacheStrategy.CACHE_1_HOUR;
import static com.mass3d.common.cache.CacheStrategy.CACHE_1_MINUTE;
import static com.mass3d.common.cache.CacheStrategy.CACHE_30_MINUTES;
import static com.mass3d.common.cache.CacheStrategy.CACHE_5_MINUTES;
import static com.mass3d.common.cache.CacheStrategy.CACHE_6AM_TOMORROW;
import static com.mass3d.common.cache.CacheStrategy.CACHE_TWO_WEEKS;
import static com.mass3d.common.cache.CacheStrategy.NO_CACHE;
import static com.mass3d.common.cache.CacheStrategy.RESPECT_SYSTEM_SETTING;
import static com.mass3d.common.cache.Cacheability.PRIVATE;
import static com.mass3d.common.cache.Cacheability.PUBLIC;
import static com.mass3d.setting.SettingKey.ANALYTICS_CACHE_PROGRESSIVE_TTL_FACTOR;
import static com.mass3d.setting.SettingKey.ANALYTICS_CACHE_TTL_MODE;
import static com.mass3d.setting.SettingKey.CACHEABILITY;
import static com.mass3d.setting.SettingKey.CACHE_STRATEGY;
import static com.mass3d.setting.SettingKey.getAsRealClass;
import static com.mass3d.webapi.utils.ContextUtils.getAttachmentFileName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import com.mass3d.common.cache.CacheStrategy;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.webapi.DhisWebSpringTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

public class ContextUtilsTest
    extends DhisWebSpringTest
{
    @Autowired
    private ContextUtils contextUtils;

    @Autowired
    private SystemSettingManager systemSettingManager;

    private HttpServletResponse response;

    @Before
    public void init()
    {
        response = new MockHttpServletResponse();
    }

    @Test
    public void testConfigureResponseReturnsCorrectTypeAndNumberOfHeaders()
    {
        contextUtils.configureResponse( response, null, NO_CACHE, null, false );
        String cacheControl = response.getHeader( "Cache-Control" );

        // Make sure we just have 1 header: Cache-Control
        assertEquals( 1, response.getHeaderNames().size() );
        assertNotNull( cacheControl );
    }

    @Test
    public void testConfigureResponseReturnsCorrectHeaderValueForAllCacheStrategies()
    {
        contextUtils.configureResponse( response, null, NO_CACHE, null, false );
        assertEquals( "no-cache", response.getHeader( "Cache-Control" ) );

        response.reset();
        contextUtils.configureResponse( response, null, CACHE_1_MINUTE, null, false );
        assertEquals( "max-age=60, public", response.getHeader( "Cache-Control" ) );

        response.reset();
        contextUtils.configureResponse( response, null, CACHE_5_MINUTES, null, false );
        assertEquals( "max-age=300, public", response.getHeader( "Cache-Control" ) );

        response.reset();
        contextUtils.configureResponse( response, null, CACHE_10_MINUTES, null, false );
        assertEquals( "max-age=600, public", response.getHeader( "Cache-Control" ) );

        response.reset();
        contextUtils.configureResponse( response, null, CACHE_15_MINUTES, null, false );
        assertEquals( "max-age=900, public", response.getHeader( "Cache-Control" ) );

        response.reset();
        contextUtils.configureResponse( response, null, CACHE_30_MINUTES, null, false );
        assertEquals( "max-age=1800, public", response.getHeader( "Cache-Control" ) );

        response.reset();
        contextUtils.configureResponse( response, null, CACHE_1_HOUR, null, false );
        assertEquals( "max-age=3600, public", response.getHeader( "Cache-Control" ) );

        response.reset();
        contextUtils.configureResponse( response, null, CACHE_TWO_WEEKS, null, false );
        assertEquals( "max-age=1209600, public", response.getHeader( "Cache-Control" ) );

        response.reset();
        contextUtils.configureResponse( response, null, CACHE_6AM_TOMORROW, null, false );
        assertEquals( "max-age=" + CACHE_6AM_TOMORROW.toSeconds() + ", public", response.getHeader( "Cache-Control" ) );

        response.reset();
        systemSettingManager.saveSystemSetting( CACHE_STRATEGY, getAsRealClass( CACHE_STRATEGY.getName(), CACHE_1_HOUR.toString() ) );
        contextUtils.configureResponse( response, null, RESPECT_SYSTEM_SETTING, null, false );
        assertEquals( "max-age=3600, public", response.getHeader( "Cache-Control" ) );
    }

    @Test
    public void testConfigureResponseReturnsCorrectCacheabilityInHeader()
    {
        // Set to public; is default
        systemSettingManager.saveSystemSetting( CACHEABILITY, PUBLIC );

        contextUtils.configureResponse( response, null, CACHE_1_HOUR, null, false );
        assertEquals( "max-age=3600, public", response.getHeader( "Cache-Control" ) );

        // Set to private
        systemSettingManager.saveSystemSetting( CACHEABILITY, PRIVATE );

        response.reset();
        contextUtils.configureResponse( response, null, CACHE_1_HOUR, null, false );
        assertEquals( "max-age=3600, private", response.getHeader( "Cache-Control" ) );
    }

//    @Test
//    public void testConfigureAnalyticsResponseWhenProgressiveIsDisabled()
//    {
//        Calendar dateBeforeToday = getInstance();
//        dateBeforeToday.add( YEAR, -5 );
//
//        DataQueryParams params = newBuilder().withEndDate( dateBeforeToday.getTime() ).build();
//
//        // Progressive caching is not enabled
//        systemSettingManager.saveSystemSetting( ANALYTICS_CACHE_TTL_MODE, FIXED );
//
//        response.reset();
//        contextUtils.configureAnalyticsResponse( response, null, CACHE_1_HOUR, null, false, params.getLatestEndDate() );
//        assertEquals( "max-age=3600, public", response.getHeader( "Cache-Control" ) );
//    }

//    @Test
//    public void testConfigureAnalyticsResponseWhenProgressiveIsEnabledAndCacheStrategyIsOverridden()
//    {
//        // Cache strategy overridden
//        CacheStrategy overriddenCacheStrategy = CACHE_1_HOUR;
//
//        Calendar dateBeforeToday = getInstance();
//        dateBeforeToday.add( YEAR, -5 );
//
//        DataQueryParams params = newBuilder().withEndDate( dateBeforeToday.getTime() ).build();
//
//        // Progressive caching is not enabled
//        systemSettingManager.saveSystemSetting( ANALYTICS_CACHE_TTL_MODE, PROGRESSIVE );
//        systemSettingManager.saveSystemSetting( ANALYTICS_CACHE_PROGRESSIVE_TTL_FACTOR, 10 );
//
//        response.reset();
//        contextUtils.configureAnalyticsResponse( response, null, overriddenCacheStrategy, null, false, params.getLatestEndDate() );
//        assertEquals( "max-age=3600, public", response.getHeader( "Cache-Control" ) );
//    }

//    @Test
//    public void testConfigureAnalyticsResponseWhenProgressiveIsEnabledAndCacheStrategyIsRespectSystemSetting()
//    {
//        Calendar dateBeforeToday = getInstance();
//        dateBeforeToday.add( YEAR, -5 );
//
//        // Cache strategy set to respect system settings
//        CacheStrategy respectSystemSetting = RESPECT_SYSTEM_SETTING;
//
//        // Defined TTL Factor
//        int ttlFactor = 10;
//
//        // Expected timeToLive. See {@link TimeToLive.compute()}
//        long timeToLive = DAYS.between( dateBeforeToday.toInstant(), now() ) * ttlFactor;
//
//        DataQueryParams params = newBuilder().withEndDate( dateBeforeToday.getTime() ).build();
//
//        // Progressive caching is not enabled
//        systemSettingManager.saveSystemSetting( ANALYTICS_CACHE_TTL_MODE, PROGRESSIVE );
//        systemSettingManager.saveSystemSetting( ANALYTICS_CACHE_PROGRESSIVE_TTL_FACTOR, ttlFactor );
//
//        response.reset();
//        contextUtils.configureAnalyticsResponse( response, null, respectSystemSetting, null, false, params.getLatestEndDate() );
//        assertEquals( "max-age=" + timeToLive +", public", response.getHeader( "Cache-Control" ) );
//    }

    @Test
    public void testGetAttachmentFileNameNull()
    {
        assertNull( getAttachmentFileName( null ) );
    }

    @Test
    public void testGetAttachmentFileNameInline()
    {
        assertNull( getAttachmentFileName( "inline; filename=test.txt" ) );
    }

    @Test
    public void testGetAttachmentFileName()
    {
        assertEquals( "test.txt", getAttachmentFileName( "attachment; filename=test.txt" ) );
    }
}
