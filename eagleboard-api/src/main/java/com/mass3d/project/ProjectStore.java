package com.mass3d.project;

import com.mass3d.common.IdentifiableObjectStore;

public interface ProjectStore
    extends IdentifiableObjectStore<Project> {

  String ID = ProjectStore.class.getName();

}
