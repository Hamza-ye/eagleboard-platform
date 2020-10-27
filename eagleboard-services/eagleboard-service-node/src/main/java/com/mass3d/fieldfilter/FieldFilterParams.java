package com.mass3d.fieldfilter;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.user.User;

public final class FieldFilterParams
{
    private User user;

    /**
     * List of object(s) to filter through. If more than one, a wrapper
     * is required.
     */
    private List<?> objects = new ArrayList<>();

    /**
     * Fields to filter by.
     */
    private List<String> fields;

    /**
     * Filters out sharing fields if true
     */
    private boolean skipSharing;

    private Defaults defaults = Defaults.INCLUDE;

    public FieldFilterParams( List<?> objects, List<String> fields )
    {
        this.objects = objects;
        this.fields = fields;
    }

    public FieldFilterParams( List<?> objects, List<String> fields, Defaults defaults )
    {
        this.objects = objects;
        this.fields = fields;
        this.defaults = defaults;
    }

    public FieldFilterParams( List<?> objects, List<String> fields, Defaults defaults, boolean skipSharing )
    {
        this.objects = objects;
        this.fields = fields;
        this.defaults = defaults;
        this.skipSharing = skipSharing;
    }

    public User getUser()
    {
        return user;
    }

    public FieldFilterParams setUser( User user )
    {
        this.user = user;
        return this;
    }

    public List<?> getObjects()
    {
        return objects;
    }

    public FieldFilterParams setObjects( List<?> objects )
    {
        this.objects = objects;
        return this;
    }

    public List<String> getFields()
    {
        return fields;
    }

    public FieldFilterParams setFields( List<String> fields )
    {
        this.fields = fields;
        return this;
    }

    public Defaults getDefaults()
    {
        return defaults;
    }

    public FieldFilterParams setDefaults( Defaults defaults )
    {
        this.defaults = defaults;
        return this;
    }

    public boolean getSkipSharing()
    {
        return this.skipSharing;
    }

    public void setSkipSharing( boolean skipSharing )
    {
        this.skipSharing = skipSharing;
    }
}
