package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link CharSequence}
 * does not contain whitespace characters.
 */
public class ShouldNotStartWithWhitespace extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotStartWithWhitespace}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotStartWithWhitespace(CharSequence actual) {
    return new ShouldNotStartWithWhitespace(actual);
  }

  private ShouldNotStartWithWhitespace(Object actual) {
    super("%n" +
          "Expecting string not to start with whitespace but found one, string was:%n" +
          "  %s", actual);
  }
}
