package com.mass3d.program;

import com.mass3d.hibernate.EnumUserType;

/**
 *
 * @version $ ProgramTypeUserType.java Jul 1, 2015 3:32:02 PM $
 */
public class ProgramTypeUserType
    extends EnumUserType<ProgramType>
{
    public ProgramTypeUserType()
    {
        super( ProgramType.class );
    }
}