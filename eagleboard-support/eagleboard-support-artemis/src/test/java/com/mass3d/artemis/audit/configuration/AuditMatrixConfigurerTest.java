package com.mass3d.artemis.audit.configuration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static com.mass3d.audit.AuditScope.AGGREGATE;
import static com.mass3d.audit.AuditScope.METADATA;
import static com.mass3d.audit.AuditScope.TRACKER;
import static com.mass3d.audit.AuditType.CREATE;
import static com.mass3d.audit.AuditType.DELETE;
import static com.mass3d.audit.AuditType.READ;
import static com.mass3d.audit.AuditType.SEARCH;
import static com.mass3d.audit.AuditType.SECURITY;
import static com.mass3d.audit.AuditType.UPDATE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Map;
import com.mass3d.audit.AuditScope;
import com.mass3d.audit.AuditType;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class AuditMatrixConfigurerTest
{
    @Mock
    private DhisConfigurationProvider config;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private AuditMatrixConfigurer subject;

    private Map<AuditScope, Map<AuditType, Boolean>> matrix;

    @Before
    public void setUp()
    {
        this.subject = new AuditMatrixConfigurer( config );
    }

    @Test
    public void verifyConfigurationForMatrixIsIngested()
    {
        when( config.getProperty( ConfigurationKey.AUDIT_METADATA_MATRIX ) ).thenReturn( "READ;" );
        when( config.getProperty( ConfigurationKey.AUDIT_TRACKER_MATRIX ) ).thenReturn( "CREATE;READ;UPDATE;DELETE" );
        when( config.getProperty( ConfigurationKey.AUDIT_AGGREGATE_MATRIX ) ).thenReturn( "CREATE;UPDATE;DELETE" );

        matrix = this.subject.configure();

        assertThat( matrix.get( METADATA ).keySet(), hasSize( 6 ) );
        assertMatrixEnabled( METADATA, READ );
        assertMatrixDisabled( METADATA, CREATE, UPDATE, DELETE );

        assertThat( matrix.get( TRACKER ).keySet(), hasSize( 6 ) );
        assertMatrixDisabled( TRACKER, SEARCH, SECURITY );
        assertMatrixEnabled( TRACKER, CREATE, UPDATE, DELETE, READ );

        assertThat( matrix.get( AGGREGATE ).keySet(), hasSize( 6 ) );
        assertMatrixDisabled( AGGREGATE, READ, SECURITY, SEARCH );
        assertMatrixEnabled( AGGREGATE, CREATE, UPDATE, DELETE );
    }

    @Test
    public void allDisabled()
    {
        when( config.getProperty( ConfigurationKey.AUDIT_METADATA_MATRIX ) ).thenReturn( "DISABLED" );
        when( config.getProperty( ConfigurationKey.AUDIT_TRACKER_MATRIX ) ).thenReturn( "DISABLED" );
        when( config.getProperty( ConfigurationKey.AUDIT_AGGREGATE_MATRIX ) ).thenReturn( "DISABLED" );

        matrix = this.subject.configure();

        assertMatrixAllDisabled( METADATA );
        assertMatrixAllDisabled( TRACKER );
        assertMatrixAllDisabled( AGGREGATE  );
    }

    @Test
    public void verifyInvalidConfigurationIsIgnored()
    {
        when( config.getProperty( ConfigurationKey.AUDIT_METADATA_MATRIX ) ).thenReturn( "READX;UPDATE" );

        matrix = this.subject.configure();
        assertThat( matrix.get( METADATA ).keySet(), hasSize( 6 ) );
        assertAllFalseBut( matrix.get( METADATA ), UPDATE );
    }

    @Test
    public void verifyDefaultAuditingConfiguration()
    {
        matrix = this.subject.configure();
        assertMatrixDisabled( METADATA, READ );
        assertMatrixEnabled( METADATA, CREATE );
        assertMatrixEnabled( METADATA, UPDATE );
        assertMatrixEnabled( METADATA, DELETE );

        assertMatrixDisabled( TRACKER, READ );
        assertMatrixEnabled( TRACKER, CREATE );
        assertMatrixEnabled( TRACKER, UPDATE );
        assertMatrixEnabled( TRACKER, DELETE );

        assertMatrixDisabled( AGGREGATE, READ );
        assertMatrixEnabled( AGGREGATE, CREATE );
        assertMatrixEnabled( AGGREGATE, UPDATE );
        assertMatrixEnabled( AGGREGATE, DELETE );
    }

    private void assertAllFalseBut( Map<AuditType, Boolean> auditTypeBooleanMap, AuditType trueAuditType )
    {
        for ( AuditType auditType : auditTypeBooleanMap.keySet() )
        {
            if ( !auditType.name().equals( trueAuditType.name() ) )
            {
                assertFalse( auditTypeBooleanMap.get( auditType ) );
            }
            else
            {
                assertTrue( auditTypeBooleanMap.get( auditType ) );
            }
        }
    }

    private void assertMatrixEnabled( AuditScope auditScope, AuditType... auditTypes )
    {
        for ( AuditType auditType : auditTypes )
        {
            assertThat( "Expecting true for audit type: " + auditType.name(), matrix.get( auditScope ).get( auditType ),
                is( true ) );
        }
    }

    private void assertMatrixDisabled( AuditScope auditScope, AuditType... auditTypes )
    {
        for ( AuditType auditType : auditTypes )
        {
            assertThat( "Expecting false for audit type: " + auditType.name(),
                matrix.get( auditScope ).get( auditType ), is( false ) );
        }
    }

    private void assertMatrixAllDisabled( AuditScope auditScope )
    {
        for ( AuditType auditType : AuditType.values() )
        {
            assertThat( "Expecting false for audit type: " + auditType.name(),
                    matrix.get( auditScope ).get( auditType ), is( false ) );
        }
    }
}