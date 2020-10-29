package com.mass3d.help.action;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.mass3d.i18n.locale.LocaleManager;
import com.mass3d.system.help.HelpManager;
import com.mass3d.util.ContextUtils;
import com.mass3d.util.StreamActionSupport;

public class GetHelpItemsAction
    extends StreamActionSupport
{
    private LocaleManager localeManager;

    public void setLocaleManager( LocaleManager localeManager )
    {
        this.localeManager = localeManager;
    }

    @Override
    protected String execute( HttpServletResponse response, OutputStream out )
        throws Exception
    {
        HelpManager.getHelpItems( out, localeManager.getCurrentLocale() );

        return SUCCESS;
    }

    @Override
    protected String getContentType()
    {
        return ContextUtils.CONTENT_TYPE_HTML;
    }

    @Override
    protected String getFilename()
    {
        return "help.html";
    }
}
