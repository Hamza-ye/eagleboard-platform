package com.mass3d.user;

import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.dataset.DataSet;

public interface UserAuthorityGroupStore
    extends IdentifiableObjectStore<UserAuthorityGroup>
{
    /**
     * Returns the number of UserAuthorityGroups which are associated with the
     * given DataSet.
     *  
     * @param dataSet the DataSet.
     * @return number of UserAuthorityGroups.
     */
    int countDataSetUserAuthorityGroups(DataSet dataSet);
}
