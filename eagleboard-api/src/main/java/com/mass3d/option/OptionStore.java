package com.mass3d.option;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

/**
 *
 * @version $OptionStore.java Jun 15, 2012 9:45:00 AM$
 */
public interface OptionStore 
    extends IdentifiableObjectStore<Option>
{
    List<Option> getOptions(long optionSetId, String key, Integer max);
}

