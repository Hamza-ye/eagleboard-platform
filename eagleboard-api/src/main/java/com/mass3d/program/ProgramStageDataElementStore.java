package com.mass3d.program;

import java.util.Map;
import java.util.Set;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.dataelement.DataElement;

public interface ProgramStageDataElementStore
    extends IdentifiableObjectStore<ProgramStageDataElement>
{
    String ID = ProgramStageInstanceStore.class.getName();

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
     * Returns Map of ProgramStages containing Set of DataElements (together ProgramStageDataElements) that have skipSynchronization flag set to true
     *
     * @return Map<String, Set<String>>
     */
    Map<String, Set<String>> getProgramStageDataElementsWithSkipSynchronizationSetToTrue();
}
