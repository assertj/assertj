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
import static org.mockito.Mockito.mock;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EntryPointAssertions_as_with_TypeBasedAssertFactory_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("arguments")
  void should_return_the_given_assert_factory(Function<TypeBasedAssertFactory<?, AbstractAssert<?, ?>>, TypeBasedAssertFactory<?, AbstractAssert<?, ?>>> underTest) {
    // GIVEN
    TypeBasedAssertFactory<?, AbstractAssert<?, ?>> assertFactory = mock(InstanceOfAssertFactory.class);
    // WHEN
    TypeBasedAssertFactory<?, AbstractAssert<?, ?>> result = underTest.apply(assertFactory);
    // THEN
    then(result).isSameAs(assertFactory);
  }

  private static Stream<Function<TypeBasedAssertFactory<?, AbstractAssert<?, ?>>, TypeBasedAssertFactory<?, AbstractAssert<?, ?>>>> arguments() {
    return Stream.of(Assertions::as, BDDAssertions::as, withAssertions::as);
  }

}
