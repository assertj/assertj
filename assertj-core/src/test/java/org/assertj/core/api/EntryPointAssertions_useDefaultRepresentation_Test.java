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
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.presentation.HexadecimalRepresentation.HEXA_REPRESENTATION;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.presentation.Representation;
import org.assertj.core.testkit.MutatesGlobalConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions useDefaultRepresentation method")
@MutatesGlobalConfiguration
class EntryPointAssertions_useDefaultRepresentation_Test extends EntryPointAssertionsBaseTest {

  private static final Representation DEFAULT_CUSTOM_REPRESENTATION = AbstractAssert.customRepresentation;

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    AbstractAssert.customRepresentation = DEFAULT_CUSTOM_REPRESENTATION;
  }

  @ParameterizedTest
  @MethodSource("useRepresentationFunctions")
  void should_set_default_Representation(Pair<Consumer<Representation>, Runnable> params) {
    // GIVEN
    params.getLeft().accept(HEXA_REPRESENTATION);
    // WHEN
    params.getRight().run();
    // THEN
    // TODO we don't go back to null representation, but shoud we?
    then(AbstractAssert.customRepresentation).isEqualTo(CONFIGURATION_PROVIDER.representation());
  }

  private static Stream<Pair<Consumer<Representation>, Runnable>> useRepresentationFunctions() {
    return Stream.of(Pair.of(Assertions::useRepresentation, () -> Assertions.useDefaultRepresentation()),
                     Pair.of(BDDAssertions::useRepresentation, () -> BDDAssertions.useDefaultRepresentation()),
                     Pair.of(withAssertions::useRepresentation, () -> withAssertions.useDefaultRepresentation()));
  }

}
