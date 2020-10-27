package com.mass3d.webapi.controller.user;

import com.mass3d.schema.descriptors.UserGroupSchemaDescriptor;
import com.mass3d.user.UserGroup;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = UserGroupSchemaDescriptor.API_ENDPOINT )
public class UserGroupController
    extends AbstractCrudController<UserGroup>
{
    @Override
    protected void postUpdateEntity( UserGroup entity )
    {
        hibernateCacheManager.clearCache();
    }
}
