package com.mass3d.dataelement;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.category.Category;
import com.mass3d.category.CategoryCombo;
import com.mass3d.category.CategoryService;
import com.mass3d.system.startup.TransactionContextStartupRoutine;

/**
 * When storing DataValues without associated dimensions there is a need to
 * refer to a default dimension. This populator persists a
 * CategoryCombo named by the
 * CategoryCombo.DEFAULT_CATEGORY_COMBO_NAME property and a
 * corresponding DataElementCatoryOptionCombo which should be used for this
 * purpose.
 *
 * @version $Id$
 */
@Slf4j
public class DataElementDefaultDimensionPopulator
    extends TransactionContextStartupRoutine
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final DataElementService dataElementService;
    
    private final CategoryService categoryService;

    public DataElementDefaultDimensionPopulator( DataElementService dataElementService,
        CategoryService categoryService )
    {
        checkNotNull( dataElementService );
        checkNotNull( categoryService );
        this.dataElementService = dataElementService;
        this.categoryService = categoryService;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    @Override
    public void executeInTransaction()
    {
        Category defaultCategory = categoryService.getCategoryByName( Category.DEFAULT_NAME );

        if ( defaultCategory == null )
        {
            categoryService.generateDefaultDimension();

            defaultCategory = categoryService.getCategoryByName( Category.DEFAULT_NAME );

            log.info( "Added default category" );
        }

        categoryService.updateCategory( defaultCategory );

        String defaultName = CategoryCombo.DEFAULT_CATEGORY_COMBO_NAME;

        CategoryCombo categoryCombo = categoryService.getCategoryComboByName( defaultName );

        if ( categoryCombo == null )
        {
            categoryService.generateDefaultDimension();

            log.info( "Added default dataelement dimension" );

            categoryCombo = categoryService.getCategoryComboByName( defaultName );
        }

        // ---------------------------------------------------------------------
        // Any data elements without dimensions need to be associated at least
        // with the default dimension
        // ---------------------------------------------------------------------

        Collection<DataElement> dataElements = dataElementService.getAllDataElements();

        for ( DataElement dataElement : dataElements )
        {
            if ( dataElement.getCategoryCombo() == null )
            {
                dataElement.setCategoryCombo( categoryCombo );

                dataElementService.updateDataElement( dataElement );
            }
        }
    }
}
