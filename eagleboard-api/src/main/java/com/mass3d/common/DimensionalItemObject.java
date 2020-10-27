package com.mass3d.common;

import com.mass3d.analytics.AggregationType;

public interface DimensionalItemObject
    extends NameableObject
{
    /**
     * Gets the dimension item identifier.
     */
    String getDimensionItem();

    /**
     * Gets the dimension item identifier based on the given
     * identifier scheme.
     *
     * @param idScheme the identifier scheme.
     */
    String getDimensionItem(IdScheme idScheme);

    /**
     * Gets the dimension type of this dimension item.
     */
    DimensionItemType getDimensionItemType();

    /**
     * Gets the aggregation type.
     */
    AggregationType getAggregationType();

    /**
     * Indicates whether this dimension has an aggregation type.
     */
    boolean hasAggregationType();

    /**
     * Gets the total aggregation type, meaning how total values
     * should be aggregated across multiple values.
     */
    TotalAggregationType getTotalAggregationType();

    /**
     * Gets a Period Offset: the offset can be applied within an Indicator formula
     * in order to "shift" the query period by the offset value (e.g. Jan 2020 with
     * offset 1 becomes Feb 2020). An offset with value 0 means no offset.
     * 
     * @return an int.
     */
    int getPeriodOffset();
}
