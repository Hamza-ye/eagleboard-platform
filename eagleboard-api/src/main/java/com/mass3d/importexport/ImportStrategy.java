package com.mass3d.importexport;

public enum ImportStrategy
{
  CREATE,
  UPDATE,
  CREATE_AND_UPDATE,
  DELETE,
  SYNC,

  NEW_AND_UPDATES,
  NEW,
  UPDATES,
  DELETES;

  public boolean isCreate()
  {
    return this == NEW || this == CREATE;
  }

  public boolean isUpdate()
  {
    return this == UPDATES || this == UPDATE;
  }

  public boolean isCreateAndUpdate()
  {
    return this == NEW_AND_UPDATES || this == CREATE_AND_UPDATE;
  }

  public boolean isDelete()
  {
    return this == DELETE || this == DELETES;
  }

  public boolean isSync()
  {
    return this == SYNC;
  }
}
