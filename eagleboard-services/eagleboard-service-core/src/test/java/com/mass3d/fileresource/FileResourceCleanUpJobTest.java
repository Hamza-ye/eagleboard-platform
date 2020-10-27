package com.mass3d.fileresource;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

import com.mass3d.todotask.TodoTask;
import com.mass3d.todotask.TodoTaskService;
import java.util.Date;
import com.mass3d.IntegrationTestBase;
import com.mass3d.analytics.AggregationType;
import com.mass3d.common.ValueType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.datavalue.DataValue;
import com.mass3d.datavalue.DataValueAuditService;
import com.mass3d.datavalue.DataValueService;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodService;
import com.mass3d.period.PeriodType;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FileResourceCleanUpJobTest
    extends IntegrationTestBase
{
    @Autowired
    private FileResourceCleanUpJob cleanUpJob;

    @Autowired
    private SystemSettingManager systemSettingManager;

    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private DataValueAuditService dataValueAuditService;
//
    @Autowired
    private DataValueService dataValueService;

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private TodoTaskService todoTaskService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private ExternalFileResourceService externalFileResourceService;

    public static Period PERIOD = createPeriod( PeriodType.getPeriodTypeByName( "Monthly" ), new Date(), new Date() );

    private DataValue dataValueA;

    private DataValue dataValueB;

    private byte[] content;

    @Before
    public void init()
    {
        periodService.addPeriod( PERIOD );
    }

    @Test
    public void testNoRetention()
    {
        systemSettingManager.saveSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY, FileResourceRetentionStrategy.NONE );

        content = "filecontentA".getBytes();
        dataValueA = createFileResourceDataValue( 'A', content );
        assertNotNull( fileResourceService.getFileResource( dataValueA.getValue() ) );

        dataValueService.deleteDataValue( dataValueA );

        cleanUpJob.execute( null );

        assertNull( fileResourceService.getFileResource( dataValueA.getValue() ) );
    }

    @Test
    @Ignore
    public void testRetention()
    {
        systemSettingManager.saveSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY, FileResourceRetentionStrategy.THREE_MONTHS );

        content = "filecontentA".getBytes();
        dataValueA = createFileResourceDataValue( 'A', content );
        assertNotNull( fileResourceService.getFileResource( dataValueA.getValue() ) );

        content = "filecontentB".getBytes();
        dataValueB = createFileResourceDataValue( 'B', content );
        assertNotNull( fileResourceService.getFileResource( dataValueB.getValue() ) );

        content = "fileResourceC".getBytes();
        FileResource fileResource = createFileResource( 'C', content );
        dataValueB.setValue( fileResource.getUid() );
        dataValueService.updateDataValue( dataValueB );
        fileResource.setAssigned( true );

        dataValueAuditService.getDataValueAudits( dataValueB ).get( 0 )
            .setCreated( getDate( 2000, 1, 1 ) );


        cleanUpJob.execute( null );

        assertNotNull( fileResourceService.getFileResource( dataValueA.getValue() ) );
        assertTrue( fileResourceService.getFileResource( dataValueA.getValue() ).isAssigned() );
//        assertNull( dataValueService.getDataValue( dataValueA.getDataElement(), dataValueA.getPeriod(), dataValueA.getSource(), null ) );
        assertNull( fileResourceService.getFileResource( dataValueB.getValue() ) );
    }

    @Test
    @Ignore
    public void testFalsePositive()
    {
        systemSettingManager.saveSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY, FileResourceRetentionStrategy.THREE_MONTHS );

        content = "externalA".getBytes();
        ExternalFileResource ex = createExternal( 'A', content );

        String uid = ex.getFileResource().getUid();
        ex.getFileResource().setAssigned( false );
        fileResourceService.updateFileResource( ex.getFileResource() );

        cleanUpJob.execute( null );

        assertNotNull( externalFileResourceService.getExternalFileResourceByAccessToken( ex.getAccessToken() ) );
        assertNotNull( fileResourceService.getFileResource( uid ) );
        assertTrue( fileResourceService.getFileResource( uid ).isAssigned() );
    }

    @Test
    @Ignore
    public void testFailedUpload()
    {
        systemSettingManager.saveSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY, FileResourceRetentionStrategy.THREE_MONTHS );

        content = "externalA".getBytes();
        ExternalFileResource ex = createExternal( 'A', content );

        String uid = ex.getFileResource().getUid();
        ex.getFileResource().setStorageStatus( FileResourceStorageStatus.PENDING );
        fileResourceService.updateFileResource( ex.getFileResource() );

        cleanUpJob.execute( null );

        assertNull( externalFileResourceService.getExternalFileResourceByAccessToken( ex.getAccessToken() ) );
        assertNull( fileResourceService.getFileResource( uid ) );
    }

    private DataValue createFileResourceDataValue( char uniqueChar, byte[] content )
    {
        DataElement fileElement = createDataElement( uniqueChar, ValueType.FILE_RESOURCE, AggregationType.NONE );
        TodoTask todotask = createTodotask( uniqueChar );

        dataElementService.addDataElement( fileElement );
        todoTaskService.addTodoTask( todotask );

        FileResource fileResource = createFileResource( uniqueChar, content );
        String uid = fileResourceService.saveFileResource( fileResource, content );

        DataValue dataValue = createDataValue( fileElement, PERIOD, todotask, uid);
        fileResource.setAssigned( true );
        fileResource.setCreated( DateTime.now().minus( Days.ONE ).toDate() );
        fileResource.setStorageStatus( FileResourceStorageStatus.STORED );

        fileResourceService.updateFileResource( fileResource );
        dataValueService.addDataValue( dataValue );

        return dataValue;
    }

    private ExternalFileResource createExternal( char uniqueeChar, byte[] constant )
    {
        ExternalFileResource externalFileResource = createExternalFileResource( uniqueeChar, content );

        fileResourceService.saveFileResource( externalFileResource.getFileResource(), content );
        externalFileResourceService.saveExternalFileResource( externalFileResource );

        FileResource fileResource = externalFileResource.getFileResource();
        fileResource.setCreated( DateTime.now().minus( Days.ONE ).toDate() );
        fileResource.setStorageStatus( FileResourceStorageStatus.STORED );

        fileResourceService.updateFileResource( fileResource );
        return externalFileResource;
    }

    @Override
    public boolean emptyDatabaseAfterTest()
    {
        return true;
    }
}
