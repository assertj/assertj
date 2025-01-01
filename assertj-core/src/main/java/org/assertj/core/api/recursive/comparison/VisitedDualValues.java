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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

class VisitedDualValues {

  private final List<VisitedDualValue> dualValues;

  VisitedDualValues() {
    this.dualValues = new ArrayList<>();
  }

  void registerVisitedDualValue(DualValue dualValue) {
    this.dualValues.add(new VisitedDualValue(dualValue));
  }

  void registerComparisonDifference(DualValue dualValue, ComparisonDifference comparisonDifference) {
    this.dualValues.stream()
                   // register difference on dual values agnostic of location, to take care of values visited several times
                   .filter(visitedDualValue -> visitedDualValue.dualValue.sameValues(dualValue))
                   .findFirst()
                   .ifPresent(visitedDualValue -> visitedDualValue.comparisonDifferences.add(comparisonDifference));
  }

  Optional<List<ComparisonDifference>> registeredComparisonDifferencesOf(DualValue dualValue) {
    return this.dualValues.stream()
                          // use sameValues to get already visited dual values with different location
                          .filter(visitedDualValue -> visitedDualValue.dualValue.sameValues(dualValue))
                          .findFirst()
                          .map(visitedDualValue -> visitedDualValue.comparisonDifferences);
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
      return format("VisitedDualValue[dualValue=%s, comparisonDifferences=%s]", this.dualValue, this.comparisonDifferences);
    }
  }
}
