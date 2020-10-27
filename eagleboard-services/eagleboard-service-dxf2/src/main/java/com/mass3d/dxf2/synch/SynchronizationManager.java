package com.mass3d.dxf2.synch;

import com.mass3d.dxf2.importsummary.ImportSummary;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.webmessage.WebMessageParseException;

public interface SynchronizationManager
{
    /**
     * Executes data value push to remote server.
     *
     * @return an {@link ImportSummary}.
     */
    ImportSummary executeDataValuePush() throws WebMessageParseException;

    /**
     * Executes a meta data pull operation from remote server.
     *
     * @param url the URL to the remote server.
     * @return an {@link ImportReport}.
     */
    ImportReport executeMetadataPull(String url);

    /**
     * Indicates the availability status of the remote server.
     *
     * @return the {@link AvailabilityStatus} of the remote server.
     */
    AvailabilityStatus isRemoteServerAvailable();
}
