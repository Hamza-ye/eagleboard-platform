package com.mass3d.sms.outbound;

import java.util.List;

public interface OutboundSmsService
{
    String ID = OutboundSmsService.class.getName();

    long save(OutboundSms sms);

    List<OutboundSms> get(OutboundSmsStatus status);

    List<OutboundSms> get(OutboundSmsStatus status, Integer min, Integer max, boolean hasPagination);

    OutboundSms get(long id);

    OutboundSms get(String uid);

    List<OutboundSms> getAll();

    List<OutboundSms> getAll(Integer min, Integer max, boolean hasPagination);

    void delete(long id);

    void delete(String uid);
}
