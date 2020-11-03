package com.mass3d.sms.config;

public interface SmsConfigurationManager
{
    SmsConfiguration getSmsConfiguration();

    void updateSmsConfiguration(SmsConfiguration config);

    SmsGatewayConfig checkInstanceOfGateway(Class<?> clazz);
}