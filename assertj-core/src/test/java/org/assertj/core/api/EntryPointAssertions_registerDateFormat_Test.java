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
import java.text.SimpleDateFormat;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions registerCustomDateFormat method")
class EntryPointAssertions_registerDateFormat_Test extends EntryPointAssertionsBaseTest {

  @BeforeEach
  void beforeEachTest() {
    // reset to the default value to avoid side effects on the other tests
    AbstractDateAssert.useDefaultDateFormatsOnly();
  }

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    AbstractDateAssert.useDefaultDateFormatsOnly();
  }

  @ParameterizedTest
  @MethodSource("registerCustomDateFormatFunctions")
  void should_register_DateFormat(Consumer<DateFormat> registerCustomDateFormatFunction) {
    // GIVEN
    DateFormat dateFormat = new SimpleDateFormat();
    // WHEN
    registerCustomDateFormatFunction.accept(dateFormat);
    // THEN
    then(AbstractDateAssert.userDateFormats.get()).hasSize(1);
  }

  private static Stream<Consumer<DateFormat>> registerCustomDateFormatFunctions() {
    return Stream.of(Assertions::registerCustomDateFormat,
                     BDDAssertions::registerCustomDateFormat,
                     withAssertions::registerCustomDateFormat);
  }

  @ParameterizedTest
  @MethodSource("registerCustomDateFormatAsStringFunctions")
  void should_register_DateFormat_as_string(Consumer<String> registerCustomDateFormatFunction) {
    // GIVEN
    String dateFormatAsString = "yyyyddMM";
    // WHEN
    registerCustomDateFormatFunction.accept(dateFormatAsString);
    // THEN
    then(AbstractDateAssert.userDateFormats.get()).hasSize(1);
  }

  private static Stream<Consumer<String>> registerCustomDateFormatAsStringFunctions() {
    return Stream.of(Assertions::registerCustomDateFormat,
                     BDDAssertions::registerCustomDateFormat,
                     withAssertions::registerCustomDateFormat);
  }
}
