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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.localtime;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link LocalTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class LocalTimeAssert_isNotIn_Test extends LocalTimeAssertBaseTest {

  @Test
  public void test_isNotIn_assertion() {
	// WHEN
	assertThat(REFERENCE).isNotIn(REFERENCE.plusHours(1).toString(), REFERENCE.plusHours(2).toString());
	// THEN
    assertThatThrownBy(() -> assertThat(REFERENCE).isNotIn(REFERENCE.toString(),
                                                           REFERENCE.plusHours(1).toString()))
                                                                                              .isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isNotIn_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(LocalTime.of(3, 0,
                                                                                             5)).isNotIn("03:00:05",
                                                                                                         "03:03:03"))
                                                   .withMessage(format("%n" +
                                                                       "Expecting:%n" +
                                                                       " <03:00:05>%n" +
                                                                       "not to be in:%n" +
                                                                       " <[03:00:05, 03:03:03]>%n"));
  }

  @Test
  public void should_fail_if_timeTimes_as_string_array_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(LocalTime.now()).isNotIn((String[]) null))
                                        .withMessage("The given LocalTime array should not be null");
  }

  @Test
  public void should_fail_if_timeTimes_as_string_array_parameter_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(LocalTime.now()).isNotIn(new String[0]))
                                        .withMessage("The given LocalTime array should not be empty");
  }

}
