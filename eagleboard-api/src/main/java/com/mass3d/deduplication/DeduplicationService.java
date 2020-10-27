package com.mass3d.deduplication;

import java.util.List;

public interface DeduplicationService
{
        long addPotentialDuplicate(PotentialDuplicate potentialDuplicate);

        PotentialDuplicate getPotentialDuplicateById(long id);

        PotentialDuplicate getPotentialDuplicateByUid(String uid);

        List<PotentialDuplicate> getAllPotentialDuplicates();

        void markPotentialDuplicateInvalid(PotentialDuplicate potentialDuplicate);

        int countPotentialDuplicates(PotentialDuplicateQuery query);

        boolean exists(PotentialDuplicate potentialDuplicate);

        List<PotentialDuplicate> getAllPotentialDuplicates(PotentialDuplicateQuery query);

        void deletePotentialDuplicate(PotentialDuplicate potentialDuplicate);
}
