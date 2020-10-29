package com.mass3d.i18n.action;

import com.opensymphony.xwork2.Action;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.i18n.I18nLocaleService;
import com.mass3d.user.UserSettingKey;
import com.mass3d.user.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.mass3d.common.IdentifiableObjectUtils.CLASS_ALIAS;

/**
 * @version $Id$
 * @modifier Dang Duy Hieu
 * @since 2010-03-24
 */
public class I18nAction
    implements Action
{
    private String className;

    private String uid;

    private String returnUrl;

    private String message;

    private Locale currentLocale;
    
    private List<Locale> availableLocales = new ArrayList<>();
    
    private Map<String, String> translations = new Hashtable<>();

    private Map<String, String> referenceTranslations = new Hashtable<>();
    
    private List<String> propertyNames = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserSettingService userSettingService;

    public void setUserSettingService( UserSettingService userSettingService )
    {
        this.userSettingService = userSettingService;
    }

    private IdentifiableObjectManager identifiableObjectManager;

    public void setIdentifiableObjectManager( IdentifiableObjectManager identifiableObjectManager )
    {
        this.identifiableObjectManager = identifiableObjectManager;
    }

    @Autowired
    private I18nLocaleService i18nLocaleService;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    public void setClassName( String className )
    {
        this.className = className;
    }

    public void setUid( String uid )
    {
        this.uid = uid;
    }

    public void setReturnUrl( String returnUrl )
    {
        this.returnUrl = returnUrl;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    public String getClassName()
    {
        return className;
    }

    public String getUid()
    {
        return uid;
    }

    public String getReturnUrl()
    {
        return returnUrl;
    }

    public String getMessage()
    {
        return message;
    }

    public Locale getCurrentLocale()
    {
        return currentLocale;
    }

    public List<Locale> getAvailableLocales()
    {
        return availableLocales;
    }

    public Map<String, String> getReferenceTranslations()
    {
        return referenceTranslations;
    }

    public Map<String, String> getTranslations()
    {
        return translations;
    }

    public List<String> getPropertyNames()
    {
        return propertyNames;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        className = className != null && CLASS_ALIAS.containsKey( className ) ? CLASS_ALIAS.get( className ) : className;
        
        currentLocale = (Locale) userSettingService.getUserSetting( UserSettingKey.DB_LOCALE );
        
        availableLocales = i18nLocaleService.getAllLocales();

        IdentifiableObject object = identifiableObjectManager.getObject( uid, className );

//        translations = TranslationUtils.convertTranslations( object.getTranslations(), currentLocale );
//
//        referenceTranslations = TranslationUtils.getObjectPropertyValues( object );
//
//        propertyNames = TranslationUtils.getObjectPropertyNames( object );

        return SUCCESS;
    }
}
