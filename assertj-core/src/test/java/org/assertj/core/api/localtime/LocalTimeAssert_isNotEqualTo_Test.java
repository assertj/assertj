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
package org.assertj.core.api.localtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

import java.time.LocalTime;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link LocalTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@DisplayName("LocalTimeAssert isNotEqualTo")
class LocalTimeAssert_isNotEqualTo_Test extends LocalTimeAssertBaseTest {

  @Test
  void should_pass_if_actual_is_not_equal_to_localTime_as_string_parameter() {
    assertThat(REFERENCE).isNotEqualTo(AFTER.toString());
  }

  @Test
  void should_fail_if_actual_is_equal_to_localTime_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isNotEqualTo(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldNotBeEqual(REFERENCE, REFERENCE).create());
  }

  @Test
  void should_fail_if_localTime_as_string_parameter_is_null() {
    // GIVEN
    String otherLocalTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalTime.now()).isNotEqualTo(otherLocalTimeAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the LocalTime to compare actual with should not be null");
  }

}
