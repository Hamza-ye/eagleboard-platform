package com.mass3d.dataelement;

import com.mass3d.common.IdentifiableObjectStore;

public interface DataElementOperandStore
    extends IdentifiableObjectStore<DataElementOperand>
{
    String ID = DataElementOperand.class.getName();
}
