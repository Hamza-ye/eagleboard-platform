package com.mass3d.webapi.controller.category;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mass3d.category.CategoryOption;
import com.mass3d.organisationunit.OrganisationUnitQueryParams;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.schema.descriptors.CategoryOptionSchemaDescriptor;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.webapi.controller.AbstractCrudController;
import com.mass3d.webapi.webdomain.WebOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = CategoryOptionSchemaDescriptor.API_ENDPOINT )
public class CategoryOptionController extends AbstractCrudController<CategoryOption>
{
    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Override
    protected void postProcessResponseEntities( List<CategoryOption> entityList, WebOptions options, Map<String, String> parameters )
    {

        if ( !options.isTrue( "restrictToCaptureScope" ) )
        {
            return;
        }

        User user = currentUserService.getCurrentUser();

        if ( user == null )
        {
            return;
        }
        
        OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
        params.setParents( user.getOrganisationUnits() );
        params.setFetchChildren( true );

        Set<String> orgUnits = organisationUnitService.getOrganisationUnitsByQuery( params ).stream().map( orgUnit -> orgUnit.getUid() ).collect(
            Collectors.toSet() );

        for ( CategoryOption catOpt : entityList )
        {
            if ( catOpt.getOrganisationUnits() != null && catOpt.getOrganisationUnits().size() > 0 )
            {
                catOpt.setOrganisationUnits(
                    catOpt.getOrganisationUnits().stream().filter( ou -> orgUnits.contains( ou.getUid() ) ).collect( Collectors.toSet() ) );
            }
        }
    }

}
