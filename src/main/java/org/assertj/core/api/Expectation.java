package org.assertj.core.api;

import java.util.function.Function;

public class Expectation {

  private final Runnable runnable;

  public Expectation(Runnable runnable) {
    this.runnable = runnable;
  }

  public <T, R> ExpectChangeMatcher<T, R> toChange(T object, Function<T, R> attribute) {
    return new ExpectChangeMatcher<>(runnable, object, attribute);
  }

  public <T> void notToChange(T object, Function<T, ?> attribute) {
    ExpectNotToChangeMatcher.notToChange(runnable, object, attribute);
  }

}
