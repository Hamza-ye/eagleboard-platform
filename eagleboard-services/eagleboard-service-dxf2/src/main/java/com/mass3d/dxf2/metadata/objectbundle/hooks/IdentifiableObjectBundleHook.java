package com.mass3d.dxf2.metadata.objectbundle.hooks;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.attribute.Attribute;
import com.mass3d.attribute.AttributeValue;
import java.util.Iterator;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.schema.Schema;
import com.mass3d.security.acl.AclService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Order( 0 )
public class IdentifiableObjectBundleHook extends AbstractObjectBundleHook
{
    private final AclService aclService;

    public IdentifiableObjectBundleHook( AclService aclService )
    {
        checkNotNull( aclService );
        this.aclService = aclService;
    }
    @Override
    public void preCreate( IdentifiableObject identifiableObject, ObjectBundle bundle )
    {
        ( ( BaseIdentifiableObject ) identifiableObject ).setAutoFields();

        BaseIdentifiableObject identifableObject = ( BaseIdentifiableObject ) identifiableObject;
        identifableObject.setAutoFields();
        identifableObject.setLastUpdatedBy( bundle.getUser() );

        Schema schema = schemaService.getDynamicSchema( identifiableObject.getClass() );
        handleAttributeValues( identifiableObject, bundle, schema );
        handleSkipSharing( identifiableObject, bundle );
    }

    @Override
    public void preUpdate( IdentifiableObject object, IdentifiableObject persistedObject, ObjectBundle bundle )
    {
        BaseIdentifiableObject identifiableObject = (BaseIdentifiableObject) object;
        identifiableObject.setAutoFields();
        identifiableObject.setLastUpdatedBy( bundle.getUser() );

        Schema schema = schemaService.getDynamicSchema( object.getClass() );
        handleAttributeValues( object, bundle, schema );
    }

    private void handleAttributeValues( IdentifiableObject identifiableObject, ObjectBundle bundle, Schema schema )
    {
        if ( !schema.havePersistedProperty( "attributeValues" ) ) return;

        Iterator<AttributeValue> iterator = identifiableObject.getAttributeValues().iterator();

        while ( iterator.hasNext() )
        {
            AttributeValue attributeValue = iterator.next();

            // if value null or empty, just skip it
            if ( StringUtils.isEmpty( attributeValue.getValue() ) )
            {
                iterator.remove();
                continue;
            }

            Attribute attribute =  bundle.getPreheat().get( bundle.getPreheatIdentifier(), Attribute.class, attributeValue.getAttribute().getUid() );

            if ( attribute == null )
            {
                iterator.remove();
                continue;
            }

            attributeValue.setAttribute( attribute );
        }
    }

    private void handleSkipSharing( IdentifiableObject identifiableObject, ObjectBundle bundle )
    {
        if ( !bundle.isSkipSharing() ) return;

        aclService.clearSharing( identifiableObject, bundle.getUser() );
    }
}