package com.mass3d.render.type;

import com.google.common.collect.ImmutableSet;

/**
 * This class represents all the different ways ValueTypes can be rendered. constrains is defined in
 * StaticRenderingConfiguration.java and is enforced in DataElementObjectBundleHook and
 * TrackedEntityAttributeObjectBundleHook
 */
public enum ValueTypeRenderingType
{
    DEFAULT,
    DROPDOWN,
    VERTICAL_RADIOBUTTONS,
    HORIZONTAL_RADIOBUTTONS,
    VERTICAL_CHECKBOXES,
    HORIZONTAL_CHECKBOXES,
    SHARED_HEADER_RADIOBUTTONS,
    ICONS_AS_BUTTONS,
    SPINNER,
    ICON,
    TOGGLE,
    VALUE,
    SLIDER,
    LINEAR_SCALE,
    AUTOCOMPLETE,
    QR_CODE,
    BAR_CODE;

    /**
     * RenderingTypes supported by OptionSet ValueTypes
     */
    public static final ImmutableSet<ValueTypeRenderingType> OPTION_SET_TYPES = ImmutableSet
        .of( DEFAULT, DROPDOWN, VERTICAL_RADIOBUTTONS, HORIZONTAL_RADIOBUTTONS, VERTICAL_CHECKBOXES,
            HORIZONTAL_CHECKBOXES, SHARED_HEADER_RADIOBUTTONS, ICONS_AS_BUTTONS, SPINNER, ICON );

    /**
     * RenderingTypes supported by boolean ValueTypes
     */
    public static final ImmutableSet<ValueTypeRenderingType> BOOLEAN_TYPES = ImmutableSet
        .of( DEFAULT, VERTICAL_RADIOBUTTONS, HORIZONTAL_RADIOBUTTONS, VERTICAL_CHECKBOXES,
            HORIZONTAL_CHECKBOXES, TOGGLE );

    /**
     * RenderingTypes supported by numerical ValueTypes
     */
    public static final ImmutableSet<ValueTypeRenderingType> NUMERIC_TYPES = ImmutableSet
        .of( DEFAULT, VALUE, SLIDER, LINEAR_SCALE, SPINNER );

    /**
     * RenderingTypes supported by textual valueTypes
     */
    public static final ImmutableSet<ValueTypeRenderingType> TEXT_TYPES = ImmutableSet
        .of( DEFAULT, VALUE, AUTOCOMPLETE, QR_CODE, BAR_CODE );
}
