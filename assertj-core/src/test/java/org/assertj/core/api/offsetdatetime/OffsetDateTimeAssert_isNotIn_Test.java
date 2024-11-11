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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.offsetdatetime;

import static java.time.OffsetDateTime.now;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.error.ShouldNotBeIn.shouldNotBeIn;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.time.OffsetDateTime;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link OffsetDateTime} are already defined in assertj-core)
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
class OffsetDateTimeAssert_isNotIn_Test extends OffsetDateTimeAssertBaseTest {

  @Test
  void should_pass_if_actual_is_not_in_offsetDateTimes_as_string_array_parameter() {
    assertThat(REFERENCE).isNotIn(AFTER.toString(), REFERENCE_WITH_DIFFERENT_OFFSET.toString());
  }

  @Test
  void should_fail_if_actual_is_in_offsetDateTimes_as_string_array_parameter() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(REFERENCE).isNotIn(REFERENCE.toString(), AFTER.toString()));
    // THEN
    then(error).hasMessage(shouldNotBeIn(REFERENCE, asList(REFERENCE, AFTER)).create());
  }

  @Test
  void should_fail_if_offsetDateTimes_as_string_array_parameter_is_null() {
    // GIVEN
    String[] otherOffsetDateTimesAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(OffsetDateTime.now()).isNotIn(otherOffsetDateTimesAsString);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("The given OffsetDateTime array should not be null");
  }

  @Test
  void should_fail_if_offsetDateTimes_as_string_array_parameter_is_empty() {
    // GIVEN
    String[] otherOffsetDateTimesAsString = new String[0];
    // WHEN
    ThrowingCallable code = () -> assertThat(now()).isNotIn(otherOffsetDateTimesAsString);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("The given OffsetDateTime array should not be empty");
  }

}
