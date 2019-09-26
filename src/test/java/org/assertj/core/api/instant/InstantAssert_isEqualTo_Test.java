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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

import java.time.Instant;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InstantAssert isEqualTo")
public class InstantAssert_isEqualTo_Test extends InstantAssertBaseTest {

  @Test
  public void should_pass_if_actual_is_equal_to_date_as_string_parameter() {
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_date_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isEqualTo(AFTER.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(format("%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.",
                                                                REFERENCE, AFTER));
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    // GIVEN
    String otherInstantAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(Instant.now()).isEqualTo(otherInstantAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the Instant to compare actual with should not be null");
  }

}
