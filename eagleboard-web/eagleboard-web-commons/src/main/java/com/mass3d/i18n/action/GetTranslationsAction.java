package com.mass3d.i18n.action;

import com.opensymphony.xwork2.ActionSupport;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.util.LocaleUtils;
import com.mass3d.util.TranslationUtils;

import java.util.Locale;
import java.util.Map;

import static com.mass3d.common.IdentifiableObjectUtils.CLASS_ALIAS;

public class GetTranslationsAction 
    extends ActionSupport
{
    private String className;

    private String objectUid;

    private String loc;

    private Map<String, String> translations;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IdentifiableObjectManager identifiableObjectManager;

    public void setIdentifiableObjectManager( IdentifiableObjectManager identifiableObjectManager )
    {
        this.identifiableObjectManager = identifiableObjectManager;
    }
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    public void setClassName( String className )
    {
        this.className = className;
    }

    public void setObjectUid( String objectUid )
    {
        this.objectUid = objectUid;
    }

    public void setLoc( String locale )
    {
        this.loc = locale;
    }

    public Map<String, String> getTranslations()
    {
        return translations;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        className = className != null && CLASS_ALIAS.containsKey( className ) ? CLASS_ALIAS.get( className ) : className;
        
        Locale locale = LocaleUtils.getLocale( loc );

        IdentifiableObject object = identifiableObjectManager.getObject( objectUid , className );

        translations = TranslationUtils.convertTranslations( object.getTranslations(), locale );

        return SUCCESS;
    }
}

