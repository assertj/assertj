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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.abstract_; // Make sure that package-private access is lost

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.StandardSoftAssertionsProvider;
import org.junit.jupiter.api.Test;

/**
 * This tests that classes extended from {@link StandardSoftAssertionsProvider} will have access to the list of
 * collected errors that the various proxies have collected.
 */
class SoftAssertionsErrorsCollectedTest {
  private final SoftAssertions softly = new SoftAssertions();

  @Test
  void return_empty_list_of_errors() {
    // GIVEN
    Object objectToTest = null;
    // WHEN
    softly.assertThat(objectToTest).isNull();
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(softly.errorsCollected()).isEqualTo(softly.assertionErrorsCollected());
  }

  @Test
  void returns_nonempty_list_of_errors() {
    // GIVEN
    Object objectToTest = null;
    // WHEN
    softly.assertThat(objectToTest).isNotNull(); // This should allow something to be collected
    // THEN
    assertThat(softly.errorsCollected()).hasAtLeastOneElementOfType(AssertionError.class)
                                        .isEqualTo(softly.assertionErrorsCollected());
  }
}
