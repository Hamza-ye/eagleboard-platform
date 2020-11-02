package com.mass3d.dataset;

import com.mass3d.common.IdentifiableObjectStore;

public interface SectionStore
    extends IdentifiableObjectStore<Section>
{
    String ID = SectionStore.class.getName();

    /**
     * Retrieves the Section with the given name and the given DataSet.
     *
     * @param name the name of the Section to retrieve.
     * @return the Section.
     */
    Section getSectionByName(String name, DataSet dataSet);
}
