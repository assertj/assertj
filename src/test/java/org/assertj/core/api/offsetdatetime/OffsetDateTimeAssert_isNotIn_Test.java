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
package org.assertj.core.api.offsetdatetime;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldNotBeIn.shouldNotBeIn;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

import java.time.OffsetDateTime;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link java.time.OffsetDateTime} are already defined in assertj-core)
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@DisplayName("OffsetDateTimeAssert isNotIn")
public class OffsetDateTimeAssert_isNotIn_Test extends OffsetDateTimeAssertBaseTest {

  @Test
  public void should_pass_if_actual_is_not_in_offsetDateTimes_as_string_array_parameter() {
    assertThat(REFERENCE).isNotIn(AFTER.toString(), OFFSET_REFERENCE.toString());
  }

  @Test
  public void should_fail_if_actual_is_in_offsetDateTimes_as_string_array_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isNotIn(REFERENCE.toString(), AFTER.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldNotBeIn(REFERENCE, asList(REFERENCE, AFTER)).create());
  }

  @Test
  public void should_fail_if_offsetDateTimes_as_string_array_parameter_is_null() {
    // GIVEN
    String[] otherOffsetDateTimesAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(OffsetDateTime.now()).isNotIn(otherOffsetDateTimesAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The given OffsetDateTime array should not be null");
  }

  @Test
  public void should_fail_if_offsetDateTimes_as_string_array_parameter_is_empty() {
    // GIVEN
    String[] otherOffsetDateTimesAsString = new String[0];
    // WHEN
    ThrowingCallable code = () -> assertThat(OffsetDateTime.now()).isNotIn(otherOffsetDateTimesAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The given OffsetDateTime array should not be empty");
  }

}
