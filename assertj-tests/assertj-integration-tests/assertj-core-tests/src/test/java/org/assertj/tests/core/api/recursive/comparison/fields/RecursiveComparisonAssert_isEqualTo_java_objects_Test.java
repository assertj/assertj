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
package org.assertj.tests.core.api.recursive.comparison.fields;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.IntSummaryStatistics;

import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_isEqualTo_java_objects_Test extends WithComparingFieldsIntrospectionStrategyBaseTest {

  @Test
  void should_describe_cause_of_equals_use() {
    // GIVEN two equal values of a type from a java package
    IntSummaryStatistics statisticsActual = new IntSummaryStatistics();
    IntSummaryStatistics statisticsExpected = new IntSummaryStatistics();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(statisticsActual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                                           .isEqualTo(statisticsExpected));
    // THEN
    then(assertionError).hasMessageContaining("Actual value is a java type and thus was compared to the expected value with its equals method");
  }
}
