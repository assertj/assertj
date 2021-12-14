package org.assertj.core.error.uri;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveNoHost.shouldHaveNoHost;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveNoHost_create_Test {

  @Test
  void should_create_error_message_with_URI() throws URISyntaxException {
    // GIVEN
    ErrorMessageFactory underTest = shouldHaveNoHost(new URI("https://example.com"));
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting no host for:%n" +
                                   "  https://example.com%n" +
                                   "but found:%n" +
                                   "  \"example.com\""));
  }

  @Test
  void should_create_error_message_with_URL() throws MalformedURLException {
    // GIVEN
    ErrorMessageFactory underTest = shouldHaveNoHost(new URL("https://example.com"));
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting no host for:%n" +
                                   "  https://example.com%n" +
                                   "but found:%n" +
                                   "  \"example.com\""));
  }

}
