package com.mass3d.dxf2.metadata.objectbundle.hooks;

import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.period.PeriodService;
import com.mass3d.period.PeriodType;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;
import com.mass3d.system.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PeriodTypeObjectBundleHook extends AbstractObjectBundleHook
{
    @Autowired
    private PeriodService periodService;

    @Override
    public <T extends IdentifiableObject> void preCreate( T object, ObjectBundle bundle )
    {
        Schema schema = schemaService.getDynamicSchema( object.getClass() );

        for ( Property property : schema.getPropertyMap().values() )
        {
            if ( PeriodType.class.isAssignableFrom( property.getKlass() ) )
            {
                PeriodType periodType = ReflectionUtils.invokeMethod( object, property.getGetterMethod() );

                if ( periodType != null )
                {
                    periodType = bundle.getPreheat().getPeriodTypeMap().get( periodType.getName() );
                    periodType = periodService.reloadPeriodType( periodType );
                    ReflectionUtils.invokeMethod( object, property.getSetterMethod(), periodType );
                }
            }
        }
    }

    @Override
    public <T extends IdentifiableObject> void preUpdate( T object, T persistedObject, ObjectBundle bundle )
    {
        Schema schema = schemaService.getDynamicSchema( object.getClass() );

        for ( Property property : schema.getPropertyMap().values() )
        {
            if ( PeriodType.class.isAssignableFrom( property.getKlass() ) )
            {
                PeriodType periodType = ReflectionUtils.invokeMethod( object, property.getGetterMethod() );

                if ( periodType != null )
                {
                    periodType = bundle.getPreheat().getPeriodTypeMap().get( periodType.getName() );
                    ReflectionUtils.invokeMethod( object, property.getSetterMethod(), periodType );
                }
            }
        }
    }
}