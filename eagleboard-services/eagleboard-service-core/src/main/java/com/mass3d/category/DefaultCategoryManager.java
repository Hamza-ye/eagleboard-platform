package com.mass3d.category;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.DeleteNotAllowedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service( "com.mass3d.category.CategoryManager" )
public class DefaultCategoryManager
    implements CategoryManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final CategoryService categoryService;

    public DefaultCategoryManager( CategoryService categoryService )
    {
        checkNotNull( categoryService );

        this.categoryService = categoryService;
    }

    // -------------------------------------------------------------------------
    // CategoryOptionCombo
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void addAndPruneOptionCombos( CategoryCombo categoryCombo )
    {
        if ( categoryCombo == null || !categoryCombo.isValid() )
        {
            log.warn( "Category combo is null or invalid, could not update option combos: " + categoryCombo );
            return;
        }

        List<CategoryOptionCombo> generatedOptionCombos = categoryCombo.generateOptionCombosList();
        Set<CategoryOptionCombo> persistedOptionCombos = Sets.newHashSet( categoryCombo.getOptionCombos() );

        boolean modified = false;


        Iterator<CategoryOptionCombo> iterator = persistedOptionCombos.iterator();

        while ( iterator.hasNext() )
        {
            CategoryOptionCombo persistedOptionCombo = iterator.next();

            boolean isDelete = true;

            for ( CategoryOptionCombo optionCombo : generatedOptionCombos )
            {
                if ( optionCombo.equals( persistedOptionCombo ) || persistedOptionCombo.getUid().equals( optionCombo.getUid() ) )
                {
                    isDelete = false;
                    if ( !optionCombo.getName().equals( persistedOptionCombo.getName() ) )
                    {
                        persistedOptionCombo.setName( optionCombo.getName() );
                        modified = true;
                    }
                }
            }

            if ( isDelete )
            {
                try
                {
                    categoryService.deleteCategoryOptionComboNoRollback( persistedOptionCombo );
                }
                catch ( DeleteNotAllowedException ex )
                {
                    log.warn( "Could not delete category option combo: " + persistedOptionCombo );
                    continue;
                }

                iterator.remove();
                categoryCombo.getOptionCombos().remove( persistedOptionCombo );

                log.info( "Deleted obsolete category option combo: " + persistedOptionCombo + " for category combo: " + categoryCombo.getName() );
                modified = true;
            }
        }

        for ( CategoryOptionCombo optionCombo : generatedOptionCombos )
        {
            if ( !persistedOptionCombos.contains( optionCombo ) )
            {
                categoryCombo.getOptionCombos().add( optionCombo );
                categoryService.addCategoryOptionCombo( optionCombo );

                log.info( "Added missing category option combo: " + optionCombo + " for category combo: " + categoryCombo.getName() );
                modified = true;
            }
        }


        if ( modified )
        {
            categoryService.updateCategoryCombo( categoryCombo );
        }
    }

    @Override
    @Transactional
    public void addAndPruneAllOptionCombos()
    {
        List<CategoryCombo> categoryCombos = categoryService.getAllCategoryCombos();

        for ( CategoryCombo categoryCombo : categoryCombos )
        {
            addAndPruneOptionCombos( categoryCombo );
        }
    }
}
