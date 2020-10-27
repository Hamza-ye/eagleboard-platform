package com.mass3d.dbms;

import java.util.List;

public interface DbmsManager {

  String ID = DbmsManager.class.getName();

  void emptyDatabase();

  void clearSession();

  void flushSession();

  void emptyTable(String table);

  boolean tableExists(String tableName);

  List<List<Object>> getTableContent(String table);
}
