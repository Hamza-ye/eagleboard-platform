package com.mass3d.sms.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BulkSmsRequestEntity
{
    private Set<BulkSmsRecipient> recipients = new HashSet<>();

    private String body;

    public BulkSmsRequestEntity()
    {
    }

    public BulkSmsRequestEntity( String body, Set<String> recipients )
    {
        this.recipients = recipients.stream().map( BulkSmsRecipient::new ).collect( Collectors.toSet() );
        this.body = body;
    }

    @JsonProperty( value = "to" )
    public Set<BulkSmsRecipient> getRecipients()
    {
        return recipients;
    }

    public String getBody()
    {
        return body;
    }
}
