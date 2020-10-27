package com.mass3d.deduplication;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface PotentialDuplicateStore
    extends IdentifiableObjectStore<PotentialDuplicate>
{
    int getCountByQuery(PotentialDuplicateQuery query);

    List<PotentialDuplicate> getAllByQuery(PotentialDuplicateQuery query);

    boolean exists(PotentialDuplicate potentialDuplicate);
}
