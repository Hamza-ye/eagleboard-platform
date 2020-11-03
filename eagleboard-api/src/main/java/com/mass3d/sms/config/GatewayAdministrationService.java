package com.mass3d.sms.config;

import java.util.Map;

public interface GatewayAdministrationService
{
    void setDefaultGateway(String uid);

    void setDefaultGateway(SmsGatewayConfig config);

    boolean removeGatewayByUid(String uid);

    Map<String, SmsGatewayConfig> getGatewayConfigurationMap();

    SmsGatewayConfig getDefaultGateway();

    boolean hasDefaultGateway();

    SmsGatewayConfig getByUid(String uid);

    boolean addOrUpdateGateway(SmsGatewayConfig config, Class<?> klass);

    boolean addGateway(SmsGatewayConfig config);

    void updateGateway(SmsGatewayConfig persisted, SmsGatewayConfig updatedConfig);

    boolean loadGatewayConfigurationMap(SmsConfiguration smsConfiguration);

    Class<? extends SmsGatewayConfig> getGatewayType(SmsGatewayConfig config);
}
