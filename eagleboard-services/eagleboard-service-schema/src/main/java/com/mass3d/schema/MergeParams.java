package com.mass3d.schema;

import java.util.Objects;
import com.mass3d.common.MergeMode;

public final class MergeParams<T>
{
    private final T source;

    private final T target;

    private MergeMode mergeMode = MergeMode.REPLACE;

    private boolean skipSharing;

    private boolean skipTranslation;

    public MergeParams( T source, T target )
    {
        this.source = Objects.requireNonNull( source );
        this.target = Objects.requireNonNull( target );
    }

    public T getSource()
    {
        return source;
    }

    public T getTarget()
    {
        return target;
    }

    public MergeMode getMergeMode()
    {
        return mergeMode;
    }

    public MergeParams<T> setMergeMode( MergeMode mergeMode )
    {
        this.mergeMode = mergeMode;
        return this;
    }

    public boolean isSkipSharing()
    {
        return skipSharing;
    }

    public MergeParams<T> setSkipSharing( boolean skipSharing )
    {
        this.skipSharing = skipSharing;
        return this;
    }

    public boolean isSkipTranslation()
    {
        return skipTranslation;
    }

    public MergeParams<T> setSkipTranslation( boolean skipTranslation )
    {
        this.skipTranslation = skipTranslation;
        return this;
    }
}
