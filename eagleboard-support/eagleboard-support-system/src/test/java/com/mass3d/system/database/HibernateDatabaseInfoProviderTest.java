package com.mass3d.system.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mock.env.MockEnvironment;

/**
 * Unit tests for {@link HibernateDatabaseInfoProvider}.
 *
 */
public class HibernateDatabaseInfoProviderTest
{
    @Mock
    private DhisConfigurationProvider config;

    @Mock
    private JdbcOperations jdbcTemplate;

    private MockEnvironment environment = new MockEnvironment();

    @Mock
    private ResultSet resultSet;

    private HibernateDatabaseInfoProvider provider;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp()
    {
        environment.setActiveProfiles( "prod" );
        provider = new HibernateDatabaseInfoProvider( config, jdbcTemplate, environment );
    }

    @Test
    @SuppressWarnings( "unchecked" )
    public void init() throws SQLException
    {
        Mockito.when( jdbcTemplate.queryForObject( Mockito.eq( "select postgis_full_version();" ), Mockito.eq( String.class ) ) ).thenReturn( "2" );

        Mockito.when( config.getProperty( Mockito.eq( ConfigurationKey.CONNECTION_URL ) ) ).thenReturn( "jdbc:postgresql:dhisx" );
        Mockito.when( config.getProperty( Mockito.eq( ConfigurationKey.CONNECTION_USERNAME ) ) ).thenReturn( "dhisy" );
        Mockito.when( config.getProperty( Mockito.eq( ConfigurationKey.CONNECTION_PASSWORD ) ) ).thenReturn( "dhisz" );

        Mockito.when( resultSet.getString( Mockito.eq( 1 ) ) ).thenReturn( "PostgreSQL 10.5, compiled by Visual C++ build 1800, 64-bit" );
        Mockito.when( resultSet.getString( Mockito.eq( 2 ) ) ).thenReturn( "dhis2" );
        Mockito.when( resultSet.getString( Mockito.eq( 3 ) ) ).thenReturn( "dhis" );

        Mockito.when( jdbcTemplate.queryForObject( Mockito.eq( "select version(),current_catalog,current_user" ), Mockito.isA( RowMapper.class ) ) )
            .thenAnswer( invocation -> ( (RowMapper<?>) invocation.getArgument( 1 ) ).mapRow( resultSet, 1 ) );

        provider.init();

        final DatabaseInfo databaseInfo = provider.getDatabaseInfo();
        Assert.assertEquals( "jdbc:postgresql:dhisx", databaseInfo.getUrl() );
        Assert.assertEquals( "dhis2", databaseInfo.getName() );
        Assert.assertEquals( "dhis", databaseInfo.getUser() );
        Assert.assertEquals( "dhisz", databaseInfo.getPassword() );
        Assert.assertEquals( "PostgreSQL 10.5", databaseInfo.getDatabaseVersion() );
        Assert.assertTrue( databaseInfo.isSpatialSupport() );
    }
}
