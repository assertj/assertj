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
package org.assertj.tests.core.groovy

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.assertj.core.api.BDDAssertions.then
import static org.assertj.core.presentation.StandardRepresentation.*

/**
 * @author Edgar Asatryan
 */
class StandardRepresentation_using_GString_Test {

  @BeforeEach
  void setUp() {
    resetDefaults();
  }

  @AfterEach
  void afterEachTest() {
    removeAllRegisteredFormatters()
    resetDefaults();
  }

  @Test
  void should_return_string_representation_of_GString() {
    // GIVEN
    def point = new Point(x: 1, y: 2)
    // WHEN
    registerFormatterForType(Point) { "($it.x, $it.y)" }
    // THEN
    then(STANDARD_REPRESENTATION.toStringOf(point)).isEqualTo("(1, 2)")
  }

  private static class Point {
    int x
    int y
  }

}
