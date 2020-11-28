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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.atomic.reference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

class AtomicReferenceAssert_hasValueSatisfying_Test {

  @Test
  void should_pass_when_actual_has_value_satisfying_given_requirements() {
    // GIVEN
    String initialValue = "FOO";
    String expectedValue = "foo";
    AtomicReference<String> actual = new AtomicReference<>(initialValue);
    // WHEN/THEN
    then(actual).hasValueSatisfying(value -> assertThat(value).isEqualToIgnoringCase(expectedValue));
  }

  @Test
  void should_fail_when_actual_has_value_which_does_not_satisfy_given_requirements() {
    // GIVEN
    String initialValue = "foo";
    String expectedValue = "bar";
    AtomicReference<String> actual = new AtomicReference<>(initialValue);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasValueSatisfying(value -> assertThat(value).isEqualToIgnoringCase(expectedValue)));
    // THEN
    then(error).hasMessageContainingAll("<\"" + initialValue + "\">",
                                        "<\"" + expectedValue + "\">");
  }

  @Test
  void should_fail_when_requirements_is_null() {
    // GIVEN
    String initialValue = "foo";
    AtomicReference<String> actual = new AtomicReference<>(initialValue);
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasValueSatisfying(null));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessageContaining("The Consumer<? super V> expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_when_actual_is_null() {
    // GIVEN
    String expectedValue = "foo";
    AtomicReference<String> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasValueSatisfying(value -> assertThat(value).isEqualToIgnoringCase(expectedValue)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
