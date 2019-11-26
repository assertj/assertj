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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
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
@DisplayName("OffsetDateTimeAssert isEqualTo")
public class OffsetDateTimeAssert_isEqualTo_Test extends OffsetDateTimeAssertBaseTest {

  @Test
  public void should_pass_if_actual_is_equal_to_offsetDateTime_as_string_parameter() {
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
  }

  @Test
  public void should_fail_if_actual_is_at_same_instant_as_offsetDateTime_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(OFFSET_REFERENCE).isEqualTo(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(format("%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.",
                                                                OFFSET_REFERENCE, REFERENCE));
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_offsetDateTime_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(AFTER).isEqualTo(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(format("%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.",
                                                                AFTER, REFERENCE));
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_offsetDateTime_as_string_parameter_with_different_offset() {
    // WHEN
    ThrowingCallable code = () -> assertThat(OFFSET_AFTER).isEqualTo(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(format("%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.",
                                                                OFFSET_AFTER, REFERENCE));
  }

  @Test
  public void should_fail_if_offsetDateTime_as_string_parameter_is_null() {
    // GIVEN
    String otherOffsetDateTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(OffsetDateTime.now()).isEqualTo(otherOffsetDateTimeAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the OffsetDateTime to compare actual with should not be null");
  }

}
