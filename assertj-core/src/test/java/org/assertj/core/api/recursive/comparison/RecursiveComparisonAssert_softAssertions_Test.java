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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_softAssertions_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  private SoftAssertions softly;

  @Override
  @BeforeEach
  public void setup() {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
    softly = new SoftAssertions();
  }

  @Test
  void should_pass_with_soft_assertions() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("John");
    expected.home.address.number = 1;
    // WHEN
    softly.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    // THEN
    softly.assertAll();
  }

  @Test
  void should_report_all_errors_with_soft_assertions() {
    // GIVEN
    Person john = new Person("John");
    john.home.address.number = 1;
    Person jack = new Person("Jack");
    jack.home.address.number = 2;
    // WHEN
    softly.assertThat(john).usingRecursiveComparison().isEqualTo(jack);
    softly.assertThat(jack).usingRecursiveComparison().isEqualTo(john);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessageContaining("field/property 'home.address.number' differ:")
                                      .hasMessageContaining("- actual value  : 1")
                                      .hasMessageContaining("- expected value: 2");
    assertThat(errorsCollected.get(1)).hasMessageContaining("field/property 'home.address.number' differ:")
                                      .hasMessageContaining("- actual value  : 2")
                                      .hasMessageContaining("- expected value: 1");
  }

}
