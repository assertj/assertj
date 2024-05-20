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
package org.assertj.core.api.offsettime;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.time.OffsetTime;
import org.junit.jupiter.api.Test;

class OffsetTimeAssert_isEqualTo_Test extends OffsetTimeAssertBaseTest {

  @Test
  void test_isEqualTo_assertion() {
    // WHEN
    assertThat(REFERENCE).isEqualTo(REFERENCE);
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
    // THEN
    expectAssertionError(() -> assertThat(REFERENCE).isEqualTo(REFERENCE.plusHours(1).toString()));
  }

  @Test
  void test_isEqualTo_assertion_error_message() {
    // GIVEN
    OffsetTime time = OffsetTime.of(3, 0, 5, 0, UTC);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(time).isEqualTo("03:03:03Z"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("03:00:05Z", "03:03:03Z"));
  }

  @Test
  void should_fail_if_offsetTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(OffsetTime.now()).isEqualTo((String) null))
                                        .withMessage("The String representing the OffsetTime to compare actual with should not be null");
  }
}
