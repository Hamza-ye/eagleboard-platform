package com.mass3d.webportal.menu;

import com.mass3d.util.SessionUtils;

/**
 * @version $Id: SessionMenuStateManager.java 4827 2008-04-08 12:49:29Z larshelg $
 */
public class SessionMenuStateManager
    implements MenuStateManager
{
    private static final String SESSION_KEY_MENU_STATE = "dhis-web-commons-menu-state";

    // -------------------------------------------------------------------------
    // MenuStateManager implementation
    // -------------------------------------------------------------------------

    @Override
    public MenuState getMenuState()
    {
        return (MenuState) SessionUtils.getSessionVar( SESSION_KEY_MENU_STATE );
    }

    @Override
    public void setMenuState( MenuState menuState )
    {
        SessionUtils.setSessionVar( SESSION_KEY_MENU_STATE, menuState );
    }
}
