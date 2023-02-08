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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Consumer;
import java.util.stream.Stream;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions setMaxStackTraceElementsDisplayed method")
class EntryPointAssertions_setMaxStackTraceElementsDisplayed_Test extends EntryPointAssertionsBaseTest {

  private static final int DEFAULT_MAX_STACK_TRACE_ELEMENTS_DISPLAYED = StandardRepresentation.getMaxStackTraceElementsDisplayed();

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    StandardRepresentation.setMaxStackTraceElementsDisplayed(DEFAULT_MAX_STACK_TRACE_ELEMENTS_DISPLAYED);
  }

  @ParameterizedTest
  @MethodSource("setMaxStackTraceElementsDisplayedFunctions")
  void should_set_maxStackTraceElementsDisplayed_value(Consumer<Integer> setMaxStackTraceElementsDisplayedFunction) {
    // GIVEN
    int maxStackTraceElementsDisplayed = DEFAULT_MAX_STACK_TRACE_ELEMENTS_DISPLAYED + 1;
    // WHEN
    setMaxStackTraceElementsDisplayedFunction.accept(maxStackTraceElementsDisplayed);
    // THEN
    then(StandardRepresentation.getMaxStackTraceElementsDisplayed()).isEqualTo(maxStackTraceElementsDisplayed);
  }

  private static Stream<Consumer<Integer>> setMaxStackTraceElementsDisplayedFunctions() {
    return Stream.of(Assertions::setMaxStackTraceElementsDisplayed,
                     BDDAssertions::setMaxStackTraceElementsDisplayed,
                     withAssertions::setMaxStackTraceElementsDisplayed);
  }

}
