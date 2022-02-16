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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.collect.TreeRangeMap;

public class RangeMapAssert_containsValues_Test extends RangeMapAssertBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values() {
    assertThat(actual).containsValues("violet");
    assertThat(actual).containsValues("blue", "red");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    TreeRangeMap<Integer, String> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues("violet", "red"));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_values_to_look_for_are_null() {
    String[] values = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues(values));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The values to look for should not be null");
  }

  @Test
  public void should_fail_if_values_to_look_for_are_empty() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues());
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The values to look for should not be empty");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_values() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues("violet", "black"));
    // THEN
    // @format:off
	  assertThat(throwable).hasMessage(format("%n" +
                		                        "Expecting:%n" +
                		                        "  [[380..450)=violet, [450..495)=blue, [495..570)=green, [570..590)=yellow, [590..620)=orange, [620..750)=red]%n" +
                		                        "to contain values:%n" +
                		                        "  [\"violet\", \"black\"]%n" +
                		                        "but could not find:%n" +
                		                        "  [\"black\"]"));
	  // @format:on
  }

  @Test
  public void should_fail_if_actual_does_not_contain_the_given_value() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues("black"));
    // THEN
    // @format:off
    // error message shows that we were looking for a unique value (not many)
	  assertThat(throwable).hasMessage(format("%n" +
                		                        "Expecting:%n" +
                		                        "  [[380..450)=violet, [450..495)=blue, [495..570)=green, [570..590)=yellow, [590..620)=orange, [620..750)=red]%n" +
                		                        "to contain value:%n" +
                		                        "  \"black\""));
	  // @format:on
  }
}
