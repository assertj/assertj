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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SoftAssertions_assertAlso_Test extends BaseAssertionsTest {

  private SoftAssertions softly;

  @BeforeEach
  void beforeEachTest() {
    softly = new SoftAssertions();
  }

  @Test
  void all_composed_assertions_should_pass() {
    // GIVEN
    SoftAssertions delegate = new SoftAssertions();
    delegate.assertThat(1).isEqualTo(1);
    delegate.assertAll();
    softly.assertThat(1).isEqualTo(1);
    softly.assertThat(list(1, 2)).containsOnly(1, 2);
    // WHEN
    softly.assertAlso(delegate);
    softly.assertAll();
    // THEN
    assertThat(softly.wasSuccess()).isTrue();
  }

  @Test
  void should_return_failure_for_failed_composed_assertions() {
    // GIVEN some soft assertions
    softly.assertThat(1).isEqualTo(1);
    assertThat(softly.wasSuccess()).isTrue();
    softly.assertThat(1).isEqualTo(3);
    assertThat(softly.wasSuccess()).isFalse();
    // and other ones
    SoftAssertions other = new SoftAssertions();
    other.assertThat(1).isEqualTo(2);
    other.assertThat(list(1, 2)).containsOnly(1, 4);
    assertThat(other.wasSuccess()).isFalse();
    assertThat(other.errorsCollected()).hasSize(2);
    // WHEN
    softly.assertAlso(other);
    // THEN
    assertThat(softly.wasSuccess()).isFalse();
    assertThat(softly.errorsCollected()).hasSize(3);
  }

}
