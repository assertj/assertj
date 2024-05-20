/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.localdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.time.LocalDate;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link java.time.LocalDate} are already defined in assertj-core)
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
class LocalDateAssert_isEqualTo_Test extends LocalDateAssertBaseTest {

  @Test
  void should_pass_if_actual_is_equal_to_date_as_string_parameter() {
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
  }

  @Test
  void should_fail_if_actual_is_not_equal_to_date_as_string_parameter() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(AFTER).isEqualTo(REFERENCE.toString()));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage(AFTER + " (java.time.LocalDate)", REFERENCE + " (java.time.LocalDate)"));
  }

  @Test
  void should_fail_if_date_as_string_parameter_is_null() {
    // GIVEN
    String otherLocalDateAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalDate.now()).isEqualTo(otherLocalDateAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the LocalDate to compare actual with should not be null");
  }

}
