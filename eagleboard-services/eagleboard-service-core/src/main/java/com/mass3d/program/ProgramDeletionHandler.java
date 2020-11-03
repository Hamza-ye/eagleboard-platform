package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mass3d.category.CategoryCombo.DEFAULT_CATEGORY_COMBO_NAME;

import java.util.Collection;
import java.util.List;
import com.mass3d.category.CategoryCombo;
import com.mass3d.category.CategoryService;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityType;
import com.mass3d.user.UserAuthorityGroup;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.program.ProgramDeletionHandler" )
public class ProgramDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final ProgramService programService;

    private final IdentifiableObjectManager idObjectManager;

    private final CategoryService categoryService;

    public ProgramDeletionHandler( ProgramService programService, IdentifiableObjectManager idObjectManager,
        CategoryService categoryService )
    {
        checkNotNull( programService );
        checkNotNull( idObjectManager );
        checkNotNull( categoryService );

        this.programService = programService;
        this.idObjectManager = idObjectManager;
        this.categoryService = categoryService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return Program.class.getSimpleName();
    }

    @Override
    public void deleteCategoryCombo( CategoryCombo categoryCombo )
    {
        CategoryCombo defaultCategoryCombo = categoryService
            .getCategoryComboByName( DEFAULT_CATEGORY_COMBO_NAME );

        Collection<Program> programs = idObjectManager.getAllNoAcl( Program.class );

        for ( Program program : programs )
        {            
            if ( program != null && categoryCombo.equals( program.getCategoryCombo() ) )
            {
                program.setCategoryCombo( defaultCategoryCombo );
                idObjectManager.updateNoAcl( program );
            }
        }        
    }

    @Override
    public void deleteOrganisationUnit( OrganisationUnit unit )
    {
        Collection<Program> programs = idObjectManager.getAllNoAcl( Program.class );

        for ( Program program : programs )
        {
            if ( program.getOrganisationUnits().remove( unit ) )
            {
                idObjectManager.updateNoAcl( program );
            }
        }
    }

    @Override
    public void deleteUserAuthorityGroup( UserAuthorityGroup group )
    {
        Collection<Program> programs = idObjectManager.getAllNoAcl( Program.class );

        for ( Program program : programs )
        {
            if ( program.getUserRoles().remove( group ) )
            {
                idObjectManager.updateNoAcl( program );
            }
        }
    }

    @Override
    public String allowDeleteTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        Collection<Program> programs = programService.getProgramsByTrackedEntityType( trackedEntityType );

        return (programs != null && programs.size() > 0) ? ERROR : null;
    }

    @Override
    public void deleteTrackedEntityAttribute( TrackedEntityAttribute trackedEntityAttribute )
    {
        Collection<Program> programs = idObjectManager.getAllNoAcl( Program.class );

        for ( Program program : programs )
        {
            for ( ProgramTrackedEntityAttribute programAttribute : program.getProgramAttributes() )
            {
                if ( programAttribute.getAttribute().equals( trackedEntityAttribute ) )
                {
                    program.getProgramAttributes().remove( programAttribute );
                    idObjectManager.updateNoAcl( program );
                }
            }
        }
    }

    @Override
    public void deleteDataEntryForm( DataEntryForm dataEntryForm )
    {
        List<Program> associatedPrograms = programService.getProgramsByDataEntryForm( dataEntryForm );

        for ( Program program : associatedPrograms )
        {
            program.setDataEntryForm( null );
            idObjectManager.updateNoAcl( program );
        }
    }
}
