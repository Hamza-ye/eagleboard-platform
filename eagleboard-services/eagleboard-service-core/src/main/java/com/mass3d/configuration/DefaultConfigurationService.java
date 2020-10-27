package com.mass3d.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.Set;
import com.mass3d.common.GenericStore;
import com.mass3d.commons.util.TextUtils;
import com.mass3d.user.User;
import com.mass3d.user.UserGroup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.configuration.ConfigurationService" )
public class DefaultConfigurationService
    implements ConfigurationService
{
    private GenericStore<Configuration> configurationStore;

    public DefaultConfigurationService(
        @Qualifier( "com.mass3d.configuration.ConfigurationStore" ) GenericStore<Configuration> configurationStore )
    {
        checkNotNull( configurationStore );

        this.configurationStore = configurationStore;
    }

    // -------------------------------------------------------------------------
    // ConfigurationService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void setConfiguration( Configuration configuration )
    {
        if ( configuration != null && configuration.getId() > 0 )
        {
            configurationStore.update( configuration );
        }
        else
        {
            configurationStore.save( configuration );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Configuration getConfiguration()
    {
        Iterator<Configuration> iterator = configurationStore.getAll().iterator();

        return iterator.hasNext() ? iterator.next() : new Configuration();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCorsWhitelisted( String origin )
    {
        Set<String> corsWhitelist = getConfiguration().getCorsWhitelist();

        for ( String cors : corsWhitelist )
        {
            String regex = TextUtils.createRegexFromGlob( cors );

            if ( origin.matches( regex ) )
            {
                return true;
            }
        }

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserInFeedbackRecipientUserGroup( User user )
    {
        UserGroup feedbackRecipients = getConfiguration().getFeedbackRecipients();

        return feedbackRecipients != null && feedbackRecipients.getMembers().contains( user );
    }
}
