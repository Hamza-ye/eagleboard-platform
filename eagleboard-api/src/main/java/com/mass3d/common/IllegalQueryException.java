package com.mass3d.common;

import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorMessage;

/**
 * Exception representing an illegal query.
 *
 */
public class IllegalQueryException
    extends RuntimeException
{
    private ErrorCode errorCode;

    /**
     * Constructor.
     *
     * @param message the exception message.
     */
    public IllegalQueryException( String message )
    {
        super( message );
    }

    /**
     * Constructor. Sets the message based on the error code message.
     *
     * @param errorCode the {@link ErrorCode}.
     */
    public IllegalQueryException( ErrorCode errorCode )
    {
        super( errorCode.getMessage() );
        this.errorCode = errorCode;
    }

    /**
     * Constructor. Sets the message and error code based on the error message.
     *
     * @param errorMessage the {@link ErrorMessage}.
     */
    public IllegalQueryException( ErrorMessage errorMessage )
    {
        super( errorMessage.getMessage() );
        this.errorCode = errorMessage.getErrorCode();
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
