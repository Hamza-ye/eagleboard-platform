package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Session;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataset.DataInputPeriod;
import com.mass3d.dataset.DataSet;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.util.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
public class DataSetObjectBundleHook extends AbstractObjectBundleHook
{
    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        List<ErrorReport> errors = new ArrayList<>();

        if ( object == null || !object.getClass().isAssignableFrom( DataSet.class ) )
        {
            return errors;
        }

        DataSet dataSet = (DataSet) object;

        Set<DataInputPeriod> inputPeriods = dataSet.getDataInputPeriods();

        if ( inputPeriods.size() > 0 )
        {
            for ( DataInputPeriod period : inputPeriods )
            {
                if ( ObjectUtils.allNonNull( period.getOpeningDate(), period.getClosingDate() )
                    && period.getOpeningDate().after( period.getClosingDate() ) )
                {
                    errors.add( new ErrorReport( DataSet.class, ErrorCode.E4013, period.getClosingDate(),
                        period.getOpeningDate() ) );
                }
            }
        }
        return errors;
    }

    @Override
    public <T extends IdentifiableObject> void preUpdate( T object, T persistedObject, ObjectBundle bundle )
    {
        if ( object == null || !object.getClass().isAssignableFrom( DataSet.class ) )
            return;

//        deleteRemovedDataElementFromSection( (DataSet) persistedObject, (DataSet) object );
//        deleteRemovedSection( (DataSet) persistedObject, (DataSet) object, bundle );
    }

//    private void deleteRemovedSection( DataSet persistedDataSet, DataSet importDataSet, ObjectBundle bundle )
//    {
//        if ( !bundle.isMetadataSyncImport() )
//            return;
//
//        Session session = sessionFactory.getCurrentSession();
//
//        List<String> importIds = importDataSet.getSections().stream().map( section -> section.getUid() )
//            .collect( Collectors.toList() );
//
//        persistedDataSet.getSections().stream().filter( section -> !importIds.contains( section.getUid() ) )
//            .forEach( section -> session.delete( section ) );
//    }

//    private void deleteRemovedDataElementFromSection( DataSet persistedDataSet, DataSet importDataSet )
//    {
//        Session session = sessionFactory.getCurrentSession();
//
//        persistedDataSet.getSections().stream()
//            .peek( section -> section.setDataElements( getUpdatedDataElements( importDataSet, section ) ) )
//            .forEach( section -> session.update( section ) );
//    }

//    private List<DataElement> getUpdatedDataElements( DataSet importDataSet, Section section )
//    {
//        return section.getDataElements().stream().filter( de -> {
//            Set<String> dataElements = importDataSet.getDataElements().stream()
//                .map( dataElement -> dataElement.getUid() ).collect( Collectors.toSet() );
//            return dataElements.contains( de.getUid() );
//        } ).collect( Collectors.toList() );
//    }
}