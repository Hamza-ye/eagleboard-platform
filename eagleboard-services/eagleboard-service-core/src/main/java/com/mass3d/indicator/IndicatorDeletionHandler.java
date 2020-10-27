package com.mass3d.indicator;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mass3d.expression.ParseType.INDICATOR_EXPRESSION;

import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataset.DataSet;
import com.mass3d.expression.ExpressionService;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

/**
 * @version $Id$
 */
@Component( "com.mass3d.indicator.IndicatorDeletionHandler" )
public class IndicatorDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final IndicatorService indicatorService;

    private final ExpressionService expressionService;

    public IndicatorDeletionHandler( IndicatorService indicatorService, ExpressionService expressionService )
    {
        checkNotNull( indicatorService );
        checkNotNull( expressionService );

        this.indicatorService = indicatorService;
        this.expressionService = expressionService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return Indicator.class.getSimpleName();
    }

    @Override
    public String allowDeleteIndicatorType( IndicatorType indicatorType )
    {
        for ( Indicator indicator : indicatorService.getAllIndicators() )
        {
            if ( indicator.getIndicatorType().equals( indicatorType ) )
            {
                return indicator.getName();
            }
        }

        return null;
    }

    @Override
    public void deleteIndicatorGroup( IndicatorGroup group )
    {
        for ( Indicator indicator : group.getMembers() )
        {
            indicator.getGroups().remove( group );
            indicatorService.updateIndicator( indicator );
        }
    }

    @Override
    public void deleteDataSet( DataSet dataSet )
    {
        for ( Indicator indicator : dataSet.getIndicators() )
        {
            indicator.getDataSets().remove( dataSet );
            indicatorService.updateIndicator( indicator );
        }
    }

//    @Override
//    public void deleteLegendSet( LegendSet legendSet )
//    {
//        for ( Indicator indicator : indicatorService.getAllIndicators() )
//        {
//            for ( LegendSet ls : indicator.getLegendSets() )
//            {
//                if( legendSet.equals( ls ) )
//                {
//                    indicator.getLegendSets().remove( ls );
//                    indicatorService.updateIndicator( indicator );
//                }
//
//            }
//        }
//    }
//
//    @Override
//    public String allowDeleteDataElement( DataElement dataElement )
//    {
//        for ( Indicator indicator : indicatorService.getAllIndicators() )
//        {
//            Set<DataElement> daels = expressionService.getExpressionDataElements( indicator.getNumerator(), INDICATOR_EXPRESSION );
//
//            if ( daels != null && daels.contains( dataElement ) )
//            {
//                return indicator.getName();
//            }
//
//            daels = expressionService.getExpressionDataElements( indicator.getDenominator(), INDICATOR_EXPRESSION );
//
//            if ( daels != null && daels.contains( dataElement ) )
//            {
//                return indicator.getName();
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public String allowDeleteCategoryCombo( CategoryCombo categoryCombo )
//    {
//        Set<String> optionComboIds = categoryCombo.getOptionCombos().stream()
//            .map( CategoryOptionCombo::getUid ).collect( Collectors.toSet() );
//
//        for ( Indicator indicator : indicatorService.getAllIndicators() )
//        {
//            Set<String> comboIds = expressionService.getExpressionOptionComboIds(
//                indicator.getNumerator(), INDICATOR_EXPRESSION );
//            comboIds.retainAll( optionComboIds );
//
//            if ( !comboIds.isEmpty() )
//            {
//                return indicator.getName();
//            }
//
//            comboIds = expressionService.getExpressionOptionComboIds(
//                indicator.getDenominator(), INDICATOR_EXPRESSION );
//            comboIds.retainAll( optionComboIds );
//
//            if ( !comboIds.isEmpty() )
//            {
//                return indicator.getName();
//            }
//        }
//
//        return null;
//    }
}
