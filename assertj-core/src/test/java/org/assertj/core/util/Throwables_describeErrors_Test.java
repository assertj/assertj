/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.util.Throwables.describeErrors;

import java.util.List;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Throwables#describeErrors(List)}.
 *
 * @author chanwonlee
 */
class Throwables_describeErrors_Test {

  private static final int DEFAULT_MAX_STACK_TRACE_ELEMENTS = StandardRepresentation.getMaxStackTraceElementsDisplayed();

  @AfterEach
  void afterEach() {
    // restore default value
    StandardRepresentation.setMaxStackTraceElementsDisplayed(DEFAULT_MAX_STACK_TRACE_ELEMENTS);
  }

  @Test
  void should_honor_maxStackTraceElementsDisplayed_setting_when_exception_has_a_cause() {
    // GIVEN
    StandardRepresentation.setMaxStackTraceElementsDisplayed(10);
    var cause = new RuntimeException("cause message");
    var error = new RuntimeException("error message", cause);
    // WHEN
    List<String> descriptions = describeErrors(List.of(error));
    // THEN
    then(descriptions).singleElement(STRING)
                      .contains("cause first 10 stack trace elements:");
    then(countStackTraceElements(descriptions.get(0))).isEqualTo(StandardRepresentation.getMaxStackTraceElementsDisplayed());
  }

  @Test
  void should_honor_maxStackTraceElementsDisplayed_setting_when_exception_has_no_cause() {
    // GIVEN
    var error = new RuntimeException("error message without cause");
    // WHEN
    List<String> descriptions = describeErrors(List.of(error));
    // THEN
    then(descriptions).singleElement(STRING)
                      .startsWith(
                                  """
                                      error message without cause
                                      first 3 stack trace elements:
                                      """)
                      .contains("should_honor_maxStackTraceElementsDisplayed_setting_when_exception_has_no_cause(Throwables_describeErrors_Test.java:60)");
    then(countStackTraceElements(descriptions.get(0))).isEqualTo(StandardRepresentation.getMaxStackTraceElementsDisplayed());
  }

  private static long countStackTraceElements(String description) {
    return description.lines()
                      .filter(line -> line.trim().startsWith("at "))
                      .count();
  }

}
