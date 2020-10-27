package com.mass3d.dxf2.monitoring;

import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.system.SystemInfo;
import com.mass3d.system.SystemService;
import com.mass3d.system.util.HttpHeadersBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service( "com.mass3d.dxf2.monitoring.MonitoringService" )
public class DefaultMonitoringService
    implements MonitoringService
{
    private static final int PUSH_INTERVAL = DateTimeConstants.MILLIS_PER_MINUTE * 5;
    private static final int PUSH_INITIAL_DELAY = DateTimeConstants.MILLIS_PER_SECOND * 30;
    
    @Autowired
    private SystemService systemService;
    
    @Autowired
    private DhisConfigurationProvider config;

    @Autowired
    private SystemSettingManager systemSettingManager;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private TaskScheduler scheduler;

    @PostConstruct
    public void init()
    {
        Date date = new DateTime().plus( PUSH_INITIAL_DELAY ).toDate();
        
        String url = config.getProperty( ConfigurationKey.SYSTEM_MONITORING_URL );
        
        if ( StringUtils.isNotBlank( url ) )
        {
            log.info( String.format( "Monitoring service configured, URL: %s", url ) );
        }
        
        scheduler.scheduleWithFixedDelay( this::pushMonitoringInfo, date, PUSH_INTERVAL );
        
        log.info( "Scheduled monitoring service" );
    }
    
    @Override
    public void pushMonitoringInfo()
    {
        final Date startTime = new Date();
        
        String url = config.getProperty( ConfigurationKey.SYSTEM_MONITORING_URL );
        String username = config.getProperty( ConfigurationKey.SYSTEM_MONITORING_USERNAME );
        String password = config.getProperty( ConfigurationKey.SYSTEM_MONITORING_URL );
        
        if ( StringUtils.isBlank( url ) )
        {
            log.debug( "Monitoring service URL not configured, aborting monitoring request" );
            return;
        }
        
        SystemInfo systemInfo = systemService.getSystemInfo();
        
        if ( systemInfo == null )
        {
            log.warn( "System info not available, aborting monitoring request" );
            return;
        }

        systemInfo.clearSensitiveInfo();
        
        HttpHeadersBuilder headersBuilder = new HttpHeadersBuilder().withContentTypeJson();
        
        if ( StringUtils.isNotBlank( username ) && StringUtils.isNotBlank( password ) )
        {
            headersBuilder.withBasicAuth( username, password );
        }
                
        HttpEntity<SystemInfo> requestEntity = new HttpEntity<>( systemInfo, headersBuilder.build() );
        
        ResponseEntity<String> response = null;
        HttpStatus sc = null;
        
        try
        {
            response = restTemplate.postForEntity( url, requestEntity, String.class );
            sc = response.getStatusCode();
        }
        catch ( HttpClientErrorException | HttpServerErrorException ex )
        {
            log.warn( String.format( "Monitoring request failed, status code: %s", sc ), ex );
            return;
        }
        catch ( ResourceAccessException ex )
        {
            log.info( "Monitoring request failed, network is unreachable" );
            return;
        }
        
        if ( response != null && sc != null && sc.is2xxSuccessful() )
        {
            systemSettingManager.saveSystemSetting( SettingKey.LAST_SUCCESSFUL_SYSTEM_MONITORING_PUSH, startTime );
            
            log.debug( String.format( "Monitoring request successfully sent, url: %s", url ) );
        }
        else
        {
            log.warn( String.format( "Monitoring request was unsuccessful, status code: %s", sc ) );
        }
    }
}
