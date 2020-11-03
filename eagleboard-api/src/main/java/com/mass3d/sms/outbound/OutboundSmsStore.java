package com.mass3d.sms.outbound;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface OutboundSmsStore  extends IdentifiableObjectStore<OutboundSms>
{
    void saveOutboundSms(OutboundSms sms);

    List<OutboundSms> getAllOutboundSms(Integer min, Integer max, boolean hasPagination);

    List<OutboundSms> get(OutboundSmsStatus status);

    List<OutboundSms> get(OutboundSmsStatus status, Integer min, Integer max, boolean hasPagination);
}
