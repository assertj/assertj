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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Soft assertions wasSuccess")
class SoftAssertions_wasSuccess_Test extends BaseAssertionsTest {

  private SoftAssertions softly;

  @BeforeEach
  void setup() {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
    softly = new SoftAssertions();
  }

  @Test
  void should_return_success_of_last_assertion() {
    softly.assertThat(true).isFalse();
    softly.assertThat(true).isEqualTo(true);
    assertThat(softly.wasSuccess()).isTrue();
  }

  @Test
  void should_return_success_of_last_assertion_with_nested_calls() {
    softly.assertThat(true).isFalse();
    softly.assertThat(true).isTrue(); // isTrue() calls isEqualTo(true)
    assertThat(softly.wasSuccess()).isTrue();
  }

  @Test
  void should_return_failure_of_last_assertion() {
    softly.assertThat(true).isTrue();
    softly.assertThat(true).isEqualTo(false);
    assertThat(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_of_last_assertion_with_multilple_nested_calls() {
    softly.assertThat(true).isTrue();
    softly.assertThat(true).isFalse(); // isFalse() calls isEqualTo(false)
    assertThat(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_of_last_assertion_with_nested_calls() {
    // scenario to avoid:
    // -- softly.assertThat(true).isFalse()
    // ----- proxied isFalse() -> calls isEqualTo(false) which is proxied
    // ------- proxied isEqualTo(false) : catch AssertionError => wasSuccess = false, back to outer call
    // ---- proxied isFalse() : no AssertionError caught => last result success = true
    softly.assertThat(true).isFalse();
    assertThat(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_after_fail() {
    // GIVEN
    String failureMessage = "Should not reach here";
    // WHEN
    softly.fail(failureMessage);
    // THEN
    assertThat(softly.wasSuccess()).isFalse();
    assertThat(softly.errorsCollected()).hasSize(1);
    assertThat(softly.errorsCollected().get(0)).hasMessageStartingWith(failureMessage);
  }

  @Test
  void should_return_failure_after_fail_with_parameters() {
    // GIVEN
    String failureMessage = "Should not reach %s or %s";
    // WHEN
    softly.fail(failureMessage, "here", "here");
    // THEN
    assertThat(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_after_fail_with_throwable() {
    // GIVEN
    String failureMessage = "Should not reach here";
    IllegalStateException realCause = new IllegalStateException();
    // WHEN
    softly.fail(failureMessage, realCause);
    // THEN
    assertThat(softly.wasSuccess()).isFalse();
  }

  @Test
  void should_return_failure_after_shouldHaveThrown() {
    // WHEN
    softly.shouldHaveThrown(IllegalArgumentException.class);
    // THEN
    assertThat(softly.wasSuccess()).isFalse();
  }
}
