package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.program.ProgramStageDataElement;
import com.mass3d.render.DeviceRenderTypeMap;
import com.mass3d.render.RenderDevice;
import com.mass3d.render.type.ValueTypeRenderingObject;
import com.mass3d.system.util.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class ProgramStageDataElementObjectBundleHook
    extends AbstractObjectBundleHook
{

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        /*
         * Validate that the RenderType (if any) conforms to the constraints of ValueType or OptionSet.
         */
        if ( object != null && object.getClass().isAssignableFrom( ProgramStageDataElement.class ) )
        {
            ProgramStageDataElement psda = (ProgramStageDataElement) object;
            DeviceRenderTypeMap<ValueTypeRenderingObject> map = psda.getRenderType();

            if ( map == null )
            {
                return errorReports;
            }
            for ( RenderDevice device : map.keySet() )
            {

                DataElement de = psda.getDataElement();

                if ( map.get( device ).getType() == null )
                {
                    errorReports
                        .add( new ErrorReport( ProgramStageDataElement.class, ErrorCode.E4011, "renderType.type" ) );
                }

                if ( !ValidationUtils
                    .validateRenderingType( ProgramStageDataElement.class, de.getValueType(), de.hasOptionSet(),
                        map.get( device ).getType() ) )
                {
                    errorReports.add( new ErrorReport( ProgramStageDataElement.class, ErrorCode.E4017,
                        map.get( device ).getType(), de.getValueType() ) );
                }

            }
        }

        return errorReports;

    }

}
