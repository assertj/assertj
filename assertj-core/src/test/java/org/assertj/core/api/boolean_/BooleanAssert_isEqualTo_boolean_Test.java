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
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class BooleanAssert_isEqualTo_boolean_Test {

  @Test
  void should_fail_if_actual_is_null_since_the_expected_argument_cannot_be_null() {
    // GIVEN
    Boolean actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualTo(true));
    // THEN
    then(assertionError).hasMessage(format("%n" +
                                           "expected: true%n" +
                                           " but was: null"));
  }

  @Test
  void should_pass_if_booleans_are_equal() {
    assertThat(true).isEqualTo(true);
    assertThat(true).isEqualTo(TRUE);
    assertThat(TRUE).isEqualTo(true);
    assertThat(TRUE).isEqualTo(TRUE);
    assertThat(false).isEqualTo(false);
    assertThat(false).isEqualTo(FALSE);
    assertThat(FALSE).isEqualTo(false);
    assertThat(FALSE).isEqualTo(FALSE);
  }

  @Test
  void should_fail_if_booleans_are_not_equal() {
    // GIVEN
    boolean actual = false;
    boolean expected = true;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(format("%n" +
                                           "expected: true%n" +
                                           " but was: false"));
  }
}
