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

import org.junit.jupiter.api.Test;

/**
 * @author Jan Gorman
 */
public class TableAssert_isEmpty_Test extends TableAssertBaseTest {

  @Test
  public void should_pass_if_actual_is_empty() {
    // GIVEN
    actual.clear();
    // WHEN/THEN
    assertThat(actual).isEmpty();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).isEmpty());
    // THEN
    then(thrown).isInstanceOf(AssertionError.class)
                .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_is_not_empty() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).isEmpty());
    // THEN
    then(thrown).isInstanceOf(AssertionError.class)
                .hasMessage("%nExpecting empty but was: {1={3=Millard Fillmore, 4=Franklin Pierce}, 2={5=Grover Cleveland}}".formatted());
  }

}
