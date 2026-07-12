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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.DualValue.rootDualValue;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

class VisitedDualValuesTest {

  @Test
  void should_return_direct_registered_differences_without_duplicates() {
    // GIVEN
    var visitedDualValues = new VisitedDualValues();
    var dualValue = rootDualValue("abc", "abc");
    visitedDualValues.registerVisitedDualValue(dualValue);
    var comparisonDifference1 = new ComparisonDifference(dualValue);
    visitedDualValues.registerComparisonDifference(dualValue, comparisonDifference1);
    var comparisonDifference1bis = new ComparisonDifference(dualValue);
    visitedDualValues.registerComparisonDifference(dualValue, comparisonDifference1bis);
    var comparisonDifference2 = new ComparisonDifference(dualValue, "another diff");
    visitedDualValues.registerComparisonDifference(dualValue, comparisonDifference2);
    // WHEN
    var optionalComparisonDifferences = visitedDualValues.getRegisteredComparisonDifferencesOf(dualValue);
    // THEN
    then(optionalComparisonDifferences).isPresent();
    then(optionalComparisonDifferences.get()).containsExactlyInAnyOrder(comparisonDifference1, comparisonDifference2);
  }

  @Test
  void should_return_registered_differences_on_grandchildren_without_duplicates() {
    // GIVEN
    var visitedDualValues = new VisitedDualValues();
    var dualValueA = new DualValue(new FieldLocation("a"), "a", "a", null);
    var dualValueB = new DualValue(new FieldLocation("a.b"), "ab", "ab", dualValueA);
    var dualValueC = new DualValue(new FieldLocation("a.b.c"), "abc", "abc", dualValueB);
    var dualValueD = new DualValue(new FieldLocation("a.b.c.d"), "abcd", "abcd", dualValueC);
    var dualValueE = new DualValue(new FieldLocation("a.b.c.d.e"), "abcde", "abcde", dualValueD);
    visitedDualValues.registerVisitedDualValue(dualValueA);
    visitedDualValues.registerVisitedDualValue(dualValueB);
    visitedDualValues.registerVisitedDualValue(dualValueC);
    visitedDualValues.registerVisitedDualValue(dualValueD);
    ComparisonDifference comparisonDifferenceB = new ComparisonDifference(dualValueB);
    visitedDualValues.registerComparisonDifference(dualValueB, comparisonDifferenceB);
    ComparisonDifference comparisonDifferenceD = new ComparisonDifference(dualValueD);
    visitedDualValues.registerComparisonDifference(dualValueD, comparisonDifferenceD);
    // WHEN
    Optional<Set<ComparisonDifference>> dualValueADifferences = visitedDualValues.getRegisteredComparisonDifferencesOf(dualValueA);
    Optional<Set<ComparisonDifference>> dualValueBDifferences = visitedDualValues.getRegisteredComparisonDifferencesOf(dualValueB);
    Optional<Set<ComparisonDifference>> dualValueCDifferences = visitedDualValues.getRegisteredComparisonDifferencesOf(dualValueC);
    Optional<Set<ComparisonDifference>> dualValueDDifferences = visitedDualValues.getRegisteredComparisonDifferencesOf(dualValueD);
    Optional<Set<ComparisonDifference>> dualValueEDifferences = visitedDualValues.getRegisteredComparisonDifferencesOf(dualValueE);
    // THEN
    then(dualValueADifferences).isPresent();
    then(dualValueADifferences.get()).containsExactlyInAnyOrder(comparisonDifferenceB, comparisonDifferenceD);
    then(dualValueBDifferences).isPresent();
    then(dualValueBDifferences.get()).containsExactlyInAnyOrder(comparisonDifferenceB, comparisonDifferenceD);
    then(dualValueCDifferences).isPresent();
    then(dualValueCDifferences.get()).containsExactlyInAnyOrder(comparisonDifferenceD);
    then(dualValueDDifferences).isPresent();
    then(dualValueDDifferences.get()).containsExactlyInAnyOrder(comparisonDifferenceD);
    then(dualValueEDifferences).isEmpty();
  }

  @Test
  void should_return_no_differences_when_none_have_been_registered() {
    // GIVEN
    var visitedDualValues = new VisitedDualValues();
    var dualValue = rootDualValue("abc", "abc");
    visitedDualValues.registerVisitedDualValue(dualValue);
    // WHEN
    Optional<Set<ComparisonDifference>> optionalComparisonDifferences = visitedDualValues.getRegisteredComparisonDifferencesOf(dualValue);
    // THEN
    then(optionalComparisonDifferences).isPresent();
    then(optionalComparisonDifferences.get()).isEmpty();
  }

  @Test
  void should_return_empty_optional_for_unknown_dual_values() {
    // GIVEN
    var visitedDualValues = new VisitedDualValues();
    var dualValue = rootDualValue("abc", "abc");
    // WHEN
    var optionalComparisonDifferences = visitedDualValues.getRegisteredComparisonDifferencesOf(dualValue);
    // THEN
    then(optionalComparisonDifferences).isEmpty();
  }
}
