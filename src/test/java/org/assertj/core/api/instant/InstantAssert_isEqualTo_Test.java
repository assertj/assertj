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
package org.assertj.core.api.instant;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(Theories.class)
public class InstantAssert_isEqualTo_Test extends InstantAssertBaseTest {

  @Theory
  public void test_isEqualTo_assertion(Instant referenceDate) {
    // WHEN
    assertThat(referenceDate).isEqualTo(referenceDate.toString());
    // THEN
    assertThatThrownBy(() -> assertThat(referenceDate).isEqualTo(referenceDate.plusSeconds(1)
                                                                              .toString())).isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isEqualTo_assertion_error_message() {
    Instant instantReference = Instant.parse("2007-12-03T10:15:30.00Z");
    Instant instantAfter = Instant.parse("2007-12-03T10:15:35.00Z");
    thrown.expectAssertionError("expected:<2007-12-03T10:15:3[5]Z> but was:<2007-12-03T10:15:3[0]Z>");
    assertThat(instantReference).isEqualTo(instantAfter.toString());
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(Instant.now()).isEqualTo((String) null))
                                        .withMessage("The String representing the Instant to compare actual with should not be null");
  }

}
