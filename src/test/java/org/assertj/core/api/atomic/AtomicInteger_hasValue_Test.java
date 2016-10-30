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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.atomic;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.AtomicShouldContain.shouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;

public class AtomicInteger_hasValue_Test extends BaseTest {

  @Test
  public void should_fail_when_optional_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());

    assertThat((AtomicInteger)null).hasValue(1234);
  }

  @Test
  public void should_fail_if_expected_value_is_null() throws Exception {
    thrown.expectIllegalArgumentException("The expected value should not be <null>.");

    assertThat(new AtomicInteger(1234)).hasValue(null);
  }

  @Test
  public void should_fail_if_atomicInteger_does_not_contain_expected_value() throws Exception {
    AtomicInteger actual = new AtomicInteger(1234);
    int expectedValue = 5678;

    thrown.expectAssertionError(shouldContain(actual.get(), expectedValue).create());

    assertThat(actual).hasValue(expectedValue);
  }

  @Test
  public void should_pass_if_atomicInteger_contains_expected_value() throws Exception {
    assertThat(new AtomicInteger(1234)).hasValue(1234);
  }

}
