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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class LocalTimeAssert_isEqualTo_Test extends LocalTimeAssertBaseTest {

  @Test
  public void test_isEqualTo_assertion() {
    // WHEN
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
    // THEN
    assertThatThrownBy(() -> assertThat(REFERENCE).isEqualTo(REFERENCE.plusHours(1).toString())).isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isEqualTo_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(LocalTime.of(3, 0, 5)).isEqualTo("03:03:03"))
                                                   .withMessage("expected:<03:0[3:03]> but was:<03:0[0:05]>");
  }

  @Test
  public void should_fail_if_timeTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(LocalTime.now()).isEqualTo((String) null))
                                        .withMessage("The String representing the LocalTime to compare actual with should not be null");
  }

}
