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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableTypeAssert;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ThrowableTypeAssert_description_Test {

  @BeforeAll
  public static void beforeAll() {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
  }

  static Stream<Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>>> parameters() {
    return Stream.of(
                     t -> t.as("test description"),
                     t -> t.describedAs("test description"),
                     t -> t.as(new TextDescription("%s description", "test")),
                     t -> t.describedAs(new TextDescription("%s description", "test")));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void should_contain_provided_description_if_nothing_is_thrown_by_lambda(Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>> descriptionAdder) {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> descriptionAdder.apply(assertThatExceptionOfType(NoSuchElementException.class))
                                                                                     .isThrownBy(() -> {}))
                                                   .withMessage(format("[test description] %nExpecting code to raise a throwable."));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void should_contain_provided_description_when_exception_type_is_wrong(Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>> descriptionAdder) {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> descriptionAdder.apply(assertThatExceptionOfType(NoSuchElementException.class))
                                                                                     .isThrownBy(() -> {
                                                                                       throw new IllegalArgumentException();
                                                                                     }))
                                                   .withMessageContaining(format("[test description] %n" +
                                                                                 "Expecting:%n" +
                                                                                 "  <java.lang.IllegalArgumentException>%n" +
                                                                                 "to be an instance of:%n" +
                                                                                 "  <java.util.NoSuchElementException>"));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void should_contain_provided_description_when_exception_message_is_wrong(Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>> descriptionAdder) {
    IllegalArgumentException exception = new IllegalArgumentException("some cause");
    assertThatAssertionErrorIsThrownBy(() -> {
      descriptionAdder.apply(assertThatIllegalArgumentException()).isThrownBy(() -> {
        throw exception;
      }).withMessage("other cause");
    }).withMessageStartingWith(format("[test description] %n" +
                          "Expecting message to be:%n" +
                          "  <\"other cause\">%n" +
                          "but was:%n" +
                                      "  <\"some cause\">%n"));
  }
}
