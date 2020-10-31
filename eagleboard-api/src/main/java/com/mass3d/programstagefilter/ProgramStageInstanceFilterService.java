package com.mass3d.programstagefilter;

import java.util.List;

public interface ProgramStageInstanceFilterService
{
    String ID = ProgramStageInstanceFilter.class.getName();
   
    List<String> validate(ProgramStageInstanceFilter programStageInstanceFilter);
}
