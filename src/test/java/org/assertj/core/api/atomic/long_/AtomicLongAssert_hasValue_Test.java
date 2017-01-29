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
package org.assertj.core.api.atomic.long_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.atomic.AtomicLong;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class AtomicLongAssert_hasValue_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_pass_when_actual_has_the_expected_value() {
    long initialValue = 123L;
    AtomicLong actual = new AtomicLong(initialValue);
    assertThat(actual).hasValue(initialValue);
  }

  @Test
  public void should_fail_when_actual_does_not_have_the_expected_value() {
    AtomicLong actual = new AtomicLong(123L);
    long expectedValue = 1234L;
    thrown.expectAssertionError(shouldHaveValue(actual, expectedValue).create());
    assertThat(actual).hasValue(expectedValue);
  }

  @Test
  public void should_fail_when_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    AtomicLong actual = null;
    assertThat(actual).hasValue(1234L);
  }
  
}
