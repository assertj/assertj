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
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultAssertionErrorCollector_register_AfterAssertionErrorCollected_Test {
  private List<String> errorMessages;
  private List<Throwable> throwables;
  private SoftAssertions softly;

  @BeforeEach
  void given() {
    errorMessages = new ArrayList<>();
    throwables = new ArrayList<>();
    softly = new SoftAssertions();
  }

  @Test
  void should_perform_both_specified_actions_on_each_assertion_error() {
    // WHEN
    softly.addAfterAssertionErrorCollected(err -> errorMessages.add(err.getMessage()));
    softly.addAfterAssertionErrorCollected(throwables::add);
    softly.assertThat(1).isEqualTo(1_000).isBetween(7, 15).isEven();
    // THEN
    then(errorMessages).hasSameSizeAs(throwables).hasSize(3);
  }

  @Test
  void should_execute_both_specified_actions_on_each_manually_added_error() {
    // WHEN
    softly.addAfterAssertionErrorCollected(err -> errorMessages.add(err.getMessage()));
    softly.addAfterAssertionErrorCollected(throwables::add);
    softly.collectAssertionError(new AssertionError("hello"));
    softly.collectAssertionError(new AssertionError("world"));
    // THEN
    then(errorMessages).hasSameSizeAs(throwables)
                       .hasSize(2);
  }

  @Test
  void should_register_the_same_callback_several_times() {
    // GIVEN
    AfterAssertionErrorCollected callback = throwables::add;
    // WHEN
    for (int i = 0; i < 10; i++) {
      softly.addAfterAssertionErrorCollected(callback);
    }
    softly.collectAssertionError(new AssertionError("hello"));
    // THEN
    then(throwables).hasSize(10);
  }

  @Test
  void setAfterAssertionErrorCollected_should_replace_all_registered_callbacks_by_the_one_specified() {
    // GIVEN
    softly.addAfterAssertionErrorCollected(err -> errorMessages.add(err.getMessage() + "1"));
    softly.addAfterAssertionErrorCollected(err -> errorMessages.add(err.getMessage() + "2"));
    softly.addAfterAssertionErrorCollected(err -> errorMessages.add(err.getMessage() + "3"));
    // WHEN
    softly.setAfterAssertionErrorCollected(throwables::add);
    softly.collectAssertionError(new AssertionError("hello"));
    // THEN
    then(errorMessages).isEmpty();
    then(throwables).hasSize(1);
  }
}
