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
package org.assertj.tests.guava.api;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.assertj.guava.api.TableAssert;
import org.junit.jupiter.api.Test;

/**
 * @author Maciej Kucharczyk
 */
class TableAssert_isNotEmpty_Test extends TableAssertBaseTest {

  @Test
  void should_pass_if_actual_is_not_empty() {
    // WHEN/THEN
    assertThat(actual).isNotEmpty();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(thrown).isInstanceOf(AssertionError.class)
                .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    actual.clear();
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(thrown).isInstanceOf(AssertionError.class)
                .hasMessage("%nExpecting actual not to be empty".formatted());
  }

  @Test
  void should_return_this() {
    // GIVEN
    TableAssert<Integer, Integer, String> assertion = assertThat(actual);
    // WHEN
    TableAssert<Integer, Integer, String> returnedAssertion = assertion.isNotEmpty();
    // THEN
    then(returnedAssertion).isSameAs(assertion);
  }

}
