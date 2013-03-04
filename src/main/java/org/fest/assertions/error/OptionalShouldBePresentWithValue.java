package org.fest.assertions.error;

import com.google.common.base.Optional;

/**
 * 
 * Creates an error message indicating that an Optional should contain an expected value
 * 
 * @author Kornel Kiełczewski
 * @author Joel Costigliola
 */
public final class OptionalShouldBePresentWithValue extends BasicErrorMessageFactory {

  public static <T> ErrorMessageFactory shouldBePresentWithValue(final Optional<T> actual, final Object value) {
    return new OptionalShouldBePresentWithValue("\nExpecting Optional to contain value \n<%s>\n but contained \n<%s>",
        value, actual.get());
  }

  public static <T> ErrorMessageFactory shouldBePresentWithValue(final Object value) {
    return new OptionalShouldBePresentWithValue(
        "Expecting Optional to contain <%s> but contained nothing (absent Optional)", value);
  }

  private OptionalShouldBePresentWithValue(final String format, final Object... arguments) {
    super(format, arguments);
  }

}
