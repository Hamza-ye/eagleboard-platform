package com.mass3d.dataset;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version $Id$
 */
@Service( "com.mass3d.dataset.SectionService" )
public class DefaultSectionService
    implements SectionService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SectionStore sectionStore;

    private DataSetService dataSetService;

    public DefaultSectionService(SectionStore sectionStore, DataSetService dataSetService) {

        checkNotNull( sectionStore );
        checkNotNull( dataSetService );

        this.sectionStore = sectionStore;
        this.dataSetService = dataSetService;
    }

    // -------------------------------------------------------------------------
    // SectionService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addSection( Section section )
    {
        sectionStore.save( section );

        return section.getId();
    }

    @Override
    @Transactional
    public void deleteSection( Section section )
    {
        sectionStore.delete( section );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Section> getAllSections()
    {
        return sectionStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Section getSection( long id )
    {
        return sectionStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public Section getSection( String uid )
    {
        return sectionStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public Section getSectionByName( String name, Integer dataSetId )
    {
        return sectionStore.getSectionByName( name, dataSetService.getDataSet( dataSetId ) );
    }

    @Override
    @Transactional
    public void updateSection( Section section )
    {
        sectionStore.update( section );
    }
}
