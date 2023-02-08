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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EntryPointAssertions_catchException_Test extends EntryPointAssertionsBaseTest {

  private static final Exception EXCEPTION = new Exception();

  @ParameterizedTest
  @MethodSource("catchExceptions")
  void should_catch_Exception(Function<ThrowingCallable, Exception> catchException) {
    // GIVEN
    ThrowingCallable throwingCallable = () -> {
      throw EXCEPTION;
    };
    // WHEN
    Exception throwable = catchException.apply(throwingCallable);
    // THEN
    then(throwable).isSameAs(EXCEPTION);
  }

  private static Stream<Function<ThrowingCallable, Exception>> catchExceptions() {
    return Stream.of(Assertions::catchException, BDDAssertions::catchException, withAssertions::catchException);
  }

}
