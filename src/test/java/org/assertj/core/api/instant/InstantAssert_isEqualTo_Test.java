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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

public class InstantAssert_isEqualTo_Test extends InstantAssertBaseTest {

  @Test
  public void test_isEqualTo_assertion() {
    // WHEN
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
    // WHEN
    ThrowingCallable assertionCode = () -> assertThat(REFERENCE).isEqualTo(REFERENCE.plusSeconds(1).toString());
    // THEN
    assertThatThrownBy(assertionCode).isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isEqualTo_assertion_error_message() {
    Instant instantReference = Instant.parse("2007-12-03T10:15:30.00Z");
    Instant instantAfter = Instant.parse("2007-12-03T10:15:35.00Z");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(instantReference).isEqualTo(instantAfter.toString()))
                                                   .withMessage(format("%nExpecting:%n <2007-12-03T10:15:30Z>%nto be equal to:%n <2007-12-03T10:15:35Z>%nbut was not."));
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(Instant.now()).isEqualTo((String) null))
                                        .withMessage("The String representing the Instant to compare actual with should not be null");
  }

}
