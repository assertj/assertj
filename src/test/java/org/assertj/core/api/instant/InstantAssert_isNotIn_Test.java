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
package org.assertj.core.api.instant;


import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class InstantAssert_isNotIn_Test extends InstantAssertBaseTest {

  @Test
  public void test_isNotIn_assertion() {
    // WHEN
    assertThat(REFERENCE).isNotIn(REFERENCE.plusSeconds(1).toString(), REFERENCE.plusSeconds(2).toString());
    // THEN
    assertThatThrownBy(() -> assertThat(REFERENCE).isNotIn(REFERENCE.toString(), REFERENCE.plusSeconds(1).toString())).isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isNotIn_assertion_error_message() {
    Instant instantReference = Instant.parse("2007-12-03T10:15:30.00Z");
    Instant instantAfter = Instant.parse("2007-12-03T10:15:35.00Z");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(instantReference).isNotIn(instantReference.toString(), instantAfter.toString()))
                                                   .withMessage(format("%nExpecting:%n <2007-12-03T10:15:30Z>%nnot to be in:%n <[2007-12-03T10:15:30Z, 2007-12-03T10:15:35Z]>%n"));
  }

  @Test
  public void should_fail_if_dates_as_string_array_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(Instant.now()).isNotIn((String[]) null))
                                        .withMessage("The given Instant array should not be null");
  }

  @Test
  public void should_fail_if_dates_as_string_array_parameter_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(Instant.now()).isNotIn(new String[0]))
                                        .withMessage("The given Instant array should not be empty");
  }

}
