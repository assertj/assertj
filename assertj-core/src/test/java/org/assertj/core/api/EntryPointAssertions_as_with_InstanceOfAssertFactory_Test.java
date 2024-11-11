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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link Assertions#as(InstanceOfAssertFactory)}</code>.
 *
 * @author Stefano Cordio
 */
@DisplayName("EntryPoint assertions as(InstanceOfAssertFactory) method")
class EntryPointAssertions_as_with_InstanceOfAssertFactory_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("asInstanceOfAssertFactoryFactories")
  void should_return_the_given_assert_factory(Function<InstanceOfAssertFactory<?, AbstractAssert<?, ?>>, InstanceOfAssertFactory<?, AbstractAssert<?, ?>>> asInstanceOfAssertFactory) {
    // GIVEN
    InstanceOfAssertFactory<?, AbstractAssert<?, ?>> assertFactory = mock(InstanceOfAssertFactory.class);
    // WHEN
    InstanceOfAssertFactory<?, AbstractAssert<?, ?>> result = asInstanceOfAssertFactory.apply(assertFactory);
    // THEN
    then(result).isSameAs(assertFactory);
  }

  private static Stream<Function<InstanceOfAssertFactory<?, AbstractAssert<?, ?>>, InstanceOfAssertFactory<?, AbstractAssert<?, ?>>>> asInstanceOfAssertFactoryFactories() {
    return Stream.of(Assertions::as, BDDAssertions::as, withAssertions::as);
  }

}
