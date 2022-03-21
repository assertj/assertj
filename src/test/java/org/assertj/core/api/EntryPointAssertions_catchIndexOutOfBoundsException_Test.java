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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EntryPointAssertions_catchIndexOutOfBoundsException_Test extends EntryPointAssertionsBaseTest {

  private static final IndexOutOfBoundsException INDEX_OUT_OF_BOUNDS_EXCEPTION = new IndexOutOfBoundsException();

  @ParameterizedTest
  @MethodSource("catchIndexOutOfBoundsExceptions")
  void should_catch_IndexOutOfBoundsException(Function<ThrowingCallable, IndexOutOfBoundsException> catchIndexOutOfBoundsException) {
    // GIVEN
    ThrowingCallable throwingCallable = () -> {
      throw INDEX_OUT_OF_BOUNDS_EXCEPTION;
    };
    // WHEN
    IndexOutOfBoundsException throwable = catchIndexOutOfBoundsException.apply(throwingCallable);
    // THEN
    then(throwable).isSameAs(INDEX_OUT_OF_BOUNDS_EXCEPTION);
  }

  private static Stream<Function<ThrowingCallable, IndexOutOfBoundsException>> catchIndexOutOfBoundsExceptions() {
    return Stream.of(Assertions::catchIndexOutOfBoundsException, BDDAssertions::catchIndexOutOfBoundsException,
                     withAssertions::catchIndexOutOfBoundsException);
  }

}
