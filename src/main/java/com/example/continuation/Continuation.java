package com.example.continuation;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Consumer;

public final class Continuation {

  private final Object delegate;

  private static final Class<?> IMPL_CLASS;
  private static final MethodHandle NEW;
  private static final MethodHandle YIELD;
  private static final MethodHandle RUN;
  private static final MethodHandle IS_DONE;

  static {
    try {
      IMPL_CLASS = Class.forName("jdk.internal.vm.Continuation");

      var lookup = MethodHandles.privateLookupIn(IMPL_CLASS, MethodHandles.lookup());
      NEW = lookup.findConstructor(IMPL_CLASS, MethodType.methodType(void.class, Scope.IMPL_CLASS, Runnable.class));
      RUN = lookup.findVirtual(IMPL_CLASS, "run", MethodType.methodType(void.class));
      YIELD = lookup.findStatic(IMPL_CLASS, "yield", MethodType.methodType(boolean.class, Scope.IMPL_CLASS));
      IS_DONE = lookup.findVirtual(IMPL_CLASS, "isDone", MethodType.methodType(boolean.class));

    } catch (Throwable t) {
      throw new ExceptionInInitializerError(t);
    }
  }

  public <T> Continuation(String scopeName, Consumer<Scope<T>> code) {
    this(scope(scopeName), code);
  }

  public <T> Continuation(Scope<T> scope, Consumer<Scope<T>> code) {
    try {
      this.delegate = NEW.invoke(scope.delegate, (Runnable) () -> code.accept(scope));
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public void run() {
    try {
      RUN.invoke(delegate);
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public <T> T next(Scope<T> scope) {
    run();
    return getState(scope);
  }

  public static <T> void yield(Scope<T> scope) {
    try {
      YIELD.invoke(scope.delegate);
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public static <T> void yield(Scope<T> scope, T state) {
    ScopedValue.where(scope.state, state).run(() -> {
      try {
        YIELD.invoke(scope.delegate);
      } catch (Throwable t) {
        throw new RuntimeException(t);
      }
    });
  }

  public static <T> T getState(Scope<T> scope) {
    return scope.state.get();
  }

  public boolean isDone() {
    try {
      return (boolean) IS_DONE.invoke(delegate);
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public static <T> Scope<T> scope(String name) {
    return new Scope<>(name);
  }

  public static final class Scope<T> {

    private final Object delegate;

    private final ScopedValue<T> state;

    private static final Class<?> IMPL_CLASS;
    private static final MethodHandle NEW;

    static {
      try {
        IMPL_CLASS = Class.forName("jdk.internal.vm.ContinuationScope");

        var lookup = MethodHandles.privateLookupIn(IMPL_CLASS, MethodHandles.lookup());
        NEW = lookup.findConstructor(IMPL_CLASS, MethodType.methodType(void.class, String.class));

      } catch (Throwable t) {
        throw new ExceptionInInitializerError(t);
      }
    }

    private Scope(String name) {
      try {
        this.delegate = NEW.invoke(name);
        this.state = ScopedValue.newInstance();
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public String toString() {
      return delegate.toString();
    }
  }
}
