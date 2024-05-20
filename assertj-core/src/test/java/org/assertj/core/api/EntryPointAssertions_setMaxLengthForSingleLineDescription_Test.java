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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EntryPointAssertions_setMaxLengthForSingleLineDescription_Test extends EntryPointAssertionsBaseTest {

  private static final int DEFAULT_MAX_LENGTH_FOR_SINGLE_LINE = StandardRepresentation.getMaxLengthForSingleLineDescription();

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    StandardRepresentation.setMaxLengthForSingleLineDescription(DEFAULT_MAX_LENGTH_FOR_SINGLE_LINE);
  }

  @ParameterizedTest
  @MethodSource("setMaxLengthForSingleLineDescriptionFunctions")
  void should_set_maxLengthForSingleLineDescription_value(Consumer<Integer> setMaxLengthForSingleLineDescriptionFunction) {
    // GIVEN
    int maxLengthForSingleLineDescription = DEFAULT_MAX_LENGTH_FOR_SINGLE_LINE + 1;
    // WHEN
    setMaxLengthForSingleLineDescriptionFunction.accept(maxLengthForSingleLineDescription);
    // THEN
    then(StandardRepresentation.getMaxLengthForSingleLineDescription()).isEqualTo(maxLengthForSingleLineDescription);
  }

  private static Stream<Consumer<Integer>> setMaxLengthForSingleLineDescriptionFunctions() {
    return Stream.of(Assertions::setMaxLengthForSingleLineDescription,
                     BDDAssertions::setMaxLengthForSingleLineDescription,
                     withAssertions::setMaxLengthForSingleLineDescription);
  }

}
