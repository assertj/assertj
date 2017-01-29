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
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.error.OptionalShouldContain.shouldContainSame;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OptionalAssert_containsSame_Test extends BaseTest {

  @Test
  public void should_fail_when_optional_is_null() {
    thrown.expectAssertionError(actualIsNull());

    assertThat((Optional<String>) null).containsSame("something");
  }

  @Test
  public void should_fail_if_expected_value_is_null() {
    thrown.expectIllegalArgumentException("The expected value should not be <null>.");

    assertThat(Optional.of("something")).containsSame(null);
  }

  @Test
  public void should_pass_if_optional_contains_the_expected_object_reference() {
    String containedAndExpected = "something";

    assertThat(Optional.of(containedAndExpected)).containsSame(containedAndExpected);
  }

  @Test
  public void should_fail_if_optional_does_not_contain_the_expected_object_reference() {
    Optional<String> actual = Optional.of("not-expected");
    String expectedValue = "something";

    thrown.expectAssertionError(shouldContainSame(actual, expectedValue).create());

    assertThat(actual).containsSame(expectedValue);
  }

  @Test
  public void should_fail_if_optional_contains_equal_but_not_same_value() {
    Optional<String> actual = Optional.of(new String("something"));
    String expectedValue = new String("something");

    thrown.expectAssertionError(shouldContainSame(actual, expectedValue).create());

    assertThat(actual).containsSame(expectedValue);
  }

  @Test
  public void should_fail_if_optional_is_empty() {
    String expectedValue = "something";

    thrown.expectAssertionError(shouldContain(expectedValue).create());

    assertThat(Optional.empty()).containsSame(expectedValue);
  }
}
