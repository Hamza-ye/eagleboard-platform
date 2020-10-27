package com.mass3d.webapi.controller.option;

import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.node.types.RootNode;
import com.mass3d.option.OptionService;
import com.mass3d.option.OptionSet;
import com.mass3d.schema.descriptors.OptionSetSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import com.mass3d.webapi.controller.metadata.MetadataExportControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping( value = OptionSetSchemaDescriptor.API_ENDPOINT )
public class OptionSetController
    extends AbstractCrudController<OptionSet>
{
    @Autowired
    private OptionService optionService;

    @RequestMapping( value = "/{uid}/metadata", method = RequestMethod.GET )
    public ResponseEntity<RootNode> getOptionSetWithDependencies( @PathVariable( "uid" ) String pvUid, HttpServletResponse response, @RequestParam( required = false, defaultValue = "false" ) boolean download ) throws WebMessageException
    {
        OptionSet optionSet = optionService.getOptionSet( pvUid );

        if ( optionSet == null )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "OptionSet not found for uid: " + pvUid ) );
        }

        return MetadataExportControllerUtils.getWithDependencies( contextService, exportService, optionSet, download );
    }
}