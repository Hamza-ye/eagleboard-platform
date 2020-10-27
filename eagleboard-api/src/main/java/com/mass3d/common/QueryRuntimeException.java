package com.mass3d.common;

import com.mass3d.feedback.ErrorCode;

public class QueryRuntimeException
    extends RuntimeException
{
    private ErrorCode errorCode;

    /**
     * Constructor. Sets the message based on the error code message.
     *
     * @param errorCode the {@link ErrorCode}.
     */
    public QueryRuntimeException( ErrorCode errorCode, Throwable cause )
    {
        super( errorCode.getMessage(), cause );
        this.errorCode = errorCode;
    }

    public QueryRuntimeException( String message, Throwable throwable )
    {
        super( message, throwable );
    }

    /**
     * Returns the {@link ErrorCode} of the exception.
     *
     * @return the {@link ErrorCode} of the exception.
     */
    public ErrorCode getErrorCode()
    {
        return errorCode;
    }
}
