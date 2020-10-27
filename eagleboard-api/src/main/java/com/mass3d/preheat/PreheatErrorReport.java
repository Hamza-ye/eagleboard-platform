package com.mass3d.preheat;

import com.mass3d.common.IdentifiableObject;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;

public class PreheatErrorReport extends ErrorReport
{
    private final PreheatIdentifier preheatIdentifier;

    public PreheatErrorReport( PreheatIdentifier preheatIdentifier, Class<?> mainKlass, ErrorCode errorCode, Object... args )
    {
        super( mainKlass, errorCode, args );
        this.preheatIdentifier = preheatIdentifier;
    }

    public PreheatIdentifier getPreheatIdentifier()
    {
        return preheatIdentifier;
    }

    public IdentifiableObject getObjectReference()
    {
        return value != null ? (IdentifiableObject) value : null;
    }
}
