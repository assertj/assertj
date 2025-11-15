/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.throwable;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIOException;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.ThrowingCallableFactory.codeThrowing;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableTypeAssert;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ThrowableTypeAssert_description_Test {

  @BeforeAll
  static void beforeAll() {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
  }

  static Stream<Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>>> parameters() {
    return Stream.of(t -> t.as("test description"),
                     t -> t.describedAs("test description"),
                     t -> t.as(new TextDescription("%s description", "test")),
                     t -> t.describedAs(new TextDescription("%s description", "test")));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_contain_provided_description_if_nothing_is_thrown_by_lambda(Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>> descriptionAdder) {
    // WHEN
    var assertionError = expectAssertionError(() -> descriptionAdder.apply(assertThatExceptionOfType(IOException.class))
                                                                    .isThrownBy(() -> {}));
    // THEN
    then(assertionError).hasMessage("[test description] %nExpecting code to throw a java.io.IOException, but no throwable was thrown.".formatted());
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_contain_provided_description_when_exception_type_is_wrong(Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>> descriptionAdder) {
    // WHEN
    var assertionError = expectAssertionError(() -> descriptionAdder.apply(assertThatIOException())
                                                                    .isThrownBy(codeThrowing(new IllegalArgumentException())));
    // THEN
    then(assertionError).hasMessageContaining(format("[test description] %n" +
                                                     "Expecting actual throwable to be an instance of:%n" +
                                                     "  java.io.IOException%n" +
                                                     "but was:%n" +
                                                     "  java.lang.IllegalArgumentException"));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_contain_provided_description_when_exception_message_is_wrong(Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>> descriptionAdder) {
    // GIVEN
    IllegalArgumentException exception = new IllegalArgumentException("some cause");
    // WHEN
    var assertionError = expectAssertionError(() -> descriptionAdder.apply(assertThatIllegalArgumentException())
                                                                    .isThrownBy(codeThrowing(exception))
                                                                    .withMessage("other cause"));
    // THEN
    then(assertionError).hasMessageStartingWith(format("[test description] %n" +
                                                       "Expecting message to be:%n" +
                                                       "  \"other cause\"%n" +
                                                       "but was:%n" +
                                                       "  \"some cause\"%n"));
  }
}
