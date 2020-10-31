package com.mass3d.program.message;

import java.util.Date;
import java.util.List;
import java.util.Set;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.outboundmessage.BatchResponseStatus;
import com.mass3d.user.User;

public interface ProgramMessageService
{
    ProgramMessageQueryParams getFromUrl(Set<String> ou, String programInstance,
        String programStageInstance,
        ProgramMessageStatus messageStatus, Integer page, Integer pageSize, Date afterDate,
        Date beforeDate);

    /**
     * To check if {@link ProgramMessage message} exists against the given uid.
     *
     * @param uid the uid of ProgramMessage.
     */
    boolean exists(String uid);

    void hasAccess(ProgramMessageQueryParams params, User user);

    void validateQueryParameters(ProgramMessageQueryParams params);

    /**
     * To validate {@link ProgramMessage message} payload in order to make sure
     * prerequisite values exist before message can be processed.
     *
     * @param message the ProgramMessage.
     */
    void validatePayload(ProgramMessage message);

    // -------------------------------------------------------------------------
    // Transport Service methods
    // -------------------------------------------------------------------------

    /**
     * Send message batch based on their {@link DeliveryChannel channel}.
     * If the DeliveryChannel is not configured with suitable value, batch will be
     * invalidated.
     *
     * @param programMessages the ProgramMessage.
     */
    BatchResponseStatus sendMessages(List<ProgramMessage> programMessages);

    void sendMessagesAsync(List<ProgramMessage> programMessages);

    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------

    ProgramMessage getProgramMessage(long id);

    ProgramMessage getProgramMessage(String uid);

    List<ProgramMessage> getAllProgramMessages();

    List<ProgramMessage> getProgramMessages(ProgramMessageQueryParams params);

    // -------------------------------------------------------------------------
    // Save OR Update
    // -------------------------------------------------------------------------

    long saveProgramMessage(ProgramMessage programMessage);
    
    void updateProgramMessage(ProgramMessage programMessage);

    // -------------------------------------------------------------------------
    // Delete
    // -------------------------------------------------------------------------

    void deleteProgramMessage(ProgramMessage programMessage);
}
