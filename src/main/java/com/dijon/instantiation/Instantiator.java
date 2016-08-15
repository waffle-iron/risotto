package com.dijon.instantiation;

import com.dijon.dependency.Dependency;

import java.util.List;

public interface Instantiator<T> {
  T getInstance();

  Instantiator<T> getBaseInstantiator();

  List<Dependency<?>> getImmediateDependencies();
}