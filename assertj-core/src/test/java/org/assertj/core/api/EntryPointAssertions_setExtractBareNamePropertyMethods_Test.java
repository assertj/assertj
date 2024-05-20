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
import org.assertj.core.util.introspection.Introspection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions setExtractBareNamePropertyMethods method")
class EntryPointAssertions_setExtractBareNamePropertyMethods_Test extends EntryPointAssertionsBaseTest {

  private static final boolean DEFAULT_EXTRACTING_BARE_NAME_PROPERTY_METHODS = Introspection.canExtractBareNamePropertyMethods();

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    Introspection.setExtractBareNamePropertyMethods(DEFAULT_EXTRACTING_BARE_NAME_PROPERTY_METHODS);
  }

  @ParameterizedTest
  @MethodSource("setAllowExtractingBareNamePropertyMethodsFunctions")
  void should_set_allowComparingPrivateFields_value(Consumer<Boolean> setAllowExtractingBareNamePropertyMethodsFunction) {
    // GIVEN
    boolean extractBareNamePropertyMethods = !DEFAULT_EXTRACTING_BARE_NAME_PROPERTY_METHODS;
    // WHEN
    setAllowExtractingBareNamePropertyMethodsFunction.accept(extractBareNamePropertyMethods);
    // THEN
    then(Introspection.canExtractBareNamePropertyMethods()).isEqualTo(extractBareNamePropertyMethods);
  }

  private static Stream<Consumer<Boolean>> setAllowExtractingBareNamePropertyMethodsFunctions() {
    return Stream.of(Assertions::setExtractBareNamePropertyMethods,
                     BDDAssertions::setExtractBareNamePropertyMethods,
                     withAssertions::setExtractBareNamePropertyMethods);
  }

}
