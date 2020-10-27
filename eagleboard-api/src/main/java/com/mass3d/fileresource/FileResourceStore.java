package com.mass3d.fileresource;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import org.joda.time.DateTime;

public interface FileResourceStore extends IdentifiableObjectStore<FileResource>
{
    List<FileResource> getExpiredFileResources(DateTime expires);

    List<FileResource> getAllUnProcessedImages();
}
