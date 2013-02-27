package org.fest.assertions.error;

import com.google.common.base.Optional;

/**
 * 
 * Creates an error message indicating that an Optional should contain an expected value
 * 
 * @author Kornel Kiełczewski
 */
public final class OptionalShouldBePresentWithValue extends BasicErrorMessageFactory {

  public static <T> ErrorMessageFactory shouldBePresentWithValue(final Optional<T> actual, final T value) {
    return new OptionalShouldBePresentWithValue("Expecting <%s> to have value <%s>", new Object[] { actual, value });
  }

  private OptionalShouldBePresentWithValue(final String format, final Object[] arguments) {
    super(format, arguments);
  }

}
