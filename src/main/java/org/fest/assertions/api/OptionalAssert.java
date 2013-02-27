package org.fest.assertions.api;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.error.OptionalShouldBeAbsent.shouldBeAbsent;
import static org.fest.assertions.error.OptionalShouldBePresent.shouldBePresent;
import static org.fest.assertions.error.OptionalShouldBePresentWithValue.shouldBePresentWithValue;

import org.fest.assertions.internal.Failures;
import org.fest.util.VisibleForTesting;

import com.google.common.base.Optional;

/**
 * @author Kornel
 */
public final class OptionalAssert<T> extends AbstractAssert<OptionalAssert<T>, Optional<T>> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  public OptionalAssert(final Optional<T> actual) {
    super(actual, OptionalAssert.class);
  }

  public OptionalAssert<T> hasValue(final T value) {

    if (!actual.isPresent()) {
      throw failures.failure(info, shouldBePresentWithValue(actual, value));
    }

    assertThat(value).isEqualTo(actual.get());
    return this;
  }

  public OptionalAssert<T> isAbsent() {
    if (actual.isPresent()) {
      throw failures.failure(info, shouldBeAbsent(actual));
    }
    return this;
  }

  public OptionalAssert<T> isPresent() {
    if (!actual.isPresent()) {
      throw failures.failure(info, shouldBePresent(actual));
    }
    return this;
  }

}
