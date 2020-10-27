package com.mass3d.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration( "supportServiceConfig" )
public class ServiceConfig
{
    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    @Bean
    public UriComponentsBuilder uriComponentsBuilder()
    {
        return UriComponentsBuilder.newInstance();
    }
}
