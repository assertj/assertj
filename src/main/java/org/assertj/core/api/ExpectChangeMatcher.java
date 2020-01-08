package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

public class ExpectChangeMatcher<T, R> {

  private final Runnable runnable;
  private final T object;
  private final Function<T, R> attribute;

  public ExpectChangeMatcher(Runnable runnable, T object, Function<T, R> attribute) {
    this.runnable = runnable;
    this.object = object;
    this.attribute = attribute;
  }

  public ExpectChangeMatcher<T, R> from(R value) {
    assertThat(object).extracting(attribute).isEqualTo(value);
    return this;
  }

  public void to(R value) {
    runnable.run();
    assertThat(object).extracting(attribute).isEqualTo(value);
  }

  public void by(R value) {
    if (value instanceof Integer) {
      int before = (int) attribute.apply(object);
      runnable.run();
      int after = (int) attribute.apply(object);
      assertThat(Math.abs(after - before)).isEqualTo(value);
    } else {
      // other numeric types should be handled
      throw new IllegalArgumentException();
    }
  }

}
