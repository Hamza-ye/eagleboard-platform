package com.mass3d.sms.incoming;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

/**
 * Store for incoming SMS messages.
 */
public interface IncomingSmsStore extends IdentifiableObjectStore<IncomingSms>
{
    String ID = IncomingSmsStore.class.getName();

    List<IncomingSms> getSmsByStatus(SmsMessageStatus status, String originator);

    List<IncomingSms> getAll(Integer min, Integer max, boolean hasPagination);

    List<IncomingSms> getSmsByStatus(SmsMessageStatus status, String keyword, Integer min,
        Integer max, boolean hasPagination);

    List<IncomingSms> getSmsByOriginator(String originator);

    List<IncomingSms> getAllUnparsedMessages();
}
