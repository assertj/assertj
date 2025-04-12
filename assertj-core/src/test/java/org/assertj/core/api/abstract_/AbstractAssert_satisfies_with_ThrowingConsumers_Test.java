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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.testkit.ClasspathResources.resourcePath;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.ThrowingConsumerFactory.throwingConsumer;

import java.nio.file.Path;

import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class AbstractAssert_satisfies_with_ThrowingConsumers_Test {

  @Test
  void should_pass_satisfying_single_requirement() {
    // GIVEN
    Path emptyFile = resourcePath("empty.txt");
    ThrowingConsumer<Path> isEmpty = path -> assertThat(readAllLines(path)).isEmpty();
    // WHEN/THEN
    then(emptyFile).satisfies(isEmpty);
  }

  @Test
  void should_pass_satisfying_multiple_requirements() {
    // GIVEN
    Path emptyFile = resourcePath("empty.txt");
    ThrowingConsumer<Path> readableConsumer = path -> assertThat(isReadable(path)).isTrue();
    ThrowingConsumer<Path> emptyConsumer = path -> assertThat(readAllLines(path)).isEmpty();
    // WHEN/THEN
    then(emptyFile).satisfies(readableConsumer, emptyConsumer);
  }

  @Test
  void should_pass_with_supertype_consumer() {
    // GIVEN
    ThrowingConsumer<Object> notNullObjectConsumer = object -> assertThat(object).isNotNull();
    // WHEN/THEN
    then("foo").satisfies(notNullObjectConsumer);
  }

  @Test
  void should_fail_not_satisfying_single_requirement() {
    // GIVEN
    Path asciiFile = resourcePath("ascii.txt");
    ThrowingConsumer<Path> emptyConsumer = path -> assertThat(readAllLines(path)).isEmpty();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(asciiFile).satisfies(emptyConsumer));
    // THEN
    then(assertionError).hasMessageContaining("Expecting empty but was: [\"abc\"]");
  }

  @Test
  void should_fail_not_satisfying_any_requirements() {
    // GIVEN
    Path asciiFile = resourcePath("ascii.txt");
    ThrowingConsumer<Path> emptyConsumer = path -> assertThat(readAllLines(path)).as("empty check").isEmpty();
    ThrowingConsumer<Path> directoryConsumer = path -> assertThat(path).as("directory check").isDirectory();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(asciiFile).satisfies(emptyConsumer, directoryConsumer));
    // THEN
    then(assertionError).hasMessageContainingAll("empty check", "directory check");
  }

  @Test
  void should_fail_not_satisfying_some_requirements() {
    // GIVEN
    Path asciiFile = resourcePath("ascii.txt");
    ThrowingConsumer<Path> notEmptyConsumer = path -> assertThat(readAllLines(path)).as("not empty check").isNotEmpty();
    ThrowingConsumer<Path> directoryConsumer = path -> assertThat(path).as("directory check").isDirectory();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(asciiFile).satisfies(directoryConsumer,
                                                                                               notEmptyConsumer));
    // THEN
    then(assertionError).hasMessageContaining("directory check")
                        .hasMessageNotContaining("not empty check");
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    Throwable exception = new Throwable("boom!");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat("foo").satisfies(throwingConsumer(exception)));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .cause().isSameAs(exception);
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
  void should_fail_if_throwing_consumer_is_null() {
    // GIVEN
    ThrowingConsumer<String> nullRequirements = null;
    // WHEN/THEN
    thenIllegalArgumentException().isThrownBy(() -> assertThat("foo").satisfies(nullRequirements))
                                  .withMessage("No assertions group should be null");
  }

  @Test
  void should_fail_if_one_of_the_consumers_is_null() {
    // GIVEN
    ThrowingConsumer<String> nullRequirement = null;
    ThrowingConsumer<String> nonNullRequirement = string -> assertThat(true).isTrue();
    // WHEN/THEN
    thenIllegalArgumentException().isThrownBy(() -> assertThat("bar").satisfies(nonNullRequirement, nullRequirement))
                                  .withMessage("No assertions group should be null");
  }
}
