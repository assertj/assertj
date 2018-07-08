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


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;

import org.junit.Test;

public class InstantAssert_isNotIn_Test extends InstantAssertBaseTest {

  @Test
  public void test_isNotIn_assertion() {
    // WHEN
    assertThat(REFERENCE).isNotIn(REFERENCE.plusSeconds(1).toString(), REFERENCE.plusSeconds(2).toString());
    // THEN
    assertThatThrownBy(() -> assertThat(REFERENCE).isNotIn(REFERENCE.toString(), REFERENCE.plusSeconds(1).toString())).isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isNotIn_assertion_error_message() {
    Instant instantReference = Instant.parse("2007-12-03T10:15:30.00Z");
    Instant instantAfter = Instant.parse("2007-12-03T10:15:35.00Z");
    thrown.expectAssertionError("%nExpecting:%n <2007-12-03T10:15:30Z>%nnot to be in:%n <[2007-12-03T10:15:30Z, 2007-12-03T10:15:35Z]>%n");
    assertThat(instantReference).isNotIn(instantReference.toString(), instantAfter.toString());
  }

  @Test
  public void should_fail_if_dates_as_string_array_parameter_is_null() {
    expectException(IllegalArgumentException.class, "The given Instant array should not be null");
    assertThat(Instant.now()).isNotIn((String[]) null);
  }

  @Test
  public void should_fail_if_dates_as_string_array_parameter_is_empty() {
    expectException(IllegalArgumentException.class, "The given Instant array should not be empty");
    assertThat(Instant.now()).isNotIn(new String[0]);
  }

}
