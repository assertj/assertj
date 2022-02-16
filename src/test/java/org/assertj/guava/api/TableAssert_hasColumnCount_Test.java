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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.TableShouldHaveColumnCount.tableShouldHaveColumnCount;

import org.junit.jupiter.api.Test;

/**
 * @author David Harris
 */
public class TableAssert_hasColumnCount_Test extends TableAssertBaseTest {

  @Test
  public void should_pass_if_actual_has_0_columns() {
    actual.clear();
    assertThat(actual).hasColumnCount(0);
  }

  @Test
  public void should_pass_if_actual_has_3_columns() {
    assertThat(actual).hasColumnCount(3);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasColumnCount(3));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_expected_is_negative() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasColumnCount(-1));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The expected size should not be negative.");
  }

  @Test
  public void should_fail_if_actual_does_not_have_3_columns() {
    // GIVEN
    actual.clear();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasColumnCount(3));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(tableShouldHaveColumnCount(actual, actual.columnKeySet().size(), 3).create());
  }

}
