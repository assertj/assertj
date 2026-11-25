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
package org.assertj.tests.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfyExactlyInAnyOrder;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

/// @author Michael Grafl
class AtomicReferenceArrayAssert_satisfiesExactlyInAnyOrder_with_ThrowingConsumer_Test {

  private static final ThrowingConsumer<String> STARTS_WITH_L = name -> {
    if (name.isEmpty()) throw new IOException("empty name");
    assertThat(name).startsWith("L");
  };
  private static final ThrowingConsumer<String> STARTS_WITH_Y = name -> {
    if (name.isEmpty()) throw new IOException("empty name");
    assertThat(name).startsWith("Y");
  };

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(STARTS_WITH_L));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_requirements_array_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Yoda"));
    ThrowingConsumer<String>[] requirements = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).satisfiesExactlyInAnyOrder(requirements));
    // THEN
    then(npe).hasMessage("The Consumer<? super E>... expressing the assertions must not be null");
  }

  @Test
  void should_fail_if_actual_and_requirements_have_different_sizes() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Yoda", "Leia"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(STARTS_WITH_L,
                                                                                                  STARTS_WITH_Y));
    // THEN
    then(assertionError).hasMessage(shouldHaveSize(list("Luke", "Yoda", "Leia"), 3, 2).create());
  }

  @Test
  void should_pass_if_requirements_are_satisfied_in_a_different_order() {
    assertThat(new AtomicReferenceArray<>(array("Yoda", "Luke"))).satisfiesExactlyInAnyOrder(STARTS_WITH_L, STARTS_WITH_Y);
  }

  @Test
  void should_fail_if_one_requirement_cannot_be_satisfied() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Leia"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(STARTS_WITH_L,
                                                                                                  STARTS_WITH_Y));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyExactlyInAnyOrder(list("Luke", "Leia")).create());
  }

  @Test
  void should_rethrow_checked_exceptions_as_runtime_exceptions() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke"));
    IOException exception = new IOException("boom!");
    ThrowingConsumer<String> requirements = _ -> {
      throw exception;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).satisfiesExactlyInAnyOrder(requirements));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .cause().isSameAs(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke"));
    RuntimeException runtimeException = new RuntimeException("boom!");
    ThrowingConsumer<String> requirements = _ -> {
      throw runtimeException;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).satisfiesExactlyInAnyOrder(requirements));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

}
