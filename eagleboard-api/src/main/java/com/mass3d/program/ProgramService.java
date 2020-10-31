package com.mass3d.program;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.trackedentity.TrackedEntityType;
import com.mass3d.user.User;

public interface ProgramService
{
    String ID = ProgramService.class.getName();

    Pattern INPUT_PATTERN = Pattern.compile( "(<input.*?/>)", Pattern.DOTALL );
    Pattern DYNAMIC_ATTRIBUTE_PATTERN = Pattern.compile( "attributeid=\"(\\w+)\"" );
    Pattern PROGRAM_PATTERN = Pattern.compile( "programid=\"(\\w+)\"" );
    Pattern VALUE_TAG_PATTERN = Pattern.compile( "value=\"(.*?)\"", Pattern.DOTALL );
    Pattern TITLE_TAG_PATTERN = Pattern.compile( "title=\"(.*?)\"", Pattern.DOTALL );
    Pattern SUGGESTED_VALUE_PATTERN = Pattern.compile( "suggested=('|\")(\\w*)('|\")" );
    Pattern CLASS_PATTERN = Pattern.compile( "class=('|\")(\\w*)('|\")" );
    Pattern STYLE_PATTERN = Pattern.compile( "style=('|\")([\\w|\\d\\:\\;]+)('|\")" );

    /**
     * Adds an {@link Program}
     *
     * @param program The to Program add.
     * @return A generated unique id of the added {@link Program}.
     */
    long addProgram(Program program);

    /**
     * Updates an {@link Program}.
     *
     * @param program the Program to update.
     */
    void updateProgram(Program program);

    /**
     * Deletes a {@link Program}. All {@link ProgramStage},
     * {@link ProgramInstance} and {@link ProgramStageInstance} belong to this
     * program are removed
     *
     * @param program the Program to delete.
     */
    void deleteProgram(Program program);

    /**
     * Returns a {@link Program}.
     *
     * @param id the id of the Program to return.
     * @return the Program with the given id
     */
    Program getProgram(long id);

    /**
     * Returns all {@link Program}.
     *
     * @return a collection of all Program, or an empty collection if there are
     *         no Programs.
     */
    List<Program> getAllPrograms();

    /**
     * Get all {@link Program} belong to a orgunit
     *
     * @param organisationUnit {@link OrganisationUnit}
     * @return The program list
     */
    List<Program> getPrograms(OrganisationUnit organisationUnit);

    /**
     * Get {@link Program} by a type
     *
     * @param type The type of program. There are three types, include Multi
     *        events with registration, Single event with registration and
     *        Single event without registration
     * @return Program list by a type specified
     */
    List<Program> getPrograms(ProgramType type);

    /**
     * Returns the {@link Program} with the given UID.
     *
     * @param uid the UID.
     * @return the Program with the given UID, or null if no match.
     */
    Program getProgram(String uid);

    /**
     * Get {@link TrackedEntityType} by TrackedEntityType
     *
     * @param trackedEntityType {@link TrackedEntityType}
     */
    List<Program> getProgramsByTrackedEntityType(TrackedEntityType trackedEntityType);

    /**
     * Get all Programs with the given DataEntryForm.
     *
     * @param dataEntryForm the DataEntryForm.
     * @return a list of Programs.
     */
    List<Program> getProgramsByDataEntryForm(DataEntryForm dataEntryForm);

    /**
     * Get {@link Program} by the current user. Returns all programs if current
     * user is superuser. Returns an empty list if there is no current user.
     *
     * @return Immutable set of programs associated with the current user.
     */
    List<Program> getUserPrograms();

    List<Program> getUserPrograms(User user);

    /**
     * Get {@link Program} by the current user and a certain type
     *
     * @param programType The type of program. There are three types, include Multi
     *        events with registration, Single event with registration and
     *        Single event without registration.
     * @return Immutable set of programs associated with the current user.
     */
    Set<Program> getUserPrograms(ProgramType programType);

    /**
     * Sets the given merge organisation units on the given programs. Only
     * the sub-hierarchy of the current user is modified.
     *
     * @param program the program.
     * @param mergeOrganisationUnits the merge organisation units.
     */
    void mergeWithCurrentUserOrganisationUnits(Program program,
        Collection<OrganisationUnit> mergeOrganisationUnits);

    /**
     * Returns a list of generated, non-persisted program data elements for the
     * program with the given identifier.
     *
     * @param programUid the program identifier.
     * @return a list of program data elements.
     */
    List<ProgramDataElementDimensionItem> getGeneratedProgramDataElements(String programUid);
}
