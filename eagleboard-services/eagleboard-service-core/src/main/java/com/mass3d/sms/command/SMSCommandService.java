package com.mass3d.sms.command;

import java.util.List;
import java.util.Set;
import com.mass3d.dataset.DataSet;
import com.mass3d.sms.command.code.SMSCode;
import com.mass3d.sms.parse.ParserType;

public interface SMSCommandService
{
    List<SMSCommand> getSMSCommands();

    SMSCommand getSMSCommand(long id);

    void save(SMSCommand cmd);

    void delete(SMSCommand cmd);

    List<SMSCommand> getJ2MESMSCommands();

    SMSCommand getSMSCommand(String commandName, ParserType parserType);

    void addSpecialCharacterSet(Set<SMSSpecialCharacter> specialCharacters, long commandId);

    void addSmsCodes(Set<SMSCode> codes, long commandId);

    void deleteSpecialCharacterSet(Set<SMSSpecialCharacter> specialCharacters, long commandId);

    void deleteCodeSet(Set<SMSCode> codes, long commandId);

    int countDataSetSmsCommands(DataSet dataSet);

    SMSCommand getSMSCommand(String name);
}
