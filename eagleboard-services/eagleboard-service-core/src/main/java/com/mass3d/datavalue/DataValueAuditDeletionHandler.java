package com.mass3d.datavalue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.dataelement.DataElement;
import com.mass3d.period.Period;
import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.todotask.TodoTask;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.datavalue.DataValueAuditDeletionHandler" )
public class DataValueAuditDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final JdbcTemplate jdbcTemplate;

    public DataValueAuditDeletionHandler( JdbcTemplate jdbcTemplate )
    {
        checkNotNull( jdbcTemplate );
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return DataValueAudit.class.getSimpleName();
    }
    
    @Override
    public String allowDeleteDataElement( DataElement dataElement )
    {
        String sql = "SELECT COUNT(*) FROM datavalueaudit where dataelementid=" + dataElement.getId();
        
        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
    
    @Override
    public String allowDeletePeriod( Period period )
    {
        String sql = "SELECT COUNT(*) FROM datavalueaudit where periodid=" + period.getId();
        
        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
    
    @Override
    public String allowDeleteTodoTask( TodoTask unit )
    {
        String sql = "SELECT COUNT(*) FROM datavalueaudit where organisationunitid=" + unit.getId();
        
        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
    
//    @Override
//    public String allowDeleteCategoryOptionCombo( CategoryOptionCombo optionCombo )
//    {
//        String sql = "SELECT COUNT(*) FROM datavalueaudit where categoryoptioncomboid=" + optionCombo.getId() + " or attributeoptioncomboid=" + optionCombo.getId();
//
//        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
//    }
}
