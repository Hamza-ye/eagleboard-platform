package com.mass3d.render;

import com.google.common.collect.ImmutableSet;
import com.mass3d.program.ProgramStageDataElement;
import com.mass3d.program.ProgramTrackedEntityAttribute;
import java.util.Set;
import com.mass3d.common.ValueType;
import com.mass3d.render.type.ValueTypeRenderingType;

/**
 * This class represents the constraint rules enforced by the application on DataElement, TrackedEntityAttribute, ValueType,
 * OptionSet and RenderTypes.
 */
public class StaticRenderingConfiguration
{
    public static final Set<ObjectValueTypeRenderingOption> RENDERING_OPTIONS_MAPPING = ImmutableSet.<ObjectValueTypeRenderingOption>builder()

        // Boolean
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.TRUE_ONLY, false,
            ValueTypeRenderingType.BOOLEAN_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.BOOLEAN, false,
            ValueTypeRenderingType.BOOLEAN_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.TRUE_ONLY, false,
            ValueTypeRenderingType.BOOLEAN_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.BOOLEAN, false,
            ValueTypeRenderingType.BOOLEAN_TYPES ) )

        // OptionSet
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, null, true,
            ValueTypeRenderingType.OPTION_SET_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, null, true,
            ValueTypeRenderingType.OPTION_SET_TYPES ) )

        // Numeric
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.INTEGER, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.INTEGER_POSITIVE, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.INTEGER_NEGATIVE, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.INTEGER_ZERO_OR_POSITIVE, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.NUMBER, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.UNIT_INTERVAL, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.PERCENTAGE, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.INTEGER, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.INTEGER_POSITIVE, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.INTEGER_NEGATIVE, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.INTEGER_ZERO_OR_POSITIVE, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.NUMBER, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.UNIT_INTERVAL, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.PERCENTAGE, false,
            ValueTypeRenderingType.NUMERIC_TYPES ) )

        .add( new ObjectValueTypeRenderingOption( ProgramStageDataElement.class, ValueType.TEXT, false,
            ValueTypeRenderingType.TEXT_TYPES ) )
        .add( new ObjectValueTypeRenderingOption( ProgramTrackedEntityAttribute.class, ValueType.TEXT, false,
            ValueTypeRenderingType.TEXT_TYPES ) )

        .build();

}
