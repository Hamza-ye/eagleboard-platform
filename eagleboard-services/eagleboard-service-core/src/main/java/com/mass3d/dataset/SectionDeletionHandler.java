package com.mass3d.dataset;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

/**
 * @version $Id$
 */
@Component( "com.mass3d.dataset.SectionDeletionHandler" )
public class SectionDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final SectionService sectionService;

    public SectionDeletionHandler( SectionService sectionService )
    {
        checkNotNull( sectionService );
        this.sectionService = sectionService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return Section.class.getSimpleName();
    }

    @Override
    public void deleteDataElement( DataElement dataElement )
    {
        for ( Section section : sectionService.getAllSections() )
        {
            List<DataElementOperand> operandsToRemove = section
                .getGreyedFields()
                .stream()
                .filter( operand -> operand.getDataElement().equals( dataElement ) )
                .collect( Collectors.toList() );

            operandsToRemove
                .stream()
                .forEach( operand -> section.getGreyedFields().remove( operand ) );

            if ( section.getDataElements().remove( dataElement ) || !operandsToRemove.isEmpty() )
            {
                sectionService.updateSection( section );
            }

        }
    }

    @Override
    public void deleteDataSet( DataSet dataSet )
    {
        Iterator<Section> iterator = dataSet.getSections().iterator();

        while ( iterator.hasNext() )
        {
            Section section = iterator.next();
            iterator.remove();
            sectionService.deleteSection( section );
        }
    }
}
