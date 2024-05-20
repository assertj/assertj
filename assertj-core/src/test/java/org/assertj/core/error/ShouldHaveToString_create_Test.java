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
import static org.assertj.core.error.ShouldHaveToString.shouldHaveToString;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

class ShouldHaveToString_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "green");
    String expectedToString = "Luke the Jedi";
    // WHEN
    String errorMessage = shouldHaveToString(actual, expectedToString).create(new TestDescription("TEST"),
                                                                              STANDARD_REPRESENTATION);
    // THEN
    then(errorMessage).isEqualTo(format("[TEST] %n" +
                                        "Expecting actual's toString() to return:%n" +
                                        "  \"Luke the Jedi\"%n" +
                                        "but was:%n" +
                                        "  \"Yoda the Jedi\""));
  }

}
