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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

class VisitedDualValuesTest {

  @Test
  void should_return_the_registered_differences() {
    // GIVEN
    VisitedDualValues visitedDualValues = new VisitedDualValues();
    DualValue dualValue = new DualValue(list(""), "abc", "abc");
    visitedDualValues.registerVisitedDualValue(dualValue);
    ComparisonDifference comparisonDifference1 = new ComparisonDifference(dualValue);
    visitedDualValues.registerComparisonDifference(dualValue, comparisonDifference1);
    ComparisonDifference comparisonDifference2 = new ComparisonDifference(dualValue);
    visitedDualValues.registerComparisonDifference(dualValue, comparisonDifference2);
    // WHEN
    Optional<List<ComparisonDifference>> optionalComparisonDifferences = visitedDualValues.registeredComparisonDifferencesOf(dualValue);
    // THEN
    then(optionalComparisonDifferences).isPresent();
    BDDAssertions.then(optionalComparisonDifferences.get()).containsExactlyInAnyOrder(comparisonDifference1,
                                                                                      comparisonDifference2);
  }

  @Test
  void should_return_no_differences_when_none_have_been_registered() {
    // GIVEN
    VisitedDualValues visitedDualValues = new VisitedDualValues();
    DualValue dualValue = new DualValue(list(""), "abc", "abc");
    visitedDualValues.registerVisitedDualValue(dualValue);
    // WHEN
    Optional<List<ComparisonDifference>> optionalComparisonDifferences = visitedDualValues.registeredComparisonDifferencesOf(dualValue);
    // THEN
    then(optionalComparisonDifferences).isPresent();
    BDDAssertions.then(optionalComparisonDifferences.get()).isEmpty();
  }

  @Test
  void should_return_empty_optional_for_unknown_dual_values() {
    // GIVEN
    VisitedDualValues visitedDualValues = new VisitedDualValues();
    DualValue dualValue = new DualValue(list(""), "abc", "abc");
    // WHEN
    Optional<List<ComparisonDifference>> optionalComparisonDifferences = visitedDualValues.registeredComparisonDifferencesOf(dualValue);
    // THEN
    then(optionalComparisonDifferences).isEmpty();
  }
}
