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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePropertyOrFieldWithValue.shouldHavePropertyOrFieldWithValue;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ShouldHavePropertyOrFieldWithValue create")
class ShouldHavePropertyOrFieldWithValue_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    Object actual = 2;
    // WHEN
    String message = shouldHavePropertyOrFieldWithValue(actual, "foo", "1", "2").create(new TestDescription("TEST"),
                                                                                        STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  2%n" +
                                   "to have a property or a field named \"foo\" with value%n"
                                   + "  \"1\"%n"
                                   + "but value was:%n"
                                   + "  \"2\"%n" +
                                   "(static and synthetic fields are ignored)"));
  }
}
