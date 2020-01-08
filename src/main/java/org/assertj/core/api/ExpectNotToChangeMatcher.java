package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

public class ExpectNotToChangeMatcher {

  private ExpectNotToChangeMatcher() {
  }

  public static <T> void notToChange(Runnable runnable, T object, Function<T, ?> attribute) {
    Object before = attribute.apply(object);
    runnable.run();
    Object after = attribute.apply(object);
    assertThat(before).isEqualTo(after);
  }

}
