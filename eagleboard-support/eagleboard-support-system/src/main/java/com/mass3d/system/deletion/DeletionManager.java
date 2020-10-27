package com.mass3d.system.deletion;

public interface DeletionManager {

  String ID = DeletionManager.class.getName();

  void execute(Object object);
}
