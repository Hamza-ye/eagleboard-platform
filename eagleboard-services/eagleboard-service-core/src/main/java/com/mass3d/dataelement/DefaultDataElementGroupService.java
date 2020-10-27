package com.mass3d.dataelement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service( "org.hisp.dhis.dataelement.DataElementGroupService" )
public class DefaultDataElementGroupService
    implements DataElementGroupService
{
    @Autowired
    private DataElementGroupStore dataElementGroupStore;

    @Override
    public DataElementGroup getDataElementGroupByUid( String uid )
    {
        return dataElementGroupStore.getByUid( uid );
    }
}
