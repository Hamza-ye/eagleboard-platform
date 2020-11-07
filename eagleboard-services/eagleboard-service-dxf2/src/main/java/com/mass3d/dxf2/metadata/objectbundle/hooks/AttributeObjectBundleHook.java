package com.mass3d.dxf2.metadata.objectbundle.hooks;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.attribute.Attribute;
import com.mass3d.attribute.AttributeService;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import org.springframework.stereotype.Component;

@Component
public class AttributeObjectBundleHook
    extends AbstractObjectBundleHook
{
    private final AttributeService attributeService;

    public AttributeObjectBundleHook( AttributeService attributeService )
    {
        checkNotNull( attributeService );
        this.attributeService = attributeService;
    }
    @Override
    public <T extends IdentifiableObject> void postUpdate( T persistedObject, ObjectBundle bundle )
    {
        if ( !Attribute.class.isInstance( persistedObject ) )
        {
            return;
        }

        attributeService.invalidateCachedAttribute( persistedObject.getUid() );
    }
}
