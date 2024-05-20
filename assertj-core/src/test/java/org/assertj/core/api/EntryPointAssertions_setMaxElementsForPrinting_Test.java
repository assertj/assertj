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
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions setMaxElementsForPrinting method")
class EntryPointAssertions_setMaxElementsForPrinting_Test extends EntryPointAssertionsBaseTest {

  private static final int DEFAULT_MAX_ELEMENTS_FOR_PRINTING = StandardRepresentation.getMaxElementsForPrinting();

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    StandardRepresentation.setMaxElementsForPrinting(DEFAULT_MAX_ELEMENTS_FOR_PRINTING);
  }

  @ParameterizedTest
  @MethodSource("setMaxElementsForPrintingFunctions")
  void should_set_maxElementsForPrinting_value(Consumer<Integer> setMaxElementsForPrintingFunction) {
    // GIVEN
    int maxElementsForPrinting = DEFAULT_MAX_ELEMENTS_FOR_PRINTING + 1;
    // WHEN
    setMaxElementsForPrintingFunction.accept(maxElementsForPrinting);
    // THEN
    then(StandardRepresentation.getMaxElementsForPrinting()).isEqualTo(maxElementsForPrinting);
  }

  private static Stream<Consumer<Integer>> setMaxElementsForPrintingFunctions() {
    return Stream.of(Assertions::setMaxElementsForPrinting,
                     BDDAssertions::setMaxElementsForPrinting,
                     withAssertions::setMaxElementsForPrinting);
  }

}
