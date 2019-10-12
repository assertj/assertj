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
import static org.assertj.core.api.AbstractOffsetDateTimeAssert.NULL_OFFSET_DATE_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

class OffsetDateTimeAssert_isAtSameInstantAs_Test extends OffsetDateTimeAssertBaseTest {
  @Test
  void should_pass_if_at_the_same_instant() {
    assertThat(REFERENCE).isAtSameInstantAs(SAME_INSTANT);
  }

  @Test
  void should_fail_if_at_a_different_instant() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(REFERENCE).isAtSameInstantAs(DIFFERENT_INSTANT))
                                                   .withMessage(format("%n" +
                                                                       "Expecting:%n" +
                                                                       " <2000-12-14T00:00:00Z>%n" +
                                                                       "to be equal to:%n" +
                                                                       " <2000-12-14T03:00:00Z>%n" +
                                                                       "but was not."));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      OffsetDateTime actual = null;
      assertThat(actual).isAtSameInstantAs(OffsetDateTime.now());
    }).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_OffsetDateTime_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(REFERENCE).isAtSameInstantAs(null))
                                        .withMessage(NULL_OFFSET_DATE_TIME_PARAMETER_MESSAGE);
  }
}
