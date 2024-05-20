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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BDD Soft assertions wasSuccess")
class BDDSoftAssertions_wasSuccess_Test extends BaseAssertionsTest {

  private BDDSoftAssertions softly;

  @BeforeEach
  void setup() {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
    softly = new BDDSoftAssertions();
  }

  @Test
  void should_return_success_of_last_assertion() {
    softly.then(true).isFalse();
    softly.then(true).isEqualTo(true);
    then(softly.wasSuccess()).isTrue();
  }

  @Test
  void should_return_success_of_last_assertion_with_nested_calls() {
    softly.then(true).isFalse();
    softly.then(true).isTrue(); // isTrue() calls isEqualTo(true)
    then(softly.wasSuccess()).isTrue();
  }

  @Test
  void should_return_failure_of_last_assertion() {
    softly.then(true).isTrue();
    softly.then(true).isEqualTo(false);
    then(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_of_last_assertion_with_multilple_nested_calls() {
    softly.then(true).isTrue();
    softly.then(true).isFalse(); // isFalse() calls isEqualTo(false)
    then(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_of_last_assertion_with_nested_calls() {
    // scenario to avoid:
    // -- softly.then(true).isFalse()
    // ----- proxied isFalse() -> calls isEqualTo(false) which is proxied
    // ------- proxied isEqualTo(false) : catch AssertionError => wasSuccess = false, back to outer call
    // ---- proxied isFalse() : no AssertionError caught => last result success = true
    softly.then(true).isFalse();
    then(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_after_fail() {
    // GIVEN
    String failureMessage = "Should not reach here";
    // WHEN
    softly.fail(failureMessage);
    // THEN
    then(softly.wasSuccess()).isFalse();
    then(softly.errorsCollected()).hasSize(1);
    then(softly.errorsCollected().get(0)).hasMessageStartingWith(failureMessage);
  }

  @Test
  void should_return_failure_after_fail_with_parameters() {
    // GIVEN
    String failureMessage = "Should not reach %s or %s";
    // WHEN
    softly.fail(failureMessage, "here", "here");
    // THEN
    then(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_after_fail_with_throwable() {
    // GIVEN
    String failureMessage = "Should not reach here";
    IllegalStateException realCause = new IllegalStateException();
    // WHEN
    softly.fail(failureMessage, realCause);
    // THEN
    then(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_after_shouldHaveThrown() {
    // WHEN
    softly.shouldHaveThrown(IllegalArgumentException.class);
    // THEN
    then(softly.wasSuccess()).isFalse();
  }
}
