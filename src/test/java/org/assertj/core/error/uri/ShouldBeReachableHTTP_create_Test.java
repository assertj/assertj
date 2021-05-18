package org.assertj.core.error.uri;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldBeReachableHTTP.shouldBeReachableHTTP;

import java.io.IOException;
import java.net.URL;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("shouldBeReachableHTTP")
public class ShouldBeReachableHTTP_create_Test {

  @Test
  void should_create_error_message() throws IOException {
    // GIVEN
    URL actual = new URL("http://localhost:8080/test");
    // WHEN
    String error = shouldBeReachableHTTP(actual).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting %n" +
                                 "  <http://localhost:8080/test>%n" +
                                 "to be reachable"));
  }
}
