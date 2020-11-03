package com.mass3d.sms.command.hibernate;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.dataset.DataSet;
import com.mass3d.sms.command.SMSCommand;
import com.mass3d.sms.parse.ParserType;

public interface SMSCommandStore
    extends IdentifiableObjectStore<SMSCommand>
{
    List<SMSCommand> getJ2MESMSCommands();

    SMSCommand getSMSCommand(String commandName, ParserType parserType);

    int countDataSetSmsCommands(DataSet dataSet);
}
