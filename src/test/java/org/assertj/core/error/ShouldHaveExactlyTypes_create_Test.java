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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveExactlyTypes.elementsTypesDifferAtIndex;
import static org.assertj.core.error.ShouldHaveExactlyTypes.shouldHaveTypes;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveExactlyTypes_create_Test {

  @Test
  void should_display_missing_and_unexpected_elements_types() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveTypes(list("Yoda", 123),
                                                  list(String.class, Double.class),
                                                  list(Double.class),
                                                  list(Integer.class));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual elements:%n"
                                   + "  [\"Yoda\", 123]%n"
                                   + "to have the following types (in this order):%n"
                                   + "  [java.lang.String, java.lang.Double]%n"
                                   + "but there were no actual elements with these types:%n"
                                   + "  [java.lang.Double]%n"
                                   + "and these actual elements types were not expected:%n"
                                   + "  [java.lang.Integer]"));
  }

  @Test
  void should_not_display_missing_elements_types_when_there_are_none() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveTypes(list("Yoda", 123),
                                                  list(String.class, String.class),
                                                  list(),
                                                  list(Integer.class));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual elements:%n"
                                   + "  [\"Yoda\", 123]%n"
                                   + "to have the following types (in this order):%n"
                                   + "  [java.lang.String, java.lang.String]%n"
                                   + "but these actual elements types were not expected:%n"
                                   + "  [java.lang.Integer]"));
  }

  @Test
  void should_not_display_unexpected_elements_types_when_there_are_none() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveTypes(list("Yoda", 123, 456),
                                                  list(String.class, Integer.class, Double.class),
                                                  list(Double.class),
                                                  list());
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual elements:%n"
                                   + "  [\"Yoda\", 123, 456]%n"
                                   + "to have the following types (in this order):%n"
                                   + "  [java.lang.String, java.lang.Integer, java.lang.Double]%n"
                                   + "but there were no actual elements with these types:%n"
                                   + "  [java.lang.Double]"));
  }

  @Test
  void should_display_first_wrong_element_type_when_only_elements_types_order_differs() {
    // GIVEN
    ErrorMessageFactory factory = elementsTypesDifferAtIndex("Luke", Double.class, 1);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "actual element at index 1 does not have the expected type, element was:\"Luke\"%n" +
                                   "actual element type: java.lang.String%n" +
                                   "expected type      : java.lang.Double"));
  }

}
