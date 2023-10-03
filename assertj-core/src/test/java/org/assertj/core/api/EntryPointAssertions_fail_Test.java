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
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions fail method")
class EntryPointAssertions_fail_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("failFunctions")
  <T> void should_fail_with_given_message(Function<String, T> failFunction) {
    // GIVEN
    String message = "boom!";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> failFunction.apply(message));
    // THEN
    then(assertionError).hasMessage(message);
  }

  private static <T> Stream<Function<String, T>> failFunctions() {
    return Stream.of(Assertions::fail, BDDAssertions::fail, withAssertions::fail);
  }

  @ParameterizedTest
  @MethodSource("failWithParamFunctions")
  <T> void should_fail_with_given_message_formatted_with_arguments(BiFunction<String, Object[], T> failWithParamFunction) {
    // GIVEN
    String message = "%sm!";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> failWithParamFunction.apply(message, array("boo")));
    // THEN
    then(assertionError).hasMessage("boom!");
  }

  private static <T> Stream<BiFunction<String, Object[], T>> failWithParamFunctions() {
    return Stream.of(Assertions::fail, BDDAssertions::fail, withAssertions::fail);
  }

  @ParameterizedTest
  @MethodSource("failWithCauseFunctions")
  <T> void should_fail_with_given_message_with_cause(BiFunction<String, Throwable, T> failWithCauseFunction) {
    // GIVEN
    String message = "boom!";
    Throwable cause = new NullPointerException();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> failWithCauseFunction.apply(message, cause));
    // THEN
    then(assertionError).hasMessage("boom!")
                        .hasCause(cause);
  }

  private static <T> Stream<BiFunction<String, Throwable, T>> failWithCauseFunctions() {
    return Stream.of(Assertions::fail, BDDAssertions::fail, withAssertions::fail);
  }

  @ParameterizedTest
  @MethodSource("failFunctions")
  void should_return_a_value_to_allow_using_optional_orElseGet(Function<String, Integer> failFunction) {
    // GIVEN
    String message = "boom!";
    Optional<Integer> empty = Optional.empty();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> doSomethingWithInt(empty.orElseGet(() -> failFunction.apply(message))));
    // THEN
    then(assertionError).hasMessage("boom!");
  }

  private void doSomethingWithInt(@SuppressWarnings("unused") int parameter) {
    // just to illustrate the previous test
  }

  @ParameterizedTest
  @MethodSource
  void should_fail_without_message(Supplier<Void> supplier) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> supplier.get());
    // THEN
    then(assertionError).hasMessage("");
  }

  private static Stream<Supplier<Void>> should_fail_without_message() {
    return Stream.of(Assertions::fail, BDDAssertions::fail, withAssertions::fail);
  }

  @ParameterizedTest
  @MethodSource
  <T> void should_fail_without_message_but_with_root_cause(Function<Throwable, T> failWithCauseFunction) {
    // GIVEN
    String message = "boom!";
    Exception cause = new Exception(message);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> failWithCauseFunction.apply(cause));
    // THEN
    then(assertionError).hasCause(cause);
  }

  private static <T> Stream<Function<Throwable, T>> should_fail_without_message_but_with_root_cause() {
    return Stream.of(Assertions::fail, BDDAssertions::fail, withAssertions::fail);
  }

}
