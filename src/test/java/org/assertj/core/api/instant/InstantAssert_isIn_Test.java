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
package org.assertj.core.api.instant;


import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Theories.class)
public class InstantAssert_isIn_Test  extends InstantAssertBaseTest {

  @Theory
  public void test_isIn_assertion(Instant referenceDate) {
    // WHEN
    assertThat(referenceDate).isIn(referenceDate.toString(), referenceDate.plusSeconds(1).toString());
    // THEN
    verify_that_isIn_assertion_fails_and_throws_AssertionError(referenceDate);
  }

  @Test
  public void test_isIn_assertion_error_message() {
    Instant instantReference = Instant.parse("2007-12-03T10:15:30.00Z");
    Instant instantAfter = Instant.parse("2007-12-03T10:15:35.00Z");
    thrown.expectAssertionError("%nExpecting:%n <2007-12-03T10:15:30Z>%nto be in:%n <[2007-12-03T10:15:35Z]>%n");
    assertThat(instantReference).isIn(instantAfter.toString());
  }

  @Test
  public void should_fail_if_dates_as_string_array_parameter_is_null() {
    expectException(IllegalArgumentException.class, "The given Instant array should not be null");
    assertThat(Instant.now()).isIn((String[]) null);
  }

  @Test
  public void should_fail_if_dates_as_string_array_parameter_is_empty() {
    expectException(IllegalArgumentException.class, "The given Instant array should not be empty");
    assertThat(Instant.now()).isIn(new String[0]);
  }

  private static void verify_that_isIn_assertion_fails_and_throws_AssertionError(Instant reference) {
    try {
      assertThat(reference).isIn(reference.plusSeconds(1).toString(), reference.plusSeconds(2).toString());
    } catch (AssertionError e) {
      // AssertionError was expected
      return;
    }
    fail("Should have thrown AssertionError");
  }

}
