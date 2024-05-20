/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldHaveSuppressedException.shouldHaveSuppressedException;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Arrays.array;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveSuppressedException_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    Throwable actual = new Throwable();
    IllegalArgumentException suppressedException1 = new IllegalArgumentException("invalid arg");
    actual.addSuppressed(suppressedException1);
    NullPointerException suppressedException2 = new NullPointerException("null arg");
    actual.addSuppressed(suppressedException2);
    ErrorMessageFactory factory = shouldHaveSuppressedException(actual, new IllegalArgumentException("foo"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  %s%n" +
                                   "to have a suppressed exception with the following type and message:%n" +
                                   "  \"java.lang.IllegalArgumentException\" / \"foo\"%n" +
                                   "but could not find any in actual's suppressed exceptions:%n" +
                                   "  %s",
                                   STANDARD_REPRESENTATION.toStringOf(actual),
                                   STANDARD_REPRESENTATION.toStringOf(array(suppressedException1, suppressedException2))));
  }
}
