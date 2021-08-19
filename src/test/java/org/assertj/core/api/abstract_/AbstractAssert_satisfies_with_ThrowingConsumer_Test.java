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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractAssert_satisfies_with_ThrowingConsumer_Test {

  private ThrowingConsumer<Path> isEOF;

  @BeforeEach
  void setup() {
    isEOF = path -> {
      assertThat(isReadable(path)).isTrue();
      // this would not compile if isEOF was declared as Consumer<Path> since it can throw an IOException
      assertThat(readAllLines(path)).isEmpty();
    };
  }

  @Test
  void should_satisfy_single_requirement() {
    // GIVEN
    Path emptyFile = Paths.get("src/test/resources/empty.txt");
    ThrowingConsumer<Path> isEmpty = path -> assertThat(readAllLines(path)).isEmpty();
    // WHEN/THEN
    then(emptyFile).satisfies(isEmpty);
  }

  @Test
  void should_satisfy_multiple_requirements() {
    // GIVEN
    Path emptyFile = Paths.get("src/test/resources/empty.txt");
    // WHEN/THEN
    then(emptyFile).satisfies(isEOF);
  }

  @Test
  void should_satisfy_supertype_consumer() {
    // GIVEN
    ThrowingConsumer<Object> notNullObjectConsumer = object -> assertThat(object).isNotNull();
    // WHEN/THEN
    then("foo").satisfies(notNullObjectConsumer);
  }

  @Test
  void should_fail_according_to_requirements() {
    // GIVEN
    Path asciiFile = Paths.get("src/test/resources/ascii.txt");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(asciiFile).satisfies(isEOF));
    // THEN
    then(assertionError).hasMessageContaining("Expecting empty but was: [\"abc\"]");
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    Throwable exception = new Throwable("boom!");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat("foo").satisfies(throwingConsumer(exception)));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .hasCauseReference(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    RuntimeException runtimeException = new RuntimeException("boom!");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat("foo").satisfies(throwingConsumer(runtimeException)));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

  @Test
  void should_propagate_AssertionError_as_is() {
    // GIVEN
    AssertionError assertionError = new AssertionError("boom!");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat("foo").satisfies(throwingConsumer(assertionError)));
    // THEN
    then(throwable).isSameAs(assertionError);
  }

  @Test
  void should_fail_if_throwing_consumer_is_null() {
    // GIVEN
    ThrowingConsumer<String> nullRequirements = null;
    // WHEN/THEN
    thenNullPointerException().isThrownBy(() -> assertThat("foo").satisfies(nullRequirements))
                              .withMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }

  private static ThrowingConsumer<String> throwingConsumer(Throwable throwable) {
    return value -> {
      throw throwable;
    };
  }
}
