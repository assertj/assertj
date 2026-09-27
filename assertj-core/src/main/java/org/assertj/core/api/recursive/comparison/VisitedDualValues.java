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

import static org.assertj.core.util.Lists.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class VisitedDualValues {

  private final List<VisitedDualValue> visitedDualValues;
  private boolean cycleGuardUsed;

  VisitedDualValues() {
    visitedDualValues = new ArrayList<>();
  }

  VisitedDualValues forAncestorsOf(DualValue dualValue) {
    VisitedDualValues ancestors = new VisitedDualValues();
    Set<DualValue> parentChain = Collections.newSetFromMap(new IdentityHashMap<>());
    for (DualValue parent = dualValue.parent(); parent != null; parent = parent.parent())
      parentChain.add(parent);
    for (VisitedDualValue visited : visitedDualValues) {
      if (parentChain.contains(visited.dualValue)) {
        // Only inherit cycle guards, not results obtained under another field's comparison rules.
        ancestors.registerVisitedDualValue(visited.dualValue);
      }
    }
    return ancestors;
  }

  boolean cycleGuardUsed() {
    return cycleGuardUsed;
  }

  void markCycleGuardUsed() {
    cycleGuardUsed = true;
  }

  void registerVisitedDualValue(DualValue dualValue) {
    visitedDualValues.add(new VisitedDualValue(dualValue));
  }

  void registerComparisonDifference(DualValue dualValue, ComparisonDifference comparisonDifference) {
    registerComparisonDifferences(dualValue, list(comparisonDifference));
  }

  void registerComparisonDifferences(DualValue dualValue, List<ComparisonDifference> comparisonDifferences) {
    Optional<VisitedDualValue> visitedDualValueWithSameValues = visitedDualValues.stream()
                                                                                 .filter(visitedDualValue -> visitedDualValue.dualValue.sameValues(dualValue))
                                                                                 .findFirst();
    // register difference on dual values agnostic of location, to take care of values visited several times
    if (visitedDualValueWithSameValues.isPresent()) {
      visitedDualValueWithSameValues.get().comparisonDifferences.addAll(comparisonDifferences);
    } else {
      VisitedDualValue visitedDualValue = new VisitedDualValue(dualValue);
      visitedDualValue.comparisonDifferences.addAll(comparisonDifferences);
      visitedDualValues.add(visitedDualValue);
    }
  }

  Optional<Set<ComparisonDifference>> getRegisteredComparisonDifferencesOf(DualValue dualValue) {
    Optional<VisitedDualValue> optionalVisitedDualValue = visitedDualValues.stream()
                                                                           .filter(visitedDualValue -> visitedDualValue.dualValue.sameValues(dualValue))
                                                                           .findFirst();
    if (optionalVisitedDualValue.isEmpty()) {
      return Optional.empty();
    }
    if (dualValue.hasAncestor(dualValue)) cycleGuardUsed = true;
    // need to aggregate the current visited dualValue differences + all the visited children differences
    Set<ComparisonDifference> comparisonDifferences = new LinkedHashSet<>(optionalVisitedDualValue.get().comparisonDifferences);
    visitedDualValues.stream()
                     .filter(visitedDualValue -> visitedDualValue.dualValue.hasAncestor(dualValue))
                     .forEach(visitedDualValue -> comparisonDifferences.addAll(visitedDualValue.comparisonDifferences));
    return Optional.of(comparisonDifferences);
  }

  private static class VisitedDualValue {
    DualValue dualValue;
    List<ComparisonDifference> comparisonDifferences;

    VisitedDualValue(DualValue dualValue) {
      this.dualValue = dualValue;
      this.comparisonDifferences = new ArrayList<>();
    }

    @Override
    public String toString() {
      return "VisitedDualValue[dualValue=%s, comparisonDifferences=%s]".formatted(this.dualValue, this.comparisonDifferences);
    }

  }
}
