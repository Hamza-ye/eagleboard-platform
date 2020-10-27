package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.document.Document;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.fileresource.FileResource;
import com.mass3d.fileresource.FileResourceDomain;
import com.mass3d.fileresource.FileResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentObjectBundleHook extends AbstractObjectBundleHook {

    private static final Pattern URL_PATTERN = Pattern.compile("^https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private IdentifiableObjectManager idObjectManager;

    @Override
    public List<ErrorReport> validate (IdentifiableObject object, ObjectBundle bundle)
    {
        if ( !Document.class.isInstance( object ) )
        {
            return new ArrayList<>();
        }

        List<ErrorReport> errors = new ArrayList<>();

        Document document = (Document) object;

        FileResource fileResource = fileResourceService.getFileResource( document.getUrl() );

        if ( document.getUrl() == null )
        {
            errors.add( new ErrorReport( Document.class, ErrorCode.E4000, "url" ) );
        }
        else if ( document.isExternal() && !URL_PATTERN.matcher( document.getUrl() ).matches() )
        {
            errors.add( new ErrorReport( Document.class, ErrorCode.E4004, "url", document.getUrl() ) );
        }
        else if ( !document.isExternal() && fileResource == null )
        {
            errors.add( new ErrorReport( Document.class, ErrorCode.E4015, "url", document.getUrl() ) );
        }
        else if ( !document.isExternal() && fileResource.isAssigned() )
        {
            errors.add( new ErrorReport( Document.class, ErrorCode.E4016, "url", document.getUrl() ) );
        }

        return errors;
    }

    @Override
    public void postCreate( IdentifiableObject object, ObjectBundle bundle )
    {
        if ( !Document.class.isInstance( object ) )
        {
            return;
        }

        Document document = (Document) object;

        saveDocument( document );
    }

    @Override
    public void postUpdate( IdentifiableObject object, ObjectBundle bundle )
    {
        if ( !Document.class.isInstance( object ) )
        {
            return;
        }

        Document document = (Document) object;

        saveDocument( document );
    }

    private void saveDocument( Document document )
    {
        if ( !document.isExternal() )
        {
            FileResource fileResource = fileResourceService.getFileResource( document.getUrl() );
            fileResource.setDomain( FileResourceDomain.DOCUMENT );
            fileResource.setAssigned( true );
            document.setFileResource( fileResource );
            fileResourceService.updateFileResource( fileResource );
        }

        idObjectManager.save( document );
    }
}
