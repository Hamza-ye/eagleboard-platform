package com.mass3d.webapi.service;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
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
import static com.mass3d.setting.SettingKey.CACHEABILITY;
import static com.mass3d.setting.SettingKey.CACHE_STRATEGY;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.junit.MockitoJUnit.rule;
import static org.springframework.http.CacheControl.maxAge;
import static org.springframework.http.CacheControl.noCache;

import java.util.Date;

import com.mass3d.analytics.cache.AnalyticsCacheSettings;
import com.mass3d.common.cache.CacheStrategy;
import com.mass3d.common.cache.Cacheability;
import com.mass3d.setting.SystemSettingManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoRule;
import org.springframework.http.CacheControl;

public class WebCacheTest
{

    @Mock
    private SystemSettingManager systemSettingManager;

    @Mock
    private AnalyticsCacheSettings analyticsCacheSettings;

    @Rule
    public MockitoRule mockitoRule = rule();

    private WebCache webCache;

    @Before
    public void setUp()
    {
        webCache = new WebCache( systemSettingManager, analyticsCacheSettings );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsNoCache()
    {
        // Given
        final CacheStrategy theCacheStrategy = NO_CACHE;
        final CacheControl expectedCacheControl = noCache();

        // When
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsRespectSystemSetting()
    {
        // Given
        final CacheStrategy theInputCacheStrategy = RESPECT_SYSTEM_SETTING;
        final CacheStrategy theCacheStrategySet = CACHE_5_MINUTES;
        final CacheControl expectedCacheControl = stubPublicCacheControl( theCacheStrategySet );

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        when( systemSettingManager.getSystemSetting( CACHE_STRATEGY ) ).thenReturn( theCacheStrategySet );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theInputCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetAnalyticsCacheControlForWhenTimeToLiveIsZero()
    {
        // Given
        final long zeroTimeToLive = 0;
        final Date aDate = new Date();
        final CacheControl expectedCacheControl = noCache();

        // When
        when( analyticsCacheSettings.progressiveExpirationTimeOrDefault( aDate ) ).thenReturn( zeroTimeToLive );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( aDate );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetAnalyticsCacheControlForWhenTimeToLiveIsNegative()
    {
        // Given
        final long zeroTimeToLive = -1;
        final Date aDate = new Date();
        final CacheControl expectedCacheControl = noCache();

        // When
        when( analyticsCacheSettings.progressiveExpirationTimeOrDefault( aDate ) ).thenReturn( zeroTimeToLive );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( aDate );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetAnalyticsCacheControlForWhenTimeToLiveIsPositive()
    {
        // Given
        final long positiveTimeToLive = 60;
        final Date aDate = new Date();
        final CacheControl expectedCacheControl = stubPublicCacheControl ( positiveTimeToLive );
        final Cacheability setCacheability = PUBLIC;

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( setCacheability );
        when( analyticsCacheSettings.progressiveExpirationTimeOrDefault( aDate ) ).thenReturn( positiveTimeToLive );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( aDate );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testGetCacheControlForWhenCacheStrategyIsRespectSystemSettingNotUsedInObjectBasis()
    {
        // Given
        final CacheStrategy theInputCacheStrategy = RESPECT_SYSTEM_SETTING;
        final CacheStrategy theCacheStrategySet = RESPECT_SYSTEM_SETTING;

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        when( systemSettingManager.getSystemSetting( CACHE_STRATEGY ) ).thenReturn( theCacheStrategySet );
        webCache.getCacheControlFor( theInputCacheStrategy );

        // Then
        fail( "Should not reach here. Exception was expected: " );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsCache1Minute()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_1_MINUTE;
        final CacheControl expectedCacheControl = stubPublicCacheControl( theCacheStrategy );

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsCache5Minutes()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_5_MINUTES;
        final CacheControl expectedCacheControl = stubPublicCacheControl( theCacheStrategy );

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsCache10Minutes()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_10_MINUTES;
        final CacheControl expectedCacheControl = stubPublicCacheControl( theCacheStrategy );

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsCache15Minutes()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_15_MINUTES;
        final CacheControl expectedCacheControl = stubPublicCacheControl( theCacheStrategy );

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsCache30Minutes()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_30_MINUTES;
        final CacheControl expectedCacheControl = stubPublicCacheControl( theCacheStrategy );

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsCache1Hour()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_1_HOUR;
        final CacheControl expectedCacheControl = stubPublicCacheControl( theCacheStrategy );

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsCache2Weeks()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_TWO_WEEKS;
        final CacheControl expectedCacheControl = stubPublicCacheControl( theCacheStrategy );

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testGetCacheControlForWhenCacheStrategyIsCache6AMTomorrow()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_6AM_TOMORROW;
        final CacheControl expectedCacheControl = stubPublicCacheControl( theCacheStrategy );

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( PUBLIC );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString(), is( expectedCacheControl.toString() ) );
    }

    @Test
    public void testSetCacheabilityWhenCacheabilityIsSetToPublic()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_5_MINUTES;
        final Cacheability setCacheability = PUBLIC;

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( setCacheability );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString().toLowerCase(), containsString( "public" ) );
    }

    @Test
    public void testSetCacheabilityWhenCacheabilityIsSetToPrivate()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_5_MINUTES;
        final Cacheability setCacheability = PRIVATE;

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( setCacheability );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString().toLowerCase(), containsString( "private" ) );
    }

    @Test
    public void testSetCacheabilityWhenCacheabilityIsSetToNull()
    {
        // Given
        final CacheStrategy theCacheStrategy = CACHE_5_MINUTES;
        final Cacheability nullCacheability = null;

        // When
        when( systemSettingManager.getSystemSetting( CACHEABILITY ) ).thenReturn( nullCacheability );
        final CacheControl actualCacheControl = webCache.getCacheControlFor( theCacheStrategy );

        // Then
        assertThat( actualCacheControl.toString().toLowerCase(), not( containsString( "private" ) ) );
        assertThat( actualCacheControl.toString().toLowerCase(), not( containsString( "public" ) ) );
    }

    private CacheControl stubPublicCacheControl( final CacheStrategy cacheStrategy )
    {
        return stubPublicCacheControl( cacheStrategy.toSeconds() );
    }

    private CacheControl stubPublicCacheControl( final long seconds )
    {
        return maxAge( seconds, SECONDS ).cachePublic();
    }
}
