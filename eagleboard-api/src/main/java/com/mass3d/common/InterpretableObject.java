package com.mass3d.common;

import com.mass3d.interpretation.Interpretation;
import java.util.Set;

public interface InterpretableObject
    extends IdentifiableObject {

  Set<Interpretation> getInterpretations();
}
