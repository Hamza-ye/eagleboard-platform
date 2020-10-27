package com.mass3d.i18n;

import java.util.Locale;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.i18n.locale.I18nLocale;

public interface I18nLocaleStore
    extends IdentifiableObjectStore<I18nLocale>
{
    I18nLocale getI18nLocaleByLocale(Locale locale);
}
