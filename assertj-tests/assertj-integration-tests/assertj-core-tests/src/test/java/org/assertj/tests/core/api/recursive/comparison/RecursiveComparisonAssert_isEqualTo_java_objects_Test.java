/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison;

import org.junit.jupiter.api.Test;

import java.util.IntSummaryStatistics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

class RecursiveComparisonAssert_isEqualTo_java_objects_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @Test
  void should_describe_cause_of_equals_use() {
    // GIVEN two equal values of a type from a java package
    IntSummaryStatistics statisticsActual = new IntSummaryStatistics();
    IntSummaryStatistics statisticsExpected = new IntSummaryStatistics();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(statisticsActual).usingRecursiveComparison()
                                                                                           .isEqualTo(statisticsExpected));
    // THEN
    then(assertionError).hasMessageContaining("Compared objects have java types and were thus compared with equals method");
  }
}
