package com.mass3d.i18n.ui.resourcebundle;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @version $Id: ResourceBundleManager.java 6335 2008-11-20 11:11:26Z larshelg $
 */
public interface ResourceBundleManager
{
    String ID = ResourceBundleManager.class.getName();

    ResourceBundle getSpecificResourceBundle(Class<?> clazz, Locale locale);

    ResourceBundle getSpecificResourceBundle(String clazzName, Locale locale);

    ResourceBundle getGlobalResourceBundle(Locale locale) throws ResourceBundleManagerException;

    List<Locale> getAvailableLocales() throws ResourceBundleManagerException;
}
