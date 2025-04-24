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
package org.assertj.core.api.localdatetime;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldNotBeIn.shouldNotBeIn;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

import java.time.LocalDateTime;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link LocalDateTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
class LocalDateTimeAssert_isNotIn_Test extends LocalDateTimeAssertBaseTest {

  @Test
  void should_pass_if_actual_is_not_in_dateTimes_as_string_array_parameter() {
    assertThat(REFERENCE).isNotIn(AFTER.toString(), BEFORE.toString());
  }

  @Test
  void should_fail_if_actual_is_in_dateTimes_as_string_array_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isNotIn(REFERENCE.toString(), AFTER.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldNotBeIn(REFERENCE, asList(REFERENCE, AFTER)).create());
  }

  @Test
  void should_fail_if_dateTimes_as_string_array_parameter_is_null() {
    // GIVEN
    String[] otherDateTimesAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalDateTime.now()).isNotIn(otherDateTimesAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The given LocalDateTime array should not be null");
  }

  @Test
  void should_fail_if_dateTimes_as_string_array_parameter_is_empty() {
    // GIVEN
    String[] otherDateTimesAsString = new String[0];
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalDateTime.now()).isNotIn(otherDateTimesAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The given LocalDateTime array should not be empty");
  }

}
