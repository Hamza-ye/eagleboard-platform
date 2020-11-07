package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.Objects;
import com.mass3d.common.ValueType;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.textpattern.TextPattern;
import com.mass3d.textpattern.TextPatternParser;
import com.mass3d.textpattern.TextPatternValidationUtils;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.dxf2.metadata.objectbundle.hooks.TrackedEntityAttributeObjectBundleHook" )
public class TrackedEntityAttributeObjectBundleHook
    extends AbstractObjectBundleHook
{
    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        // Validate that the RenderType (if any) conforms to the constraints of ValueType or OptionSet.

        if ( object != null && object.getClass().isAssignableFrom( TrackedEntityAttribute.class ) )
        {
            TrackedEntityAttribute attr = (TrackedEntityAttribute) object;

            if ( attr.isGenerated() && !attr.getValueType().equals( ValueType.TEXT ) )
            {
                errorReports.add( new ErrorReport( TrackedEntityAttribute.class, ErrorCode.E4010, "generated", attr.getValueType() ) );
            }

            errorReports.addAll( textPatternValid( attr ) );

            if ( attr.getFieldMask() != null )
            {
                try
                {
                    TextPatternParser.parse( "\"" + attr.getFieldMask() + "\"" );
                }
                catch ( TextPatternParser.TextPatternParsingException e )
                {
                    errorReports.add( new ErrorReport( TrackedEntityAttribute.class, ErrorCode.E4019, attr.getFieldMask(), "Not a valid TextPattern 'TEXT' segment." ));
                }
            }

        }

        return errorReports;
    }

    @Override
    public <T extends IdentifiableObject> void postCreate( T persistedObject, ObjectBundle bundle )
    {
        if ( persistedObject != null && persistedObject.getClass().isAssignableFrom( TrackedEntityAttribute.class ) )
        {
            updateTextPattern( (TrackedEntityAttribute) persistedObject );
        }
    }

    @Override
    public <T extends IdentifiableObject> void postUpdate( T persistedObject, ObjectBundle bundle )
    {
        if ( persistedObject != null && persistedObject.getClass().isAssignableFrom( TrackedEntityAttribute.class ) )
        {
            updateTextPattern( (TrackedEntityAttribute) persistedObject );
        }
    }

    private void updateTextPattern( TrackedEntityAttribute attr )
    {
        if ( attr.isGenerated() )
        {
            try
            {
                TextPattern textPattern = TextPatternParser.parse( attr.getPattern() );
                textPattern.setOwnerObject( Objects.TRACKEDENTITYATTRIBUTE );
                textPattern.setOwnerUid( attr.getUid() );
                attr.setTextPattern( textPattern );
            }
            catch ( TextPatternParser.TextPatternParsingException e )
            {
                e.printStackTrace();
            }
        }
    }

    private List<ErrorReport> textPatternValid( TrackedEntityAttribute attr )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( attr.isGenerated() )
        {
            try
            {
                TextPattern tp = TextPatternParser.parse( attr.getPattern() );

                long generatedSegments = tp.getSegments().stream().filter( ( s ) -> s.getMethod().isGenerated() )
                    .count();

                if ( generatedSegments != 1 )
                {
                    errorReports.add( new ErrorReport( TrackedEntityAttribute.class, ErrorCode.E4021 ) );
                }

                if ( !TextPatternValidationUtils.validateValueType( tp, attr.getValueType() ) )
                {
                    errorReports.add( new ErrorReport( TrackedEntityAttribute.class, ErrorCode.E4022, attr.getPattern(),
                        attr.getValueType().name() ) );
                }
            }
            catch ( TextPatternParser.TextPatternParsingException e )
            {
                errorReports.add( new ErrorReport( TrackedEntityAttribute.class, ErrorCode.E4019, attr.getPattern(),
                    e.getMessage() ) );
            }

        }

        return errorReports;
    }
}
