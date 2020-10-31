package com.mass3d.dataset;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.List;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataelement.DataElement;
import com.mass3d.indicator.Indicator;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.dataset.DataSetDeletionHandler" )
public class DataSetDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    private final DataSetService dataSetService;

    public DataSetDeletionHandler( IdentifiableObjectManager idObjectManager, DataSetService dataSetService )
    {
        checkNotNull( idObjectManager );
        checkNotNull( dataSetService );

        this.idObjectManager = idObjectManager;
        this.dataSetService = dataSetService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return DataSet.class.getSimpleName();
    }

    @Override
    public void deleteDataElement( DataElement dataElement )
    {
        Iterator<DataSetElement> elements = dataElement.getDataSetElements().iterator();
        
        while ( elements.hasNext() )
        {
            DataSetElement element = elements.next();
            elements.remove();
            
            dataElement.removeDataSetElement( element );
            idObjectManager.updateNoAcl( element.getDataSet() );
        }
        
        List<DataSet> dataSets = idObjectManager.getAllNoAcl( DataSet.class );
        
        for ( DataSet dataSet : dataSets )
        {
            boolean update = false;
            
//            Iterator<DataElementOperand> operands = dataSet.getCompulsoryDataElementOperands().iterator();
//
//            while ( operands.hasNext() )
//            {
//                DataElementOperand operand = operands.next();
//
//                if ( operand.getDataElement().equals( dataElement ) )
//                {
//                    operands.remove();
//                    update = true;
//                }
//            }
            
            if ( update )
            {
                idObjectManager.updateNoAcl( dataSet );
            }
        }
    }
    
    @Override
    public void deleteIndicator( Indicator indicator )
    {
        for ( DataSet dataSet : indicator.getDataSets() )
        {
            dataSet.getIndicators().remove( indicator );
            idObjectManager.updateNoAcl( dataSet );
        }
    }
    
//    @Override
//    public void deleteSection( Section section )
//    {
//        DataSet dataSet = section.getDataSet();
//
//        if ( dataSet != null )
//        {
//            dataSet.getSections().remove( section );
//            idObjectManager.updateNoAcl( dataSet );
//        }
//    }
    
//    @Override
//    public void deleteLegendSet( LegendSet legendSet )
//    {
//        for ( DataSet dataSet : idObjectManager.getAllNoAcl( DataSet.class ) )
//        {
//            for ( LegendSet ls : dataSet.getLegendSets() )
//            {
//                if( legendSet.equals( ls ) )
//                {
//                    dataSet.getLegendSets().remove( ls );
//                    idObjectManager.updateNoAcl( dataSet );
//                }
//
//            }
//        }
//    }
    
//    @Override
//    public void deleteCategoryCombo( CategoryCombo categoryCombo )
//    {
//        CategoryCombo defaultCategoryCombo = categoryService
//            .getCategoryComboByName( DEFAULT_CATEGORY_COMBO_NAME );
//
//        Collection<DataSet> dataSets = idObjectManager.getAllNoAcl( DataSet.class );
//
//        for ( DataSet dataSet : dataSets )
//        {
//            if ( dataSet != null && categoryCombo.equals( dataSet.getCategoryCombo() ) )
//            {
//                dataSet.setCategoryCombo( defaultCategoryCombo );
//                idObjectManager.updateNoAcl( dataSet );
//            }
//        }
//    }
//
//    @Override
//    public void deleteOrganisationUnit( OrganisationUnit unit )
//    {
//        for ( DataSet dataSet : unit.getDataSets() )
//        {
//            dataSet.getTodoTasks().remove( unit );
//            idObjectManager.updateNoAcl( dataSet );
//        }
//    }
//
//    @Override
//    public void deleteDataEntryForm( DataEntryForm dataEntryForm )
//    {
//        List<DataSet> associatedDataSets = dataSetService.getDataSetsByDataEntryForm( dataEntryForm );
//
//        for ( DataSet dataSet : associatedDataSets )
//        {
//            dataSet.setDataEntryForm( null );
//            idObjectManager.updateNoAcl( dataSet );
//        }
//    }
//
//    @Override
//    public void deleteDataApprovalWorkflow( DataApprovalWorkflow workflow )
//    {
//        for ( DataSet dataSet : workflow.getDataSets() )
//        {
//            dataSet.setWorkflow( null );
//            idObjectManager.updateNoAcl( dataSet );
//        }
//    }
}
