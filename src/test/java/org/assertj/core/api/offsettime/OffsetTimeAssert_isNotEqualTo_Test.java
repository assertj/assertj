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
package org.assertj.core.api.offsettime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * Only test String based assertion (tests with {@link java.time.OffsetTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@RunWith(Theories.class)
public class OffsetTimeAssert_isNotEqualTo_Test extends OffsetTimeAssertBaseTest {

  @Theory
  public void test_isNotEqualTo_assertion(OffsetTime referenceTime) {
    // WHEN
    assertThat(referenceTime).isNotEqualTo(referenceTime.plusHours(1).toString());
    // THEN
    verify_that_isNotEqualTo_assertion_fails_and_throws_AssertionError(referenceTime);
  }

  @Test
  public void test_isNotEqualTo_assertion_error_message() {
    thrown.expectAssertionError("%n" +
                                "Expecting:%n" +
                                " <03:00:05Z>%n" +
                                "not to be equal to:%n" +
                                " <03:00:05Z>%n");
    assertThat(OffsetTime.of(3, 0, 5, 0, ZoneOffset.UTC)).isNotEqualTo("03:00:05Z");
  }

  @Test
  public void should_fail_if_offsetTime_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
                    "The String representing the OffsetTime to compare actual with should not be null");
    assertThat(OffsetTime.now()).isNotEqualTo((String) null);
  }

  private static void verify_that_isNotEqualTo_assertion_fails_and_throws_AssertionError(OffsetTime reference) {
    try {
      assertThat(reference).isNotEqualTo(reference.toString());
    } catch (AssertionError e) {
      // AssertionError was expected
      return;
    }
    fail("Should have thrown AssertionError");
  }

}
