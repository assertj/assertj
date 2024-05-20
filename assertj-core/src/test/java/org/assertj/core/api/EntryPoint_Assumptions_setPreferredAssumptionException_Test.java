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

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.AssumptionExceptionFactory.getPreferredAssumptionException;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.PreferredAssumptionException.JUNIT4;
import static org.assertj.core.configuration.PreferredAssumptionException.JUNIT5;
import static org.assertj.core.configuration.PreferredAssumptionException.TEST_NG;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.assertj.core.configuration.PreferredAssumptionException;
import org.assertj.core.testkit.MutatesGlobalConfiguration;
import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.TestAbortedException;

@MutatesGlobalConfiguration
class EntryPoint_Assumptions_setPreferredAssumptionException_Test {

  protected static final WithAssumptions withAssumptions = mock(WithAssumptions.class, CALLS_REAL_METHODS);

  private static final PreferredAssumptionException DEFAULT_PREFERRED_ASSUMPTION_EXCEPTION = getPreferredAssumptionException();

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    Assumptions.setPreferredAssumptionException(DEFAULT_PREFERRED_ASSUMPTION_EXCEPTION);
  }

  @ParameterizedTest
  @MethodSource("setPreferredAssumptionExceptionFunctions")
  void should_set_preferredAssumptionException_value(Consumer<PreferredAssumptionException> setPreferredAssumptionExceptionFunction) {
    // WHEN
    setPreferredAssumptionExceptionFunction.accept(TEST_NG);
    // THEN
    then(getPreferredAssumptionException()).isEqualTo(TEST_NG);
  }

  @ParameterizedTest
  @MethodSource("setPreferredAssumptionExceptionFunctions")
  void should_throw_TestAbortedException_when_assumption_fails_if_preferredAssumptionException_is_set_to_opentest4j(Consumer<PreferredAssumptionException> setPreferredAssumptionExceptionFunction) {
    // GIVEN
    setPreferredAssumptionExceptionFunction.accept(JUNIT5);
    // WHEN
    Throwable thrown = catchThrowable(() -> assumeThat(true).isEqualTo(false));
    // THEN
    then(thrown).isInstanceOf(TestAbortedException.class);
  }

  @ParameterizedTest
  @MethodSource("setPreferredAssumptionExceptionFunctions")
  void should_throw_AssumptionViolatedException_when_assumption_fails_if_preferredAssumptionException_is_set_to_junit4(Consumer<PreferredAssumptionException> setPreferredAssumptionExceptionFunction) {
    // GIVEN
    setPreferredAssumptionExceptionFunction.accept(JUNIT4);
    // WHEN
    Throwable thrown = catchThrowable(() -> assumeThat(true).isEqualTo(false));
    // THEN
    then(thrown).isInstanceOf(AssumptionViolatedException.class);
  }

  @ParameterizedTest
  @MethodSource("setPreferredAssumptionExceptionFunctions")
  void should_throw_IllegalStateException_when_selected_assumption_exception_is_not_found(Consumer<PreferredAssumptionException> setPreferredAssumptionExceptionFunction) {
    // GIVEN
    setPreferredAssumptionExceptionFunction.accept(TEST_NG);
    // WHEN
    IllegalStateException exception = catchThrowableOfType(() -> assumeThat(true).isEqualTo(false), IllegalStateException.class);
    // THEN
    then(exception).hasMessage("Failed to load org.testng.SkipException class, make sure it is available in the classpath.");
  }

  @ParameterizedTest
  @MethodSource("setPreferredAssumptionExceptionFunctions")
  void should_throw_NPE_if_provided_PreferredAssumptionException_is_null(Consumer<PreferredAssumptionException> setPreferredAssumptionExceptionFunction) {
    assertThatNullPointerException().isThrownBy(() -> setPreferredAssumptionExceptionFunction.accept(null))
                                    .withMessage("preferredAssumptionException must not be null");
  }

  private static Stream<Consumer<PreferredAssumptionException>> setPreferredAssumptionExceptionFunctions() {
    return Stream.of(withAssumptions::setPreferredAssumptionException,
                     Assumptions::setPreferredAssumptionException);
  }

}
