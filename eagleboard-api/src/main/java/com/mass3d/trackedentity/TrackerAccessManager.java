package com.mass3d.trackedentity;

import java.util.List;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.dataelement.DataElement;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.relationship.Relationship;
import com.mass3d.user.User;

public interface TrackerAccessManager
{
    List<String> canRead(User user, TrackedEntityInstance trackedEntityInstance);

    List<String> canWrite(User user, TrackedEntityInstance trackedEntityInstance);

    List<String> canRead(User user, TrackedEntityInstance trackedEntityInstance, Program program,
        boolean skipOwnershipCheck);

    List<String> canWrite(User user, TrackedEntityInstance trackedEntityInstance, Program program,
        boolean skipOwnershipCheck);

    List<String> canRead(User user, ProgramInstance programInstance, boolean skipOwnershipCheck);

    List<String> canCreate(User user, ProgramInstance programInstance, boolean skipOwnershipCheck);

    List<String> canUpdate(User user, ProgramInstance programInstance, boolean skipOwnershipCheck);

    List<String> canDelete(User user, ProgramInstance programInstance, boolean skipOwnershipCheck);

    List<String> canRead(User user, ProgramStageInstance programStageInstance,
        boolean skipOwnershipCheck);

    List<String> canCreate(User user, ProgramStageInstance programStageInstance,
        boolean skipOwnershipCheck);

    List<String> canUpdate(User user, ProgramStageInstance programStageInstance,
        boolean skipOwnershipCheck);

    List<String> canDelete(User user, ProgramStageInstance programStageInstance,
        boolean skipOwnershipCheck);

    List<String> canRead(User user, Relationship relationship);

    List<String> canWrite(User user, Relationship relationship);

    /**
     * Checks the sharing read access to EventDataValue
     *
     * @param user User validated for write access
     * @param programStageInstance ProgramStageInstance under which the EventDataValue belongs
     * @param dataElement DataElement of EventDataValue
     * @return Empty list if read access allowed, list of errors otherwise.
     */
    List<String> canRead(User user, ProgramStageInstance programStageInstance,
        DataElement dataElement, boolean skipOwnershipCheck);

    /**
     * Checks the sharing write access to EventDataValue
     *
     * @param user User validated for write access
     * @param programStageInstance ProgramStageInstance under which the EventDataValue belongs
     * @param dataElement DataElement of EventDataValue
     * @return Empty list if write access allowed, list of errors otherwise.
     */
    List<String> canWrite(User user, ProgramStageInstance programStageInstance,
        DataElement dataElement, boolean skipOwnershipCheck);

    List<String> canRead(User user, CategoryOptionCombo categoryOptionCombo);

    List<String> canWrite(User user, CategoryOptionCombo categoryOptionCombo);
}
