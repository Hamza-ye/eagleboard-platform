package com.mass3d.indicator;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface IndicatorStore
    extends IdentifiableObjectStore<Indicator>
{
    String ID = IndicatorStore.class.getName();

    List<Indicator> getIndicatorsWithGroupSets();

    List<Indicator> getIndicatorsWithoutGroups();

    List<Indicator> getIndicatorsWithDataSets();
}
