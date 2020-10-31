package com.mass3d.dataset;

public enum FormType
{
    DEFAULT,
    CUSTOM,
    SECTION,
    SECTION_MULTIORG;

    public boolean isDefault()
    {
        return this == DEFAULT;
    }

    public boolean isCustom()
    {
        return this == CUSTOM;
    }

    public boolean isSection()
    {
        return this == SECTION || this == SECTION_MULTIORG;
    }
}
