/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.guava.api;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainKeys.shouldNotContainKeys;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.set;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.tests.guava.testkit.AssertionErrors.expectAssertionError;

import com.google.common.collect.LinkedHashMultimap;
import org.junit.jupiter.api.Test;

class MultimapAssert_doesNotContainKeys_Test extends MultimapAssertBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).doesNotContainKeys("Nets", "Bulls", "Knicks"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_does_not_contains_given_key() {
    // WHEN/THEN
    assertThat(actual).doesNotContainKeys("apples");
  }

  @Test
  void should_pass_if_actual_does_not_contains_given_keys() {
    // WHEN/THEN
    assertThat(actual).doesNotContainKeys("apples", "oranges");
  }

  @Test
  void should_fail_with_null_key() {
    // GIVEN
    actual = LinkedHashMultimap.create();
    actual.put(null, "apples");
    String key = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).doesNotContainKey(key));
    // THEN
    then(error).hasMessage(shouldNotContainKeys(actual, set(key)).create());
  }

  @Test
  void should_fail_with_null_key_in_array() {
    // GIVEN
    actual = LinkedHashMultimap.create();
    actual.put(null, "apples");
    String key = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).doesNotContainKeys("cheese", key));
    // THEN
    then(error).hasMessage(shouldNotContainKeys(actual, set(key)).create());
  }

  @Test
  void should_fail_with_null_array() {
    // GIVEN
    String[] keys = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).doesNotContainKeys(keys));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The array of keys to look for should not be null");
  }

  @Test
  void should_fail_if_single_key_is_present() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).doesNotContainKey("Bulls"));
    // THEN
    then(error).hasMessage(shouldNotContainKeys(actual, set("Bulls")).create());
  }

  @Test
  void should_fail_if_one_key_is_present() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).doesNotContainKeys("Bulls", "Knicks"));
    // THEN
    then(error).hasMessage(shouldNotContainKeys(actual, set("Bulls")).create());
  }

  @Test
  void should_fail_if_multiple_keys_are_present() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).doesNotContainKeys("Bulls", "Knicks", "Spurs"));
    // THEN
    then(error).hasMessage(shouldNotContainKeys(actual, set("Bulls", "Spurs")).create());
  }

}
