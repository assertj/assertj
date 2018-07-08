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
package org.assertj.core.api.localdatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.Test;

/**
 * Only test String based assertion (tests with {@link java.time.LocalDateTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class LocalDateTimeAssert_isEqualTo_Test extends LocalDateTimeAssertBaseTest {

  @Test
  public void test_isEqualTo_assertion() {
    // WHEN
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
    // THEN
    assertThatThrownBy(() -> assertThat(REFERENCE).isEqualTo(REFERENCE.plusSeconds(1)
                                                                      .toString())).isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isEqualTo_assertion_error_message() {
    thrown.expectAssertionError("expected:<20[12-01-01T03:03:03]> but was:<20[00-01-05T03:00:05]>");
    assertThat(LocalDateTime.of(2000, 1, 5, 3, 0, 5)).isEqualTo(LocalDateTime.of(2012, 1, 1, 3, 3, 3).toString());
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
        "The String representing the LocalDateTime to compare actual with should not be null");
    assertThat(LocalDateTime.now()).isEqualTo((String) null);
  }

}
