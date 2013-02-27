package org.fest.assertions.error;

import com.google.common.base.Optional;

/**
 * @author Kornel Kiełczewski
 */
public final class OptionalShouldBePresent extends BasicErrorMessageFactory {

  public static <T> ErrorMessageFactory shouldBePresent(final Optional<T> actual) {
    return new OptionalShouldBePresent("Expecting <%s> to be present", new Object[] { actual });
  }

  private OptionalShouldBePresent(final String format, final Object[] arguments) {
    super(format, arguments);
  }

}
