package com.mass3d.schema;

import java.util.Collection;
import java.util.stream.Collectors;
import com.mass3d.common.MergeMode;
import com.mass3d.system.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.schema.MergeService" )
@Transactional
public class DefaultMergeService implements MergeService
{
    private final SchemaService schemaService;

    public DefaultMergeService( SchemaService schemaService )
    {
        this.schemaService = schemaService;
    }

    @Override
    public <T> T merge( MergeParams<T> mergeParams )
    {
        T source = mergeParams.getSource();
        T target = mergeParams.getTarget();

        Schema schema = schemaService.getDynamicSchema( source.getClass() );

        for ( Property property : schema.getProperties() )
        {
            if ( schema.isIdentifiableObject() )
            {
                if ( mergeParams.isSkipSharing() && ReflectionUtils.isSharingProperty( property ) )
                {
                    continue;
                }

                if ( mergeParams.isSkipTranslation() && ReflectionUtils.isTranslationProperty( property ) )
                {
                    continue;
                }
            }

            // Passwords should only be merged manually
            if ( property.is( PropertyType.PASSWORD ) )
            {
                continue;
            }

            if ( property.isCollection() )
            {
                Collection<T> sourceObject = ReflectionUtils.invokeMethod( source, property.getGetterMethod() );
                Collection<T> targetObject = ReflectionUtils.invokeMethod( target, property.getGetterMethod() );

                if ( sourceObject == null )
                {
                    continue;
                }

                if ( targetObject == null )
                {
                    targetObject = ReflectionUtils.newCollectionInstance( property.getKlass() );
                }

                if ( mergeParams.getMergeMode().isMerge() )
                {
                    Collection<T> merged = ReflectionUtils.newCollectionInstance( property.getKlass() );
                    merged.addAll( targetObject );
                    merged.addAll( sourceObject.stream().filter( o -> !merged.contains( o ) ).collect( Collectors
                        .toList() ) );

                    targetObject.clear();
                    targetObject.addAll( merged );
                }
                else
                {
                    targetObject.clear();
                    targetObject.addAll( sourceObject );
                }

                ReflectionUtils.invokeMethod( target, property.getSetterMethod(), targetObject );
            }
            else
            {
                Object sourceObject = ReflectionUtils.invokeMethod( source, property.getGetterMethod() );

                if ( mergeParams.getMergeMode().isReplace() || ( mergeParams.getMergeMode().isMerge() && sourceObject != null ) )
                {
                    ReflectionUtils.invokeMethod( target, property.getSetterMethod(), sourceObject );
                }
            }
        }

        return target;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T> T clone( T source )
    {
        if ( source == null ) return null;

        try
        {
            return merge( new MergeParams<>( source, (T) source.getClass().newInstance() )
                .setMergeMode( MergeMode.REPLACE ) );
        }
        catch ( InstantiationException | IllegalAccessException ignored )
        {
        }

        return null;
    }
}