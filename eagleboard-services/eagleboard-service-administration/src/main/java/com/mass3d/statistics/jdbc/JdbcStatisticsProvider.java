package com.mass3d.statistics.jdbc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import com.mass3d.common.Objects;
import com.mass3d.statistics.StatisticsProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @version $Id$
 */
@Service( "com.mass3d.statistics.StatisticsProvider" )
public class JdbcStatisticsProvider
    implements StatisticsProvider
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    /**
     * Read only JDBC template.
     */
    private final JdbcTemplate jdbcTemplate;

    public JdbcStatisticsProvider( JdbcTemplate jdbcTemplate )
    {
        checkNotNull( jdbcTemplate );
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // StatisticsProvider implementation
    // -------------------------------------------------------------------------

    @Override
    public Map<Objects, Integer> getObjectCounts()
    {
        final Map<Objects, Integer> objectCounts = new HashMap<>();

        objectCounts.put( Objects.ACTIVITY, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM activity", Integer.class ) );
        objectCounts.put( Objects.TODOTASK, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM todotask", Integer.class ) );
        objectCounts.put( Objects.PROJECT, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM project", Integer.class ) );

        objectCounts.put( Objects.DATAELEMENT, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM dataelement", Integer.class ) );
        objectCounts.put( Objects.DATAELEMENTGROUP, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM dataelementgroup", Integer.class ) );
        objectCounts.put( Objects.INDICATORTYPE, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM indicatortype", Integer.class ) );
        objectCounts.put( Objects.INDICATOR, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM indicator", Integer.class ) );
        objectCounts.put( Objects.INDICATORGROUP, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM indicatorgroup", Integer.class ) );
        objectCounts.put( Objects.DATASET, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM dataset", Integer.class ) );
//        objectCounts.put( Objects.ORGANISATIONUNIT, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM organisationunit", Integer.class ) );
//        objectCounts.put( Objects.ORGANISATIONUNITGROUP, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM orgunitgroup", Integer.class ) );
//        objectCounts.put( Objects.VALIDATIONRULE, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM validationrule", Integer.class ) );
        objectCounts.put( Objects.PERIOD, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM period", Integer.class ) );
        objectCounts.put( Objects.USER, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM users", Integer.class ) );
        objectCounts.put( Objects.USERGROUP, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM usergroup", Integer.class ) );
//        objectCounts.put( Objects.REPORTTABLE, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM visualization v WHERE v.type = 'PIVOT_TABLE'", Integer.class ) );
//        objectCounts.put( Objects.VISUALIZATION, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM visualization", Integer.class ) );
//        objectCounts.put( Objects.CHART, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM visualization v WHERE v.type <> 'PIVOT_TABLE'", Integer.class ) );
//        objectCounts.put( Objects.MAP, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM map", Integer.class ) );
//        objectCounts.put( Objects.DASHBOARD, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM dashboard", Integer.class ) );
        objectCounts.put( Objects.DATAVALUE, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM datavalue dv where dv.deleted is false", Integer.class ) );
//        objectCounts.put( Objects.PROGRAM, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM program", Integer.class ) );
//        objectCounts.put( Objects.PROGRAMSTAGEINSTANCE, jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM programstageinstance psi where psi.deleted is false", Integer.class ) );

        return objectCounts;
    }
}
