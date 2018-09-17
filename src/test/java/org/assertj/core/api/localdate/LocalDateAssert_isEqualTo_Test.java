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
package org.assertj.core.api.localdate;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link java.time.LocalDate} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class LocalDateAssert_isEqualTo_Test extends LocalDateAssertBaseTest {

  @Test
  public void test_isEqualTo_assertion() {
    // WHEN
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
    // THEN
    assertThatThrownBy(() -> assertThat(REFERENCE).isEqualTo(REFERENCE.plusDays(1)
                                                                      .toString())).isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isEqualTo_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(LocalDate.of(2000, 1, 5)).isEqualTo(LocalDate.of(2012, 1, 1).toString()))
                                                   .withMessage(format("%nExpecting:%n <2000-01-05>%nto be equal to:%n <2012-01-01>%nbut was not."));
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(LocalDate.now()).isEqualTo((String) null))
                                        .withMessage("The String representing the LocalDate to compare actual with should not be null");
  }

}
