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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.NotThrownAssert;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class NotThrownAssert_description_Test {

  @BeforeAll
  static void beforeAll() {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
  }

  static Stream<Function<NotThrownAssert, NotThrownAssert>> parameters() {
    return Stream.of(
                     t -> t.as("test description"),
                     t -> t.describedAs("test description"),
                     t -> t.as(new TextDescription("%s description", "test")),
                     t -> t.describedAs(new TextDescription("%s description", "test")));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_contain_provided_description_if_exception_is_thrown_by_lambda(Function<NotThrownAssert, NotThrownAssert> descriptionAdder) {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> descriptionAdder.apply(assertThatNoException())
                                                                                     .isThrownBy(() -> {
                                                                                       throw new IllegalArgumentException();
                                                                                     }))
                                                   .withMessageStartingWith(format("[test description] %n" +
                                                                                   "Expecting code not to raise a throwable but caught%n" +
                                                                                   "  <\"java.lang.IllegalArgumentException"));
  }
}
