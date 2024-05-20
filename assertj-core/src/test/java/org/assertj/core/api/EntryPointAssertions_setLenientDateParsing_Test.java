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

import java.text.DateFormat;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions setLenientDateParsing method")
class EntryPointAssertions_setLenientDateParsing_Test extends EntryPointAssertionsBaseTest {

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    AbstractDateAssert.setLenientDateParsing(false);
  }

  @ParameterizedTest
  @MethodSource("setLenientDateParsingFunctions")
  void should_setLenientDateParsing(Consumer<Boolean> setLenientDateParsingFunction) {
    // WHEN
    setLenientDateParsingFunction.accept(true);
    // THEN
    then(AbstractDateAssert.defaultDateFormats()).allMatch(DateFormat::isLenient);
  }

  private static Stream<Consumer<Boolean>> setLenientDateParsingFunctions() {
    return Stream.of(Assertions::setLenientDateParsing,
                     BDDAssertions::setLenientDateParsing,
                     withAssertions::setLenientDateParsing);
  }

}
