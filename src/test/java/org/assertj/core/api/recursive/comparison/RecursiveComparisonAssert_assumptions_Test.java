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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.Person;
import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.Test;

public class RecursiveComparisonAssert_assumptions_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @Test
  public void should_ignore_test_when_one_of_the_assumption_fails() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("John");
    expected.home.address.number = 1;
    Person unexpected = new Person("John");
    unexpected.home.address.number = 2;
    // THEN
    assumeThat(actual).usingRecursiveComparison().isEqualTo(expected);
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(actual).usingRecursiveComparison()
                                                                                                    .isEqualTo(unexpected));
  }

  @Test
  public void should_run_test_when_all_assumptions_are_met() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("John");
    expected.home.address.number = 1;
    // THEN
    assertThatCode(() -> {
      assumeThat("foo").isNotNull()
                       .isNotEmpty()
                       .isEqualTo("foo");
      assumeThat(actual).usingRecursiveComparison().isEqualTo(expected);
      assumeThat(expected).usingRecursiveComparison().isEqualTo(actual);
      assumeThat(actual).as("test description")
                        .withFailMessage("error message")
                        .withRepresentation(UNICODE_REPRESENTATION)
                        .usingRecursiveComparison()
                        .isEqualTo(expected);
    }).doesNotThrowAnyException();
  }

}
