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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions registerFormatterForType method")
class EntryPointAssertions_registerFormatterForType_Test extends EntryPointAssertionsBaseTest {

  @AfterEach
  void afterEachTest() {
    StandardRepresentation.removeAllRegisteredFormatters();
  }

  @ParameterizedTest
  @MethodSource("registerFormatterForTypeFunctions")
  void should_register_DateFormat(BiConsumer<Class<Long>, Function<Long, String>> registerFormatterForTypeFunction) {
    // WHEN
    registerFormatterForTypeFunction.accept(Long.class, l -> format("%s long", l));
    // THEN
    then(StandardRepresentation.STANDARD_REPRESENTATION.toStringOf(3L)).isEqualTo("3 long");
  }

  private static <T> Stream<BiConsumer<Class<T>, Function<T, String>>> registerFormatterForTypeFunctions() {
    return Stream.of(Assertions::registerFormatterForType,
                     BDDAssertions::registerFormatterForType,
                     withAssertions::registerFormatterForType);
  }

}
