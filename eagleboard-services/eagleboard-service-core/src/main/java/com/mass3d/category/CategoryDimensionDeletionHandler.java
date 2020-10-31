package com.mass3d.category;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.category.CategoryDimensionDeletionHandler" )
public class CategoryDimensionDeletionHandler
    extends DeletionHandler
{
    private JdbcTemplate jdbcTemplate;

    public CategoryDimensionDeletionHandler( JdbcTemplate jdbcTemplate )
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
        return CategoryDimension.class.getSimpleName();
    }
    
    @Override
    public String allowDeleteCategoryOption( CategoryOption categoryOption )
    {
        String sql = "select count(*) from categorydimension_items where categoryoptionid = " + categoryOption.getId();
        
        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
    
    @Override
    public String allowDeleteCategory( Category category )
    {
        String sql = "select count(*) from categorydimension where categoryid = " + category.getId();
        
        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
}
