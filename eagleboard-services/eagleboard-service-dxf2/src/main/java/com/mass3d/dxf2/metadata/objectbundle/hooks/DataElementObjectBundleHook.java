package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.textpattern.TextPatternParser;
import org.springframework.stereotype.Component;

@Component
public class DataElementObjectBundleHook
    extends AbstractObjectBundleHook
{
    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        List<ErrorReport> errors = new ArrayList<>(  );

        if ( object != null && object.getClass().isInstance( DataElement.class ) )
        {
            DataElement dataElement = (DataElement) object;

            if ( dataElement.getFieldMask() != null )
            {
                try
                {
                    TextPatternParser.parse( "\"" + dataElement.getFieldMask() + "\"" );
                }
                catch ( TextPatternParser.TextPatternParsingException ex )
                {
                    errors.add( new ErrorReport( DataElement.class, ErrorCode.E4019, dataElement.getFieldMask(), "Not a valid TextPattern 'TEXT' segment" ) );
                }
            }

        }

        return errors;
    }
}
