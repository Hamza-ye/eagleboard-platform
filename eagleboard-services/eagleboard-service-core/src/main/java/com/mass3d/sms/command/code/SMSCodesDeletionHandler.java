package com.mass3d.sms.command.code;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.dataelement.DataElement;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.jdbc.core.JdbcTemplate;

public class SMSCodesDeletionHandler
    extends DeletionHandler
{
    private final JdbcTemplate jdbcTemplate;

    public SMSCodesDeletionHandler( JdbcTemplate jdbcTemplate )
    {
        checkNotNull( jdbcTemplate );
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected String getClassName()
    {
        return SMSCode.class.getSimpleName();
    }

    @Override
    public String allowDeleteDataElement( DataElement dataElement )
    {
        String sql = "SELECT COUNT(*) FROM smscodes where dataelementid=" + dataElement.getId();

        return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
    }
}
