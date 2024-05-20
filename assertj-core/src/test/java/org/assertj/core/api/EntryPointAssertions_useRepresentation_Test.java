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
import static org.assertj.core.presentation.HexadecimalRepresentation.HEXA_REPRESENTATION;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions useRepresentation method")
class EntryPointAssertions_useRepresentation_Test extends EntryPointAssertionsBaseTest {

  private static final Representation DEFAULT_CUSTOM_REPRESENTATION = AbstractAssert.customRepresentation;

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    AbstractAssert.customRepresentation = DEFAULT_CUSTOM_REPRESENTATION;
  }

  @ParameterizedTest
  @MethodSource("useRepresentationFunctions")
  void should_set_customRepresentation_value(Consumer<Representation> useRepresentationFunction) {
    // GIVEN
    Representation customRepresentation = HEXA_REPRESENTATION;
    // WHEN
    useRepresentationFunction.accept(customRepresentation);
    // THEN
    then(AbstractAssert.customRepresentation).isEqualTo(customRepresentation);
  }

  private static Stream<Consumer<Representation>> useRepresentationFunctions() {
    return Stream.of(Assertions::useRepresentation,
                     BDDAssertions::useRepresentation,
                     withAssertions::useRepresentation);
  }

}
