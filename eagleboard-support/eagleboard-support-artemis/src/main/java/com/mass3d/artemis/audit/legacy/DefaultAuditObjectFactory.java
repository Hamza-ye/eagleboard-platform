package com.mass3d.artemis.audit.legacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.audit.AuditAttribute;
import com.mass3d.audit.AuditAttributes;
import com.mass3d.audit.AuditScope;
import com.mass3d.audit.AuditType;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.commons.util.DebugUtils;
import com.mass3d.system.util.AnnotationUtils;
import com.mass3d.system.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * A factory for constructing {@see com.mass3d.audit.Audit} data payloads.
 * This can be the object itself (as is the case for metadata), or it can be a
 * wrapper object collecting the parts wanted.
 *
 */
@Slf4j
@Component
public class DefaultAuditObjectFactory implements AuditObjectFactory
{
    private final ObjectMapper objectMapper;

    /**
     * Cache for Fields of {@link com.mass3d.audit.Auditable} classes Key is
     * class name. Value is Map of {@link AuditAttribute} Fields and its getter
     * Method
     */
    private final Map<String, Map<Field, Method>> cachedAuditAttributeFields = new ConcurrentHashMap<>();

    public DefaultAuditObjectFactory( @Qualifier("jsonMapper") ObjectMapper objectMapper )
    {
        this.objectMapper = objectMapper;

        // TODO consider moving this to CommonsConfig
        objectMapper.registerModule( new Hibernate5Module() );
    }

    @Override
    public Object create( AuditScope auditScope, AuditType auditType, Object object, String user )
    {
        switch ( auditScope )
        {
        case METADATA:
            return handleMetadataAudit( object );
        case TRACKER:
            return handleTracker( object );
        case AGGREGATE:
            return handleAggregate( object );
        }
        return null;
    }

    @Override
    public AuditAttributes collectAuditAttributes( Object auditObject )
    {
        AuditAttributes auditAttributes = new AuditAttributes();

        getAuditAttributeFields( auditObject.getClass() ).forEach( ( key, value ) -> {

            Object attributeObject = ReflectionUtils.invokeMethod( auditObject, value );

            if ( attributeObject instanceof IdentifiableObject )
            {
                auditAttributes.put( key.getName(), ((IdentifiableObject) attributeObject).getUid() );
            }
            else
            {
                auditAttributes.put( key.getName(), attributeObject );
            }
        } );

        return auditAttributes;
    }

    private Map<Field, Method> getAuditAttributeFields( Class<?> auditClass )
    {
        Map<Field, Method> map = cachedAuditAttributeFields.get( auditClass.getName() );

        if ( map == null )
        {
            map = AnnotationUtils.getAnnotatedFields( auditClass, AuditAttribute.class );
            cachedAuditAttributeFields.put( auditClass.getName(), map );
        }

        return map;
    }

    private String handleTracker( Object object )
    {
        return toJson( object );
    }

    private String handleAggregate( Object object )
    {
        return toJson( object );
    }

    private String handleMetadataAudit( Object object )
    {
        return toJson( object );
    }

    private String toJson( Object object )
    {
        try
        {
            return objectMapper.writeValueAsString( object );
        }
        catch ( JsonProcessingException e )
        {
            log.error( DebugUtils.getStackTrace( e ) );
        }

        return null;
    }
}