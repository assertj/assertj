/*
 * Copyright 2012-2025 the original author or authors.
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

import java.util.Collection;
import java.util.LinkedList;

// special deque that can ignore DualValue according to RecursiveComparisonConfiguration.
class DualValueDeque extends LinkedList<DualValue> {
  private final RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  public DualValueDeque(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
  }

  @Override
  public boolean add(DualValue dualValue) {
    if (shouldNotEvaluate(dualValue)) return false;
    return super.add(dualValue);
  }

  @Override
  public void add(int index, DualValue dualValue) {
    if (shouldNotEvaluate(dualValue)) return;
    super.add(index, dualValue);
  }

  @Override
  public boolean addAll(int index, Collection<? extends DualValue> collection) {
    return super.addAll(index, collection.stream().filter(this::shouldAddDualValue).toList());
  }

  @Override
  public void addFirst(DualValue dualValue) {
    if (shouldNotEvaluate(dualValue)) return;
    super.addFirst(dualValue);
  }

  @Override
  public void addLast(DualValue dualValue) {
    if (shouldNotEvaluate(dualValue)) return;
    super.addLast(dualValue);
  }

  /**
   * Decides whether the value needs to evaluated, note that we need to evaluate all values if we have
   * compared types registered as a value could have fields of type to compare.
   * <p>
   * For example if we want to compare Employee in a Company, we need to evaluate company as it holds a list of Employee.
   *
   * @param dualValue the value to check
   * @return true if we want to register the value for evaluation, false otherwise
   */
  private boolean shouldNotEvaluate(DualValue dualValue) {
    return recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
  }

  private boolean shouldAddDualValue(DualValue dualValue) {
    return !shouldNotEvaluate(dualValue);
  }

}
