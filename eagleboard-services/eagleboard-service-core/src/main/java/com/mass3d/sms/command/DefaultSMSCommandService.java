package com.mass3d.sms.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Set;
import com.mass3d.dataset.DataSet;
import com.mass3d.sms.command.code.SMSCode;
import com.mass3d.sms.command.hibernate.SMSCommandStore;
import com.mass3d.sms.parse.ParserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.sms.command.SMSCommandService" )
public class DefaultSMSCommandService
    implements SMSCommandService
{
    private SMSCommandStore smsCommandStore;

    public DefaultSMSCommandService( SMSCommandStore smsCommandStore )
    {
        checkNotNull( smsCommandStore );

        this.smsCommandStore = smsCommandStore;
    }

    @Override
    @Transactional( readOnly = true )
    public List<SMSCommand> getSMSCommands()
    {
        return smsCommandStore.getAll();
    }

    public void setSmsCommandStore( SMSCommandStore smsCommandStore )
    {
        this.smsCommandStore = smsCommandStore;
    }

    @Override
    @Transactional
    public void save( SMSCommand cmd )
    {
        smsCommandStore.save( cmd );
    }

    @Override
    @Transactional( readOnly = true )
    public SMSCommand getSMSCommand( long id )
    {
        return smsCommandStore.get( id );
    }

    @Override
    @Transactional( readOnly = true )
    public SMSCommand getSMSCommand( String name )
    {
        return smsCommandStore.getByName( name );
    }

    @Override
    @Transactional
    public void addSmsCodes( Set<SMSCode> codes, long commandId )
    {
        SMSCommand command = smsCommandStore.get( commandId );

        if ( command != null )
        {
            command.getCodes().addAll( codes);

            smsCommandStore.update( command );
        }
    }

    @Override
    @Transactional
    public void delete( SMSCommand cmd )
    {
        smsCommandStore.delete( cmd );
    }

    @Override
    @Transactional( readOnly = true )
    public List<SMSCommand> getJ2MESMSCommands()
    {
        return smsCommandStore.getJ2MESMSCommands();
    }

    @Override
    @Transactional( readOnly = true )
    public SMSCommand getSMSCommand( String commandName, ParserType parserType )
    {
        return smsCommandStore.getSMSCommand( commandName, parserType );
    }

    @Override
    @Transactional
    public void addSpecialCharacterSet( Set<SMSSpecialCharacter> specialCharacters, long commandId )
    {
        SMSCommand command = smsCommandStore.get( commandId );

        if ( command != null )
        {
            command.getSpecialCharacters().addAll( specialCharacters );

            smsCommandStore.update( command );
        }
    }

    @Override
    @Transactional
    public void deleteCodeSet( Set<SMSCode> codes, long commandId )
    {
        SMSCommand command = smsCommandStore.get( commandId );

        command.getCodes().removeAll( codes );

        smsCommandStore.update( command );
    }

    @Override
    @Transactional( readOnly = true )
    public int countDataSetSmsCommands( DataSet dataSet )
    {
        return smsCommandStore.countDataSetSmsCommands( dataSet );
    }

    @Override
    @Transactional
    public void deleteSpecialCharacterSet( Set<SMSSpecialCharacter> specialCharacters, long commandId )
    {
        SMSCommand command = smsCommandStore.get( commandId );

        command.getSpecialCharacters().removeAll( specialCharacters );

        smsCommandStore.update( command );
    }
}
