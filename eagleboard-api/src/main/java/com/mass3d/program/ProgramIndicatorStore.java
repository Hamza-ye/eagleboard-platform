package com.mass3d.program;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface ProgramIndicatorStore
    extends IdentifiableObjectStore<ProgramIndicator>
{
    List<ProgramIndicator> getProgramIndicatorsWithNoExpression();
}
