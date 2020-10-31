package com.mass3d.dataelement;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.dataelement.DataElementOperandDeletionHandler" )
public class DataElementOperandDeletionHandler
    extends DeletionHandler
{
    private final JdbcTemplate jdbcTemplate;

    public DataElementOperandDeletionHandler( JdbcTemplate jdbcTemplate )
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
        return DataElementOperand.class.getSimpleName();
    }

    //TODO masking real problem, we should control operands better and check associated objects regarding deletion
    
    @Override
    public String allowDeleteCategoryOptionCombo( CategoryOptionCombo optionCombo )
    {
        String sql = "select count(*) from dataelementoperand where categoryoptioncomboid=" + optionCombo.getId();

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
}
