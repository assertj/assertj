package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that two {@code CharSequence}s are equal,
 * on their canonical form relying on {@link java.text.Normalizer}, failed.
 *
 * @author Julieta Navarro
 */
public class ShouldBeEqualNormalizingUnicode extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link org.assertj.core.error.ShouldBeEqualNormalizingUnicode}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param normalizedActual the normalized actual value in the failed assertion.
   * @param normalizedExpected the normalized expected value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualNormalizingUnicode(CharSequence actual, CharSequence expected, CharSequence normalizedActual, CharSequence normalizedExpected) {
    return new ShouldBeEqualNormalizingUnicode(actual, expected, normalizedActual, normalizedExpected);
  }

  private ShouldBeEqualNormalizingUnicode(CharSequence actual, CharSequence expected, CharSequence normalizedActual, CharSequence normalizedExpected) {
    super("%nExpecting:%n  <%s>%nto be equal to:%n  <%s>%non their canonical form.%nThe normalized strings should be equal.%nNormalized actual:%n  <%s>%nNormalized expected:%n  <%s>", actual, expected, normalizedActual, normalizedExpected);
  }
}
