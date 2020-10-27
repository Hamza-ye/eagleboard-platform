package com.mass3d.indicator;

import java.util.List;

/**
 * @version $Id$
 */
public interface IndicatorService
{
    String ID = IndicatorService.class.getName();

    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    long addIndicator(Indicator indicator);

    void updateIndicator(Indicator indicator);

    void deleteIndicator(Indicator indicator);

    Indicator getIndicator(long id);

    Indicator getIndicator(String uid);

    List<Indicator> getAllIndicators();

    List<Indicator> getIndicatorsWithGroupSets();

    List<Indicator> getIndicatorsWithoutGroups();

    List<Indicator> getIndicatorsWithDataSets();

    // -------------------------------------------------------------------------
    // IndicatorType
    // -------------------------------------------------------------------------

    long addIndicatorType(IndicatorType indicatorType);

    void updateIndicatorType(IndicatorType indicatorType);

    void deleteIndicatorType(IndicatorType indicatorType);

    IndicatorType getIndicatorType(long id);

    IndicatorType getIndicatorType(String uid);

    List<IndicatorType> getAllIndicatorTypes();

    // -------------------------------------------------------------------------
    // IndicatorGroup
    // -------------------------------------------------------------------------

    long addIndicatorGroup(IndicatorGroup indicatorGroup);

    void updateIndicatorGroup(IndicatorGroup indicatorGroup);

    void deleteIndicatorGroup(IndicatorGroup indicatorGroup);

    IndicatorGroup getIndicatorGroup(long id);

    IndicatorGroup getIndicatorGroup(String uid);

    List<IndicatorGroup> getAllIndicatorGroups();

    // -------------------------------------------------------------------------
    // IndicatorGroupSet
    // -------------------------------------------------------------------------

    long addIndicatorGroupSet(IndicatorGroupSet groupSet);

    void updateIndicatorGroupSet(IndicatorGroupSet groupSet);

    void deleteIndicatorGroupSet(IndicatorGroupSet groupSet);

    IndicatorGroupSet getIndicatorGroupSet(long id);

    IndicatorGroupSet getIndicatorGroupSet(String uid);

    List<IndicatorGroupSet> getAllIndicatorGroupSets();
}
