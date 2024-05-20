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
package org.assertj.core.api.boolean_;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class BooleanAssert_isNotEqualTo_boolean_Test {

  @Test
  void should_pass_if_actual_is_null_since_the_other_argument_cannot_be_null() {
    Boolean actual = null;
    assertThat(actual).isNotEqualTo(false);
    assertThat(actual).isNotEqualTo(FALSE);
    assertThat(actual).isNotEqualTo(true);
    assertThat(actual).isNotEqualTo(TRUE);
  }

  @Test
  void should_pass_if_booleans_are_not_equal() {
    assertThat(true).isNotEqualTo(FALSE);
    assertThat(true).isNotEqualTo(false);
    assertThat(TRUE).isNotEqualTo(FALSE);
    assertThat(TRUE).isNotEqualTo(false);
    assertThat(false).isNotEqualTo(true);
    assertThat(false).isNotEqualTo(TRUE);
    assertThat(FALSE).isNotEqualTo(true);
    assertThat(FALSE).isNotEqualTo(TRUE);
  }

  @Test
  void should_fail_if_booleans_are_equal() {
    // GIVEN
    boolean actual = TRUE;
    boolean expected = true;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqual(actual, expected).create());
  }
}
