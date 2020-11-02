package com.mass3d.common;

import com.mass3d.translation.TranslationProperty;

public interface NameableObject
    extends IdentifiableObject
{
    String[] I18N_PROPERTIES = { TranslationProperty.NAME.getName(), TranslationProperty.SHORT_NAME.getName(), TranslationProperty
        .DESCRIPTION.getName() };

    String getShortName();

    String getDisplayShortName();

    String getDescription();

    String getDisplayDescription();

    String getDisplayProperty(DisplayProperty property);
}