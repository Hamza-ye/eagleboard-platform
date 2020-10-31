package com.mass3d.dataentryform;

import com.mass3d.common.IdentifiableObjectStore;

public interface DataEntryFormStore
    extends IdentifiableObjectStore<DataEntryForm>
{
    String ID = DataEntryFormStore.class.getName();

    /**
     * Returns a DataEntryForm with the given name.
     *
     * @param name The name.
     * @return A DataEntryForm with the given name.
     */
    DataEntryForm getDataEntryFormByName(String name);
}
