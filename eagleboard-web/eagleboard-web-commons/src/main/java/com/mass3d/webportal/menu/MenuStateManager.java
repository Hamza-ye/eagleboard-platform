package com.mass3d.webportal.menu;

/**
 * @version $Id: MenuStateManager.java 2869 2007-02-20 14:26:09Z andegje $
 */
public interface MenuStateManager
{
    String ID = MenuStateManager.class.getName();

    MenuState getMenuState();

    void setMenuState(MenuState menuState);
}
