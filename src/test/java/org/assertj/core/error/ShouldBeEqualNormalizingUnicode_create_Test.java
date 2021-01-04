package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEqualNormalizingUnicode.shouldBeEqualNormalizingUnicode;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

/**
 * Tests for
 * <code>{@link org.assertj.core.error.ShouldBeEqualNormalizingUnicode#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Julieta Navarro
 */
public class ShouldBeEqualNormalizingUnicode_create_Test {
  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeEqualNormalizingUnicode("\u00C4", "\u0041","Ä", "A");
    // WHEN
    String message = factory.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                    "Expecting:%n  " +
                                    "<\"\u00C4\">%n" +
                                    "to be equal to:%n  " +
                                    "<\"\u0041\">%non their canonical form.%n" +
                                    "The normalized strings should be equal.%nNormalized actual:%n  " +
                                    "<\"Ä\">" +
                                    "%nNormalized expected:%n  " +
                                    "<\"A\">"));
  }
}
