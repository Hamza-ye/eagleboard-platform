package com.mass3d.trackedentity;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.trackedentity.TrackedEntityInstanceDeletionHandler" )
public class TrackedEntityInstanceDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private final JdbcTemplate jdbcTemplate;

    public TrackedEntityInstanceDeletionHandler( JdbcTemplate jdbcTemplate )
    {
        checkNotNull( jdbcTemplate );
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    protected String getClassName()
    {
        return TrackedEntityInstance.class.getSimpleName();
    }

    @Override
    public String allowDeleteOrganisationUnit( OrganisationUnit unit )
    {
        String sql = "select count(*) from trackedentityinstance where organisationunitid = " + unit.getId();

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;    }
    
    @Override
    public String allowDeleteTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        String sql = "select count(*) from trackedentityinstance where trackedentitytypeid = " + trackedEntityType.getId();

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
}
