package com.mass3d.attribute;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.attribute.AttributeValueDeletionHandler" )
public class AttributeValueDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager identifiableObjectManager;

    private String supportedClassName;

    public AttributeValueDeletionHandler( IdentifiableObjectManager identifiableObjectManager )
    {
        checkNotNull( identifiableObjectManager );

        this.identifiableObjectManager = identifiableObjectManager;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return supportedClassName  + "." +  AttributeValue.class.getSimpleName();
    }

    @Override
    public String allowDeleteAttribute( Attribute attribute )
    {
        for (  Class<? extends IdentifiableObject> supportedClass : attribute.getSupportedClasses() )
        {
            if ( identifiableObjectManager.countAllValuesByAttributes( supportedClass, Lists.newArrayList( attribute ) ) > 0 )
            {
                supportedClassName = supportedClass.getSimpleName();
                return ERROR;
            }
        }

        return null;
    }
}
