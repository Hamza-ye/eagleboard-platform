package com.mass3d.system.database;

/**
 * @version $Id$
 */
public interface DatabaseInfoProvider
{
    String ID = DatabaseInfoProvider.class.getName();

    DatabaseInfo getDatabaseInfo();

    boolean isInMemory();
}
