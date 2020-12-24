package org.assertj.core.error;
/**
 * Creates an error message indicating that an assertion that verifies that two {@code CharSequence}s are not equal,
 * on their canonical form relying on {@link java.text.Normalizer}.
 *
 * @author Julieta Navarro
 */
public class ShouldNotBeEqualNormalizingUnicode extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link org.assertj.core.error.ShouldBeEqualNormalizingUnicode}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeEqualNormalizingUnicode(CharSequence actual, CharSequence expected) {
    return new ShouldNotBeEqualNormalizingUnicode(actual, expected);
  }

  private ShouldNotBeEqualNormalizingUnicode(CharSequence actual, CharSequence expected) {
    super("%nExpecting:%n  <%s>%nnot to be equal to:%n  <%s>%non their canonical form", actual, expected);
  }
}
