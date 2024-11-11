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
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPointAssertions shouldHaveThrown")
class EntryPointAssertions_shouldHaveThrown_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("shouldHaveThrownFunction")
  <T> void should_throw_an_AssertionError_with_message_indicating_the_expected_excepion(Function<Class<? extends Throwable>, T> shouldHaveThrownFunction) {
    // GIVEN
    Class<? extends Throwable> throwableClass = NullPointerException.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> shouldHaveThrownFunction.apply(throwableClass));
    // THEN
    then(assertionError).hasMessage("NullPointerException should have been thrown");
  }

  private static <T> Stream<Function<Class<? extends Throwable>, T>> shouldHaveThrownFunction() {
    return Stream.of(Assertions::shouldHaveThrown, BDDAssertions::shouldHaveThrown, withAssertions::shouldHaveThrown);
  }

}
