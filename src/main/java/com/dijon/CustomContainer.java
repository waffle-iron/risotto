package com.dijon;

import com.dijon.binding.InstanceBinding;
import com.dijon.binding.InstantiatableBinding;
import com.dijon.dependency.AnnotatedDependency;
import com.dijon.dependency.Dependency;
import com.dijon.dependency.NamedDependency;
import com.dijon.exception.DependencyResolutionFailedException;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class CustomContainer extends AbstractContainer {
  protected final AbstractContainer parentContainer;

  private final String name;

  private List<InstanceBinding<?>> bindingList;

  private List<Dependency<?>> dependencyList;

  public CustomContainer(AbstractContainer parentContainer, String name) {
    this.parentContainer = parentContainer;

    this.name = name;
  }


  public <T> Optional<T> getInstance(Class<T> clazz) {
    Dependency<T> dependency = new Dependency<>(clazz);

    return returnInstance(dependency);
  }

  public <T> Optional<T> getInstance(Class<T> clazz, String name) {
    NamedDependency<T> dependency = new NamedDependency<T>(clazz, name);

    return returnInstance(dependency);
  }

  public <T> Optional<T> getInstance(Class<T> clazz, Class<? extends Annotation> annotation) {
    AnnotatedDependency<T> dependency = new AnnotatedDependency<T>(clazz, annotation);

    return returnInstance(dependency);
  }

  @SuppressWarnings("unchecked")
  private <T> Optional<T> returnInstance(Dependency<T> dependency) {
    Optional<InstantiatableBinding<?>> bindingOptional = resolve(dependency);

    if (!bindingOptional.isPresent()) {
      return Optional.empty();
    }

    InstantiatableBinding<T> binding = (InstantiatableBinding<T>)bindingOptional.get();

    return Optional.of(binding.getInstance());
  }

  /**
   * Configures the contents of the container. By overriding this method, instances and child
   * containers can be added to the container object.
   */
  protected abstract void configure();

  void configureChildren() {

  }

  protected void addBinding(InstanceBinding<?> instanceBinding) {
    bindingList.add(instanceBinding);

    List<Dependency<?>> immediateDependencies = instanceBinding.getImmediateDependencies();

    for (Dependency<?> dependency : immediateDependencies) {
      if (!dependencyList.contains(dependency)) {
        dependencyList.add(dependency);
      }
    }
  }

  protected void performResolution() throws DependencyResolutionFailedException {
    for (Dependency<?> dependency : dependencyList) {
      Optional<InstantiatableBinding<?>> bindingOptional = resolve(dependency);

      if (!bindingOptional.isPresent()) {
        throw new DependencyResolutionFailedException(dependency);
      }

      dependency.setResolvingBinding(bindingOptional.get());
    }
  }

  @Override
  protected Optional<InstantiatableBinding<?>> resolve(Dependency<?> dependency) {
    for (InstantiatableBinding<?> binding : bindingList) {
      if (binding.canResolve(dependency)) {
        return Optional.of(binding);
      }
    }

    return parentContainer.resolve(dependency);
  }

  @Override
  protected CompletableFuture<Optional<InstantiatableBinding<?>>> resolveAsync(
      Dependency<?> dependency) {
    return null;
  }

  public String getName() {
    return name;
  }
}
