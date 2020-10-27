package com.mass3d.dataelement;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataset.DataSet;
import com.mass3d.dataset.DataSetElement;
import com.mass3d.option.OptionSet;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.dataelement.DataElementDeletionHandler" )
public class DataElementDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    private final JdbcTemplate jdbcTemplate;

    public DataElementDeletionHandler( IdentifiableObjectManager idObjectManager, JdbcTemplate jdbcTemplate )
    {
        checkNotNull( idObjectManager );
        checkNotNull( jdbcTemplate );

        this.idObjectManager = idObjectManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return DataElement.class.getSimpleName();
    }


    @Override
    public void deleteDataSet( DataSet dataSet )
    {
        Iterator<DataSetElement> elements = dataSet.getDataSetElements().iterator();

        while ( elements.hasNext() )
        {
            DataSetElement element = elements.next();
            elements.remove();

            dataSet.removeDataSetElement( element );
            idObjectManager.updateNoAcl( element.getDataElement() );
        }
    }

    @Override
    public String allowDeleteOptionSet( OptionSet optionSet )
    {
        String sql = "SELECT COUNT(*) FROM dataelement WHERE optionsetid = " + optionSet.getId();

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
}
