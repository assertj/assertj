package org.fest.assertions.error;

import com.google.common.base.Optional;

/**
 * Creates an error message indicating that an Optional which should be absent is present
 * 
 * @author Kornel Kiełczewski
 */
public final class OptionalShouldBeAbsent extends BasicErrorMessageFactory {

  public static <T> ErrorMessageFactory shouldBeAbsent(final Optional<T> actual) {
    return new OptionalShouldBeAbsent("Expecting <%s> to be absent", new Object[] { actual });
  }

  private OptionalShouldBeAbsent(final String format, final Object[] arguments) {
    super(format, arguments);
  }

}
