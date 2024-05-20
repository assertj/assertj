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

import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions catchThrowable method")
class EntryPointAssertions_catchThrowable_Test extends EntryPointAssertionsBaseTest {

  private static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException();

  @ParameterizedTest
  @MethodSource("catchThrowables")
  void should_catch_throwables(Function<ThrowingCallable, Throwable> catchThrowable) {
    // GIVEN
    ThrowingCallable throwingCallable = () -> {
      throw RUNTIME_EXCEPTION;
    };
    // WHEN
    Throwable throwable = catchThrowable.apply(throwingCallable);
    // THEN
    then(throwable).isSameAs(RUNTIME_EXCEPTION);
  }

  private static Stream<Function<ThrowingCallable, Throwable>> catchThrowables() {
    return Stream.of(Assertions::catchThrowable, BDDAssertions::catchThrowable, withAssertions::catchThrowable);
  }

}
