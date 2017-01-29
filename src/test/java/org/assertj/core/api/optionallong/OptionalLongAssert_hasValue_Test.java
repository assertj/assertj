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
package org.assertj.core.api.optionallong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.OptionalLong;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OptionalLongAssert_hasValue_Test extends BaseTest {

  @Test
  public void should_fail_when_OptionalLong_is_null() {
    thrown.expectAssertionError(actualIsNull());

    assertThat((OptionalLong) null).hasValue(10L);
  }

  @Test
  public void should_pass_if_OptionalLong_has_expected_value() {
    assertThat(OptionalLong.of(10L)).hasValue(10L);
  }

  @Test
  public void should_fail_if_OptionalLong_does_not_have_expected_value() {
    OptionalLong actual = OptionalLong.of(5L);
    long expectedValue = 10L;

    thrown.expectAssertionError(shouldContain(actual, expectedValue).create());

    assertThat(actual).hasValue(expectedValue);
  }

  @Test
  public void should_fail_if_OptionalLong_is_empty() {
    long expectedValue = 10L;

    thrown.expectAssertionError(shouldContain(expectedValue).create());

    assertThat(OptionalLong.empty()).hasValue(expectedValue);
  }
}
