package com.mass3d.program;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.dataelement.DataElement;

/**
 * TODO do we need this service given cascade all on
 * program stage -> program stage data element?
 *
 */
public interface ProgramStageDataElementService
{
    String ID = ProgramStageInstanceService.class.getName();

    /**
     * Adds an {@link ProgramStageDataElement}
     *
     * @param programStageDataElement The to ProgramStageDataElement add.
     */
    void addProgramStageDataElement(ProgramStageDataElement programStageDataElement);

    /**
     * Updates an {@link ProgramStageDataElement}.
     *
     * @param programStageDataElement the ProgramStageDataElement to update.
     */
    void updateProgramStageDataElement(ProgramStageDataElement programStageDataElement);

    /**
     * Deletes a {@link ProgramStageDataElement}.
     *
     * @param programStageDataElement the ProgramStageDataElement to delete.
     */
    void deleteProgramStageDataElement(ProgramStageDataElement programStageDataElement);

    /**
     * Retrieve ProgramStageDataElement list on a program stage and a data
     * element
     *
     * @param programStage ProgramStage
     * @param dataElement  DataElement
     * @return ProgramStageDataElement
     */
    ProgramStageDataElement get(ProgramStage programStage, DataElement dataElement);

    /**
     * Returns all {@link ProgramStageDataElement}
     *
     * @return a collection of all ProgramStageDataElement, or an empty
     * collection if there are no ProgramStageDataElements.
     */
    List<ProgramStageDataElement> getAllProgramStageDataElements();

    /**
     * Returns Map of ProgramStages containing Set of DataElements (together ProgramStageDataElements) that have skipSynchronization flag set to true
     *
     * @return Map<String, Set<String>>
     */
    Map<String, Set<String>> getProgramStageDataElementsWithSkipSynchronizationSetToTrue();
}
