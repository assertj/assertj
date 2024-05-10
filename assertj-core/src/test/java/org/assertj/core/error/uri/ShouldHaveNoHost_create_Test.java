/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
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
