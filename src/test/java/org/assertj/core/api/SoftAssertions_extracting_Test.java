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
import static org.assertj.core.test.Name.name;

import org.assertj.core.test.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SoftAssertions_extracting_Test extends BaseAssertionsTest {

  private SoftAssertions softly;

  @BeforeEach
  void beforeEachTest() {
    softly = new SoftAssertions();
  }

  @Test
  void should_throw_assertion_error_when_extracting_from_null() {
    // GIVEN
    Name nameObject = name("John", "Doe");
    Name nameNull = null;
    // WHEN
    softly.assertThat(nameObject).extracting(Name::getFirst).isEqualTo("John");
    softly.assertThat(nameNull).extracting(Name::getFirst);
    // THEN
    assertThat(softly.errorsCollected()).hasSize(1);
  }

}
