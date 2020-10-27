package com.mass3d.setting;

import com.mass3d.common.GenericStore;

public interface SystemSettingStore
    extends GenericStore<SystemSetting>
{
    /**
     * Returns the {@link SystemSetting} with the given name.
     * <p>
     * Note: This method invocation will occur within a transaction.
     *
     * @param name the system setting name.
     * @return a system setting.
     */
    SystemSetting getByNameTx(String name);

    /**
     * Returns the {@link SystemSetting} with the given name.
     *
     * @param name the system setting name.
     * @return a system setting.
     */
    SystemSetting getByName(String name);
}
