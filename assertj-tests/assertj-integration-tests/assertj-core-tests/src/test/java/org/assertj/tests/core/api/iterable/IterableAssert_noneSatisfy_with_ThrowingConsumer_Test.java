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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class IterableAssert_noneSatisfy_with_ThrowingConsumer_Test {

  private final List<String> actual = List.of("Luke", "Leia", "Yoda");

  @Test
  void should_pass_when_no_elements_satisfy_the_given_restrictions() {
    // GIVEN
    ThrowingConsumer<String> restrictions = name -> assertThat(name).hasSize(5);
    // WHEN/THEN
    then(actual).noneSatisfy(restrictions);
  }

  @Test
  void should_fail_when_some_elements_satisfy_the_given_restrictions() {
    // GIVEN
    ThrowingConsumer<String> restrictions = name -> assertThat(name).startsWith("L");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).noneSatisfy(restrictions));
    // THEN
    then(assertionError).hasMessage(noElementsShouldSatisfy(actual, List.of("Luke", "Leia")).create());
  }

  @Test
  void should_throw_error_if_consumer_restrictions_is_null() {
    // GIVEN
    ThrowingConsumer<String> restrictions = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).noneSatisfy(restrictions));
    // THEN
    then(npe).hasMessage("The Consumer<T> expressing the restrictions must not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    ThrowingConsumer<String> restrictions = name -> assertThat(name).startsWith("Y");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).noneSatisfy(restrictions));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    Throwable exception = new Throwable("boom!");
    ThrowingConsumer<String> restrictions = s -> {
      throw exception;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).noneSatisfy(restrictions));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .cause().isSameAs(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    RuntimeException runtimeException = new RuntimeException("boom!");
    ThrowingConsumer<String> restrictions = s -> {
      throw runtimeException;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).noneSatisfy(restrictions));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    ThrowingConsumer<String> restrictions = s -> assertThat(s).startsWith("Luk");
    // WHEN
    softly.assertThat(List.of("Luke", "Yoda"))
          .overridingErrorMessage("error message")
          .noneSatisfy(restrictions);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
