package com.mass3d.statistics;

import java.util.Map;
import com.mass3d.common.Objects;

/**
 * @version $Id$
 */
public interface StatisticsProvider
{
    String ID = StatisticsProvider.class.getName();
    
    Map<Objects, Integer> getObjectCounts();
}
