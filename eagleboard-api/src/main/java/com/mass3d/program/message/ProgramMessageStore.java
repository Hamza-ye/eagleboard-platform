package com.mass3d.program.message;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface ProgramMessageStore
    extends IdentifiableObjectStore<ProgramMessage>
{
    List<ProgramMessage> getProgramMessages(ProgramMessageQueryParams params);
        
    List<ProgramMessage> getAllOutboundMessages();

    boolean exists(String uid);
}
