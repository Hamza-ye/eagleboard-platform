package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.program.ProgramTrackedEntityAttribute;
import com.mass3d.render.DeviceRenderTypeMap;
import com.mass3d.render.RenderDevice;
import com.mass3d.render.type.ValueTypeRenderingObject;
import com.mass3d.system.util.ValidationUtils;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import org.springframework.stereotype.Component;

@Component
public class ProgramTrackedEntityAttributeObjectBundleHook
    extends AbstractObjectBundleHook
{

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        /*
         * Validate that the RenderType (if any) conforms to the constraints of ValueType or OptionSet.
         */
        if ( object != null && object.getClass().isAssignableFrom( ProgramTrackedEntityAttribute.class ) )
        {
            ProgramTrackedEntityAttribute ptea = (ProgramTrackedEntityAttribute) object;

            errorReports.addAll( renderTypeConformsToConstrains( ptea ) );

        }

        return errorReports;
    }

    private List<ErrorReport> renderTypeConformsToConstrains( ProgramTrackedEntityAttribute ptea )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        DeviceRenderTypeMap<ValueTypeRenderingObject> map = ptea.getRenderType();

        TrackedEntityAttribute attr = ptea.getAttribute();

        if ( map == null )
        {
            return errorReports;
        }

        for ( RenderDevice device : map.keySet() )
        {
            if ( map.get( device ).getType() == null )
            {
                errorReports
                    .add( new ErrorReport( ProgramTrackedEntityAttribute.class, ErrorCode.E4011, "renderType.type" ) );
            }

            if ( !ValidationUtils
                .validateRenderingType( ProgramTrackedEntityAttribute.class, attr.getValueType(), attr.hasOptionSet(),
                    map.get( device ).getType() ) )
            {
                errorReports.add( new ErrorReport( ProgramTrackedEntityAttribute.class, ErrorCode.E4020,
                    map.get( device ).getType(), attr.getValueType() ) );
            }

        }

        return errorReports;
    }

}
