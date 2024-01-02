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

import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions setPrintAssertionsDescription method")
class EntryPointAssertions_setPrintAssertionsDescription_Test extends EntryPointAssertionsBaseTest {

  private static final boolean DEFAULT_EXTRACTING_BARE_NAME_PROPERTY_METHODS = AbstractAssert.printAssertionsDescription;

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    AbstractAssert.printAssertionsDescription = DEFAULT_EXTRACTING_BARE_NAME_PROPERTY_METHODS;
  }

  @ParameterizedTest
  @MethodSource("setPrintAssertionsDescriptionMethodsFunctions")
  void should_set_printAssertionsDescription_value(Consumer<Boolean> setPrintAssertionsDescriptionMethodsFunction) {
    // GIVEN
    boolean printAssertionsDescription = !DEFAULT_EXTRACTING_BARE_NAME_PROPERTY_METHODS;
    // WHEN
    setPrintAssertionsDescriptionMethodsFunction.accept(printAssertionsDescription);
    // THEN
    then(AbstractAssert.printAssertionsDescription).isEqualTo(printAssertionsDescription);
  }

  private static Stream<Consumer<Boolean>> setPrintAssertionsDescriptionMethodsFunctions() {
    return Stream.of(Assertions::setPrintAssertionsDescription,
                     BDDAssertions::setPrintAssertionsDescription,
                     WithAssertions::setPrintAssertionsDescription);
  }

}
