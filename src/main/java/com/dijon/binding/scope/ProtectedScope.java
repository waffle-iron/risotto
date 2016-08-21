package com.dijon.binding.scope;

import com.dijon.Container;
import com.dijon.dependency.Dependency;

public class ProtectedScope extends Scope {
  @Override
  public boolean isImportAllowedTo(Container targetContainer) {
    return false;
  }

  @Override
  public boolean isVisibleTo(Dependency<?> dependency) {
    return true;
  }
}
