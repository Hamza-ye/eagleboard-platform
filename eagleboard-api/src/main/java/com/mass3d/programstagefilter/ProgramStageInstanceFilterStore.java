package com.mass3d.programstagefilter;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface ProgramStageInstanceFilterStore extends IdentifiableObjectStore<ProgramStageInstanceFilter>
{
    List<ProgramStageInstanceFilter> getByProgram(String program);
}
