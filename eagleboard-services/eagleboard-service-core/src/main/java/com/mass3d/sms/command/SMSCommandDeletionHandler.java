package com.mass3d.sms.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.dataset.DataSet;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.sms.command.SMSCommandDeletionHandler" )
public class SMSCommandDeletionHandler
    extends DeletionHandler
{
    private final SMSCommandService smsCommandService;

    public SMSCommandDeletionHandler( SMSCommandService smsCommandService )
    {
        checkNotNull( smsCommandService );
        this.smsCommandService = smsCommandService;
    }

    @Override
    protected String getClassName()
    {
        return SMSCommand.class.getSimpleName();
    }

    @Override
    public String allowDeleteDataSet( DataSet dataSet )
    {
        return smsCommandService.countDataSetSmsCommands( dataSet ) == 0 ? null : ERROR;
    }
}
