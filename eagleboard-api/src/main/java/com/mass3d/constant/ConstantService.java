package com.mass3d.constant;

import java.util.List;
import java.util.Map;

public interface ConstantService
{
    String ID = ConstantService.class.getName();

    // -------------------------------------------------------------------------
    // Concept
    // -------------------------------------------------------------------------

    long saveConstant(Constant constant);

    void deleteConstant(Constant constant);

    void updateConstant(Constant constant);

    Constant getConstant(int constantId);

    Constant getConstant(String uid);

    List<Constant> getAllConstants();

    Map<String, Constant> getConstantMap();

    Map<String, Double> getConstantParameterMap();

    // -------------------------------------------------------------------------
    // Constant expanding
    // -------------------------------------------------------------------------

    List<Constant> getConstantsBetween(int first, int max);

    List<Constant> getConstantsBetweenByName(String name, int first, int max);

    int getConstantCount();

    int getConstantCountByName(String name);
}
