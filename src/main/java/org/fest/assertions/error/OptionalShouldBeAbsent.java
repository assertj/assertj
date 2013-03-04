package org.fest.assertions.error;

import com.google.common.base.Optional;

/**
 * Creates an error message indicating that an Optional which should be absent is actually present
 * 
 * @author Kornel Kiełczewski
 * @author Joel Costigliola
 */
public final class OptionalShouldBeAbsent extends BasicErrorMessageFactory {

  public static <T> ErrorMessageFactory shouldBeAbsent(final Optional<T> actual) {
    return new OptionalShouldBeAbsent("Expecting Optional to contain nothing (absent Optional) but contained <%s>",
        actual.get());
  }

  private OptionalShouldBeAbsent(final String format, final Object... arguments) {
    super(format, arguments);
  }

}
