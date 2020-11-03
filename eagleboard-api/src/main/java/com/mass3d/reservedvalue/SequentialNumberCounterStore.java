package com.mass3d.reservedvalue;

import java.util.List;

public interface SequentialNumberCounterStore
{
    List<Integer> getNextValues(String uid, String key, int length);

    void deleteCounter(String uid);

}
