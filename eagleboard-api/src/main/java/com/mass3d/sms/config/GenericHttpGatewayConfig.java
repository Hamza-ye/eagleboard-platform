package com.mass3d.sms.config;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.mass3d.sms.config.views.SmsConfigurationViews;

@JsonTypeName( "http" )
public class GenericHttpGatewayConfig
    extends SmsGatewayConfig
{
    private static final long serialVersionUID = 6340853488475760213L;

    @JsonView( SmsConfigurationViews.Public.class )
    private String configurationTemplate;

    @JsonView( SmsConfigurationViews.Public.class )
    private boolean useGet;

    @JsonView( SmsConfigurationViews.Public.class )
    private ContentType contentType = ContentType.FORM_URL_ENCODED;

    @JsonView( SmsConfigurationViews.Public.class )
    private List<GenericGatewayParameter> parameters = Lists.newArrayList();

    public List<GenericGatewayParameter> getParameters()
    {
        return parameters;
    }

    public Map<String, String> getParametersMap()
    {
        return parameters.stream()
            .collect( Collectors.toMap( GenericGatewayParameter::getKey, GenericGatewayParameter::getDisplayValue ) );
    }

    public void setParameters( List<GenericGatewayParameter> parameters )
    {
        this.parameters = parameters;
    }

    public boolean isUseGet()
    {
        return useGet;
    }

    public void setUseGet( boolean useGet )
    {
        this.useGet = useGet;
    }

    public String getConfigurationTemplate()
    {
        return configurationTemplate;
    }

    public void setConfigurationTemplate( String configurationTemplate )
    {
        this.configurationTemplate = configurationTemplate;
    }

    public ContentType getContentType()
    {
        return contentType;
    }

    public void setContentType( ContentType contentType )
    {
        this.contentType = contentType;
    }
}
