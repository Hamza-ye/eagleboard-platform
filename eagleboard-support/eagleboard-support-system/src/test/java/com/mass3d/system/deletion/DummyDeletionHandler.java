package com.mass3d.system.deletion;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.dataelement.DataElement;

/**
 * @version $Id$
 */
@Slf4j
public class DummyDeletionHandler
    extends DeletionHandler
{
    @Override
    public String getClassName()
    {
        return Object.class.getSimpleName();
    }
    
    @Override
    public void deleteDataElement( DataElement dataElement )
    {
        log.info( "Iterating over all objects and removing associations to the data element" );
    }
}
