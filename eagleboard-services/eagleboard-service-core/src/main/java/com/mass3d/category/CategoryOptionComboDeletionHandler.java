package com.mass3d.category;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @version $Id$
 */
@Component( "com.mass3d.category.CategoryOptionComboDeletionHandler" )
public class CategoryOptionComboDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final CategoryService categoryService;

    private final JdbcTemplate jdbcTemplate;

    public CategoryOptionComboDeletionHandler( CategoryService categoryService, JdbcTemplate jdbcTemplate )
    {
        checkNotNull( categoryService );
        checkNotNull( jdbcTemplate );

        this.categoryService = categoryService;
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    //TODO expressionoptioncombo

    @Override
    public String getClassName()
    {
        return CategoryOptionCombo.class.getSimpleName();
    }

    @Override
    public String allowDeleteCategoryOption( CategoryOption categoryOption )
    {
        final String dvSql =
            "select count(*) from datavalue dv " +
            "where dv.categoryoptioncomboid in ( " +
                "select cc.categoryoptioncomboid from categoryoptioncombos_categoryoptions cc " +
                "where cc.categoryoptionid = " + categoryOption.getId() + " ) " +
            "or dv.attributeoptioncomboid in ( " +
                "select cc.categoryoptioncomboid from categoryoptioncombos_categoryoptions cc " +
                "where cc.categoryoptionid = " + categoryOption.getId() + " );";

        if ( jdbcTemplate.queryForObject( dvSql, Integer.class ) > 0 )
        {
            return ERROR;
        }

        final String crSql =
            "select count(*) from completedatasetregistration cdr " +
            "where cdr.attributeoptioncomboid in ( " +
                "select cc.categoryoptioncomboid from categoryoptioncombos_categoryoptions cc " +
                "where cc.categoryoptionid = " + categoryOption.getId() + " );";

        if ( jdbcTemplate.queryForObject( crSql, Integer.class ) > 0 )
        {
            return ERROR;
        }

        return null;
    }

    @Override
    public String allowDeleteCategoryCombo( CategoryCombo categoryCombo )
    {
        final String dvSql =
            "select count(*) from datavalue dv " +
            "where dv.categoryoptioncomboid in ( " +
                "select co.categoryoptioncomboid from categorycombos_optioncombos co " +
                "where co.categorycomboid=" + categoryCombo.getId() + " ) " +
            "or dv.attributeoptioncomboid in ( " +
                "select co.categoryoptioncomboid from categorycombos_optioncombos co " +
                "where co.categorycomboid=" + categoryCombo.getId() + " );";

        if ( jdbcTemplate.queryForObject( dvSql, Integer.class ) > 0 )
        {
            return ERROR;
        }

        final String crSql =
            "select count(*) from completedatasetregistration cdr " +
            "where cdr.attributeoptioncomboid in ( " +
                "select co.categoryoptioncomboid from categorycombos_optioncombos co " +
                "where co.categorycomboid=" + categoryCombo.getId() + " );";

        if ( jdbcTemplate.queryForObject( crSql, Integer.class ) > 0 )
        {
            return ERROR;
        }

        return null;
    }

    @Override
    public void deleteCategoryOption( CategoryOption categoryOption )
    {
        Iterator<CategoryOptionCombo> iterator = categoryOption.getCategoryOptionCombos().iterator();

        while ( iterator.hasNext() )
        {
            CategoryOptionCombo optionCombo = iterator.next();
            iterator.remove();
            categoryService.deleteCategoryOptionCombo( optionCombo );
        }
    }

    @Override
    public void deleteCategoryCombo( CategoryCombo categoryCombo )
    {
        Iterator<CategoryOptionCombo> iterator = categoryCombo.getOptionCombos().iterator();

        while ( iterator.hasNext() )
        {
            CategoryOptionCombo optionCombo = iterator.next();
            iterator.remove();
            categoryService.deleteCategoryOptionCombo( optionCombo );
        }
    }
}
