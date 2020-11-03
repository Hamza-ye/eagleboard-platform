package com.mass3d.sms.incoming;

import java.util.Date;
import java.util.List;
import com.mass3d.user.User;

/**
 * Service providing support for retrieving incoming SMSes.
 */
public interface IncomingSmsService
{
    String ID = IncomingSmsService.class.getName();

    void update(IncomingSms sms);

    IncomingSms get(long id);

    IncomingSms get(String uid);

    List<IncomingSms> getAll();

    List<IncomingSms> getAll(Integer min, Integer max, boolean hasPagination);

    void delete(long id);

    void delete(String uid);

    long save(IncomingSms sms);

    long save(String message, String originator, String gateway, Date receivedTime, User user);

    List<IncomingSms> getSmsByStatus(SmsMessageStatus status, String originator);

    List<IncomingSms> getSmsByStatus(SmsMessageStatus status, String keyword, Integer min,
        Integer max, boolean hasPagination);

    List<IncomingSms> getAllUnparsedMessages();
}
