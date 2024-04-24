package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link CharSequence}
 * does not contain whitespace characters.
 */
public class ShouldNotEndWithWhitespace extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotStartWithWhitespace}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotEndWithWhitespace(CharSequence actual) {
    return new ShouldNotEndWithWhitespace(actual);
  }

  private ShouldNotEndWithWhitespace(Object actual) {
    super("%n" +
          "Expecting string not to end with whitespace but found one, string was:%n" +
          "  %s", actual);
  }
}
