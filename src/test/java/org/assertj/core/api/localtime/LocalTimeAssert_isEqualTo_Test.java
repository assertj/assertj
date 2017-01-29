/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.localtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.time.LocalTime;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class LocalTimeAssert_isEqualTo_Test extends LocalTimeAssertBaseTest {

  @Theory
  public void test_isEqualTo_assertion(LocalTime referenceTime) {
    // WHEN
    assertThat(referenceTime).isEqualTo(referenceTime.toString());
    // THEN
    verify_that_isEqualTo_assertion_fails_and_throws_AssertionError(referenceTime);
  }

  @Test
  public void test_isEqualTo_assertion_error_message() {
    thrown.expectAssertionError("expected:<03:0[3:03]> but was:<03:0[0:05]>");
    assertThat(LocalTime.of(3, 0, 5)).isEqualTo("03:03:03");
  }

  @Test
  public void should_fail_if_timeTime_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
        "The String representing the LocalTime to compare actual with should not be null");
    assertThat(LocalTime.now()).isEqualTo((String) null);
  }

  private static void verify_that_isEqualTo_assertion_fails_and_throws_AssertionError(LocalTime reference) {
    try {
      assertThat(reference).isEqualTo(reference.plusHours(1).toString());
    } catch (AssertionError e) {
      // AssertionError was expected
      return;
    }
    fail("Should have thrown AssertionError");
  }

}
