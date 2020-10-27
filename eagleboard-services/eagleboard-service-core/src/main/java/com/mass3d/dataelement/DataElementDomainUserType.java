package com.mass3d.dataelement;

import com.mass3d.hibernate.EnumUserType;

public class DataElementDomainUserType
    extends EnumUserType<DataElementDomain>
{
    public DataElementDomainUserType()
    {
        super( DataElementDomain.class );
    }
}
