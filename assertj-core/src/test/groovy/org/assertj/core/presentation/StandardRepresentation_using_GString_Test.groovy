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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.presentation

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat

/**
 * @author Edgar Asatryan
 */
class StandardRepresentation_using_GString_Test extends AbstractBaseRepresentationTest {
  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @AfterEach
  void afterEachTest() {
    StandardRepresentation.removeAllRegisteredFormatters()
  }

  @Test
  void should_return_string_representation_of_GString() {
    // GIVEN
    StandardRepresentation.registerFormatterForType(Point) { "($it.x, $it.y)" }
    def point = new Point()
    point.x = 1
    point.y = 2
    // WHEN
    def actual = STANDARD_REPRESENTATION.toStringOf(point)
    // THEN
    assertThat(actual).isEqualTo("(1, 2)")
  }

  private static class Point {
    int x
    int y
  }
}
