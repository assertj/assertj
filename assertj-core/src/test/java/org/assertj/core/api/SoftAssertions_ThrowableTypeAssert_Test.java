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
import static org.assertj.core.testkit.ThrowingCallableFactory.codeThrowing;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SoftAssertions_ThrowableTypeAssert_Test {

  private SoftAssertions softly;

  @BeforeEach
  void setup() {
    softly = new SoftAssertions();
  }

  @Test
  void should_collect_errors_with_their_description() {
    // GIVEN
    ThrowingCallable codeThrowingRuntimeException = codeThrowing(new RuntimeException("boom"));
    // WHEN
    softly.assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(codeThrowingRuntimeException)
          .as("withMessage error 1")
          .withMessage("bam")
          .as("withMessage error 2")
          .withMessage("bim");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    then(errorsCollected).hasSize(2);
    then(errorsCollected.get(0)).hasMessageContaining("withMessage error 1");
    then(errorsCollected.get(1)).hasMessageContaining("withMessage error 2");
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("assertThat_exception_methods")
  void should_collect_errors_from_assertThat_exception(SoftAssertionsFunction<?> assertionFunction, Throwable throwable) {
    // GIVEN
    ThrowingCallable codeThrowingThrowable = codeThrowing(throwable);
    // WHEN
    assertionFunction.apply(softly)
                     .isThrownBy(codeThrowingThrowable)
                     .withMessage("bam")
                     .withMessage("bim");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    then(errorsCollected).hasSize(2);
    then(errorsCollected.get(0)).hasMessageContaining("bam");
    then(errorsCollected.get(1)).hasMessageContaining("bim");
  }

  private static Stream<Arguments> assertThat_exception_methods() {
    return Stream.of(arguments(softAssertionFunction("assertThatExceptionOfType",
                                                     softly -> softly.assertThatExceptionOfType(RuntimeException.class)),
                               new NullPointerException("boom")),
                     arguments(softAssertionFunction("assertThatRuntimeException",
                                                     softly -> softly.assertThatRuntimeException()),
                               new RuntimeException("boom")),
                     arguments(softAssertionFunction("assertThatNullPointerException",
                                                     softly -> softly.assertThatNullPointerException()),
                               new NullPointerException("boom")),
                     arguments(softAssertionFunction("assertThatIllegalArgumentException",
                                                     softly -> softly.assertThatIllegalArgumentException()),
                               new IllegalArgumentException("boom")),
                     arguments(softAssertionFunction("assertThatIOException",
                                                     softly -> softly.assertThatIOException()),
                               new IOException("boom")),
                     arguments(softAssertionFunction("assertThatIndexOutOfBoundsException",
                                                     softly -> softly.assertThatIndexOutOfBoundsException()),
                               new IndexOutOfBoundsException("boom")),
                     arguments(softAssertionFunction("assertThatReflectiveOperationException",
                                                     softly -> softly.assertThatReflectiveOperationException()),
                               new ReflectiveOperationException("boom")));
  }

  private static <T extends Throwable> Function<SoftAssertions, ThrowableTypeAssert<T>> softAssertionFunction(String assertionMethod,
                                                                                                              Function<SoftAssertions, ThrowableTypeAssert<T>> softAssertionsFunction) {
    return new SoftAssertionsFunction<>(assertionMethod, softAssertionsFunction);
  }

  // just here to get a nice toString for the ParameterizedTest display name
  private static class SoftAssertionsFunction<T extends Throwable> implements Function<SoftAssertions, ThrowableTypeAssert<T>> {

    private Function<SoftAssertions, ThrowableTypeAssert<T>> function;
    private String assertionMethod;

    SoftAssertionsFunction(String assertionMethod, Function<SoftAssertions, ThrowableTypeAssert<T>> softAssertionsFunction) {
      this.function = softAssertionsFunction;
      this.assertionMethod = assertionMethod;
    }

    @Override
    public ThrowableTypeAssert<T> apply(SoftAssertions softly) {
      return function.apply(softly);
    }

    @Override
    public String toString() {
      return this.assertionMethod;
    }
  }

}
