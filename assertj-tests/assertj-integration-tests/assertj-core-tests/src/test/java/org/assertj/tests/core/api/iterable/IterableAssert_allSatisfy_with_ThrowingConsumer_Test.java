/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.tests.core.api.iterable;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class IterableAssert_allSatisfy_with_ThrowingConsumer_Test {

  private final List<String> actual = List.of("Luke", "Leia", "Yoda");

  @Test
  void should_pass_if_all_elements_satisfy_requirements() {
    // GIVEN
    ThrowingConsumer<String> requirements = s -> {
      if (s.length() != 4) throw new IOException("unexpected length");
    };
    // WHEN/THEN
    then(actual).allSatisfy(requirements);
  }

  @Test
  void should_fail_according_to_requirements() {
    // GIVEN
    ThrowingConsumer<String> requirements = s -> assertThat(s).startsWith("L");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allSatisfy(requirements));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(assertionError).hasMessageContaining(format("%n" +
                                                     "Expecting actual:%n" +
                                                     "  \"Yoda\"%n" +
                                                     "to start with:%n" +
                                                     "  \"L\"%n"));
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    List<String> emptyIterable = List.of();
    ThrowingConsumer<String> impossibleRequirements = s -> assertThat(s).contains("error").doesNotContain("error");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(emptyIterable).allSatisfy(impossibleRequirements));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_fail_if_consumer_is_null() {
    // GIVEN
    ThrowingConsumer<String> requirements = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).allSatisfy(requirements));
    // THEN
    then(npe).hasMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    ThrowingConsumer<String> requirements = s -> assertThat(s).isNotNull();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allSatisfy(requirements));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    Throwable exception = new Throwable("boom!");
    ThrowingConsumer<String> requirements = s -> {
      throw exception;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).allSatisfy(requirements));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .cause().isSameAs(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    RuntimeException runtimeException = new RuntimeException("boom!");
    ThrowingConsumer<String> requirements = s -> {
      throw runtimeException;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).allSatisfy(requirements));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    ThrowingConsumer<String> requirements = s -> assertThat(s).startsWith("Lu");
    // WHEN
    softly.assertThat(List.of("Luke", "Yoda"))
          .overridingErrorMessage("error message")
          .allSatisfy(requirements);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
