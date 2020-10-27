package com.mass3d.dxf2.metadata.sync;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.mass3d.dxf2.metadata.systemsettings.DefaultMetadataSystemSettingService;
import com.mass3d.render.RenderFormat;
import com.mass3d.render.RenderService;
import com.mass3d.system.SystemInfo;
import com.mass3d.system.SystemService;
import com.mass3d.system.util.HttpUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith( PowerMockRunner.class )
@PrepareForTest( HttpUtils.class )
public class MetadataSyncDelegateTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private MetadataSyncDelegate metadataSyncDelegate;

    @Mock
    private DefaultMetadataSystemSettingService metadataSystemSettingService;

    @Mock
    private SystemService systemService;

    @Mock
    private RenderService renderService;

    @Before
    public void setup()
    {
        PowerMockito.mockStatic( HttpUtils.class );

    }

    @Test
    public void testShouldVerifyIfStopSyncReturnFalseIfNoSystemVersionInLocal()
    {
        String versionSnapshot = "{\"system:\": {\"date\":\"2016-05-24T05:27:25.128+0000\", \"version\": \"2.26\"}, \"name\":\"testVersion\",\"created\":\"2016-05-26T11:43:59.787+0000\",\"type\":\"BEST_EFFORT\",\"id\":\"ktwh8PHNwtB\",\"hashCode\":\"12wa32d4f2et3tyt5yu6i\"}";
        SystemInfo systemInfo = new SystemInfo();
        when ( systemService.getSystemInfo() ).thenReturn( systemInfo );
        boolean shouldStopSync = metadataSyncDelegate.shouldStopSync( versionSnapshot );
        assertFalse(shouldStopSync);
    }

    @Test
    public void testShouldVerifyIfStopSyncReturnFalseIfNoSystemVersionInRemote() throws IOException
    {
        String versionSnapshot = "{\"system:\": {\"date\":\"2016-05-24T05:27:25.128+0000\", \"version\": \"2.26\"}, \"name\":\"testVersion\",\"created\":\"2016-05-26T11:43:59.787+0000\",\"type\":\"BEST_EFFORT\",\"id\":\"ktwh8PHNwtB\",\"hashCode\":\"12wa32d4f2et3tyt5yu6i\"}";
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setVersion( "2.26" );
        when ( systemService.getSystemInfo() ).thenReturn( systemInfo );
        boolean shouldStopSync = metadataSyncDelegate.shouldStopSync( versionSnapshot );
        assertFalse(shouldStopSync);
    }

    @Test
    public void testShouldVerifyIfStopSyncReturnTrueIfDHISVersionMismatch() throws IOException
    {
        String versionSnapshot = "{\"system:\": {\"date\":\"2016-06-24T05:27:25.128+0000\", \"version\": \"2.26\"}, \"name\":\"testVersion\",\"created\":\"2016-05-26T11:43:59.787+0000\",\"type\":\"BEST_EFFORT\",\"id\":\"ktwh8PHNwtB\"," +
            "\"hashCode\":\"12wa32d4f2et3tyt5yu6i\"}";
        String systemNodeString = "{\"date\":\"2016-06-24T05:27:25.128+0000\", \"version\": \"2.26\"}";
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setVersion( "2.25" );
        when( systemService.getSystemInfo() ).thenReturn( systemInfo );
        when( metadataSystemSettingService.getStopMetadataSyncSetting() ).thenReturn( true );
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree( systemNodeString );
        when( renderService.getSystemObject( any( ByteArrayInputStream.class), eq( RenderFormat.JSON ) ) ).thenReturn( jsonNode);

        boolean shouldStopSync = metadataSyncDelegate.shouldStopSync( versionSnapshot );
        assertTrue(shouldStopSync);
    }

    @Test
    public void testShouldVerifyIfStopSyncReturnFalseIfDHISVersionSame() throws IOException
    {
        String versionSnapshot = "{\"system:\": {\"date\":\"2016-05-24T05:27:25.128+0000\", \"version\": \"2.26\"}, \"name\":\"testVersion\",\"created\":\"2016-05-26T11:43:59.787+0000\",\"type\":\"BEST_EFFORT\",\"id\":\"ktwh8PHNwtB\",\"hashCode\":\"12wa32d4f2et3tyt5yu6i\"}";
        String systemNodeString = "{\"date\":\"2016-05-24T05:27:25.128+0000\", \"version\": \"2.26\"}";
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setVersion( "2.26" );
        when( systemService.getSystemInfo() ).thenReturn( systemInfo );
        when( metadataSystemSettingService.getStopMetadataSyncSetting() ).thenReturn( true );
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree( systemNodeString );
        when( renderService.getSystemObject(any( ByteArrayInputStream.class), eq( RenderFormat.JSON ) ) ).thenReturn( jsonNode);

        boolean shouldStopSync = metadataSyncDelegate.shouldStopSync( versionSnapshot );
        assertFalse(shouldStopSync);
    }

    @Test
    public void testShouldVerifyIfStopSyncReturnFalseIfStopSyncIsNotSet()
    {
        String versionSnapshot = "{\"system:\": {\"date\":\"2016-05-24T05:27:25.128+0000\", \"version\": \"2.26\"}, \"name\":\"testVersion\",\"created\":\"2016-05-26T11:43:59.787+0000\",\"type\":\"BEST_EFFORT\",\"id\":\"ktwh8PHNwtB\",\"hashCode\":\"12wa32d4f2et3tyt5yu6i\"}";
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setVersion( "2.26" );

        when( systemService.getSystemInfo() ).thenReturn( systemInfo );
        when( metadataSystemSettingService.getStopMetadataSyncSetting() ).thenReturn( false );
        boolean shouldStopSync = metadataSyncDelegate.shouldStopSync( versionSnapshot );
        assertFalse(shouldStopSync);
    }
}
