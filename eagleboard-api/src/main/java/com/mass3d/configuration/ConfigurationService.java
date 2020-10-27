package com.mass3d.configuration;

import com.mass3d.user.User;

public interface ConfigurationService
{
    String ID = ConfigurationService.class.getName();
    
    /**
     * Sets the configuration.
     * 
     * @param configuration the configuration.
     */
    void setConfiguration(Configuration configuration);
    
    /**
     * Gets the configuration.
     * 
     * @return the configuration.
     */
    Configuration getConfiguration();
    
    /**
     * Indicates whether the given origin is CORS white listed.
     * 
     * @param origin the origin.
     * @return true if the given origin is CORS white listed.
     */
    boolean isCorsWhitelisted(String origin);

    /**
     * Indicates whether the current user is part of the feedback Recipients group
     */
    boolean isUserInFeedbackRecipientUserGroup(User user);
}
