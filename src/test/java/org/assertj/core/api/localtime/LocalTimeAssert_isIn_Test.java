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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.localtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.Test;

/**
 * Only test String based assertion (tests with {@link LocalTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class LocalTimeAssert_isIn_Test extends LocalTimeAssertBaseTest {

  @Test
  public void test_isIn_assertion() {
	// WHEN
	assertThat(REFERENCE).isIn(REFERENCE.toString(), REFERENCE.plusHours(1).toString());
	// THEN
    assertThatThrownBy(() -> assertThat(REFERENCE).isIn(REFERENCE.plusHours(1).toString(),
                                                        REFERENCE.plusHours(2).toString()))
                                                                                           .isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isIn_assertion_error_message() {
    thrown.expectAssertionError("%n" +
                                "Expecting:%n" +
                                " <03:00:05>%n" +
                                "to be in:%n" +
                                " <[03:03:03]>%n");
    assertThat(LocalTime.of(3, 0, 5)).isIn("03:03:03");
  }

  @Test
  public void should_fail_if_timeTimes_as_string_array_parameter_is_null() {
	expectException(IllegalArgumentException.class, "The given LocalTime array should not be null");
	assertThat(LocalTime.now()).isIn((String[]) null);
  }

  @Test
  public void should_fail_if_timeTimes_as_string_array_parameter_is_empty() {
	expectException(IllegalArgumentException.class, "The given LocalTime array should not be empty");
	assertThat(LocalTime.now()).isIn(new String[0]);
  }

}
