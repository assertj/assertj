package org.fest.assertions.api;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.error.OptionalShouldBeAbsent.shouldBeAbsent;
import static org.fest.assertions.error.OptionalShouldBePresent.shouldBePresent;
import static org.fest.assertions.error.OptionalShouldBePresentWithValue.shouldBePresentWithValue;

import com.google.common.base.Optional;

import org.fest.assertions.internal.Failures;
import org.fest.assertions.internal.Objects;
import org.fest.util.VisibleForTesting;

/**
 * 
 * Assertions for guava {@link Optional}.
 * <p>
 * To create an instance of this class, invoke <code>{@link GUAVA#assertThat(Optional)}</code>
 * <p>
 * 
 * @param <T> the type of elements of the tested Optional value
 * 
 * @author Kornel Kiełczewski
 */
public final class OptionalAssert<T> extends AbstractAssert<OptionalAssert<T>, Optional<T>> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  protected OptionalAssert(final Optional<T> actual) {
    super(actual, OptionalAssert.class);
  }

  /**
   * Verifies that the actual {@link Optional} contains the given value.<br>
   * <p>
   * Example :
   * 
   * <pre>
   * Optional&lt;String&gt; optional = Optional.of(&quot;Test&quot;);
   * assertThat(optional).hasValue(&quot;Test&quot;);
   * </pre>
   * 
   * @param value the value to look for in actual {@link Optional}.
   * @return this {@link OptionalAssert} for assertions chaining.
   * 
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains nothing or does not have the given value.
   */
  public OptionalAssert<T> hasValue(final T value) { // TODO rename to contains
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isPresent()) {
      throw failures.failure(info, shouldBePresentWithValue(actual, value));
    }
    assertThat(actual.get()).isEqualTo(value);
    return this;
  }

  /**
   * Verifies that the actual {@link Optional} contained instance is absent/null (ie. not {@link Optional#isPresent()}).<br>
   * <p>
   * Example :
   * 
   * <pre>
   * Optional&lt;String&gt; optional = Optional.absent();
   * assertThat(optional).isAbsent();
   * </pre>
   * 
   * @return this {@link OptionalAssert} for assertions chaining.
   * 
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains a (non-null) instance.
   */
  public OptionalAssert<T> isAbsent() {
    Objects.instance().assertNotNull(info, actual);
    if (actual.isPresent()) {
      throw failures.failure(info, shouldBeAbsent(actual));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link Optional} contains a (non-null) instance.<br>
   * <p>
   * Example :
   * 
   * <pre>
   * Optional&lt;String&gt; optional = Optional.of(&quot;value&quot;);
   * assertThat(optional).isPresent();
   * </pre>
   * 
   * @return this {@link OptionalAssert} for assertions chaining.
   * 
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains a null instance.
   */
  public OptionalAssert<T> isPresent() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isPresent()) {
      throw failures.failure(info, shouldBePresent(actual));
    }
    return this;
  }

}
