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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

class AtomicReferenceArrayAssert_noneSatisfy_Test {

  private static final Consumer<String> STARTS_WITH_L = name -> assertThat(name).startsWith("L");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).noneSatisfy(STARTS_WITH_L));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_consumer_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Leia"));
    Consumer<String> restrictions = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).noneSatisfy(restrictions));
    // THEN
    then(npe).hasMessage("The Consumer<T> expressing the restrictions must not be null");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    assertThat(new AtomicReferenceArray<>(new String[0])).noneSatisfy(STARTS_WITH_L);
  }

  @Test
  void should_pass_if_no_element_satisfies_the_restrictions() {
    assertThat(new AtomicReferenceArray<>(array("Yoda", "Mace"))).noneSatisfy(STARTS_WITH_L);
  }

  @Test
  void should_fail_if_some_elements_satisfy_the_restrictions() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Yoda", "Leia"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).noneSatisfy(STARTS_WITH_L));
    // THEN
    then(assertionError).hasMessage(noElementsShouldSatisfy(list("Luke", "Yoda", "Leia"), list("Luke", "Leia")).create());
  }

}
