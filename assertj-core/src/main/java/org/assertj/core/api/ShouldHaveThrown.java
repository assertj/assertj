package org.assertj.core.api;

import java.util.Collection;
import java.util.List;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * Creates an error message indicating that an assertion that verifies that some {@link Throwable} was expected
 * to be thrown, but actually wasn't
 *
 * @author Mikhail Polivakha
 */
public class ShouldHaveThrown extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveThrown(Class<? extends Throwable> throwable) {
    return new ShouldHaveThrown(List.of(throwable));
  }

  /**
   * Creates a new <code>{@link ShouldHaveThrown}</code>.
   *
   * @param throwables arguments referenced by the format specifiers in the format string.
   */
  public ShouldHaveThrown(Collection<Class<? extends Throwable>> throwables) {
    super(errorMessageTemplate(), throwables);
  }

  private static String errorMessageTemplate() {
    return "%nExpecting code block to raise a Throwable with any type below:%n" +
           "  %s%n" +
           "but nothing was thrown";
  }

}
