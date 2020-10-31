package com.mass3d.program;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.trackedentity.TrackedEntityType;

/**
 * @version $Id: ProgramStore.java Dec 14, 2011 9:22:17 AM $
 */
public interface ProgramStore
    extends IdentifiableObjectStore<Program>
{
    String ID = ProgramStore.class.getName();

    /**
     * Get {@link Program} by a type
     *
     * @param type The type of program. There are three types, include Multi
     *             events with registration, Single event with registration and
     *             Single event without registration
     * @return Program list by a type specified
     */
    List<Program> getByType(ProgramType type);

    /**
     * Get {@link Program} assigned to an {@link OrganisationUnit} by a type
     *
     * @param organisationUnit Where programs assigned
     * @return Program list by a type specified
     */
    List<Program> get(OrganisationUnit organisationUnit);

    /**
     * Get {@link Program} by TrackedEntityType
     *
     * @param trackedEntityType {@link TrackedEntityType}
     */
    List<Program> getByTrackedEntityType(TrackedEntityType trackedEntityType);

    /**
     * Get all Programs associated with the given DataEntryForm.
     * @param dataEntryForm the DataEntryForm.
     * @return a list of {@link Program}
     */
    List<Program> getByDataEntryForm(DataEntryForm dataEntryForm);
}
