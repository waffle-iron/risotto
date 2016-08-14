package com.dijon.binding;

import com.dijon.instantiation.DependencyInjectionInstantiator;
import com.dijon.instantiation.InstantiatorFactory;

public class ClassBinding<T> extends InstantiatableBinding<T> {
  private final Class<? extends T> targetClass;

  public ClassBinding(Binding<T> binding, Class<? extends T> targetClass) {
    super(binding);

    this.targetClass = targetClass;

    instantiator = InstantiatorFactory
        .decorateWithDefaultInstantiator(new DependencyInjectionInstantiator<>(targetClass));
  }

  public Class<? extends T> getTargetClass() {
    return targetClass;
  }
}
