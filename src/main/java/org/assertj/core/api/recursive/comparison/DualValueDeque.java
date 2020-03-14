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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.LinkedList;

// special deque that can ignore DualKey according to RecursiveComparisonConfiguration.
@SuppressWarnings("serial")
class DualValueDeque extends LinkedList<DualValue> {
  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  public DualValueDeque(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
  }

  @Override
  public boolean add(DualValue dualKey) {
    if (shouldIgnore(dualKey)) return false;
    return super.add(dualKey);
  }

  @Override
  public void add(int index, DualValue dualKey) {
    if (shouldIgnore(dualKey)) return;
    super.add(index, dualKey);
  }

  @Override
  public boolean addAll(int index, Collection<? extends DualValue> collection) {
    return super.addAll(index, collection.stream().filter(this::shouldAddDualKey).collect(toList()));
  }

  @Override
  public void addFirst(DualValue dualKey) {
    if (shouldIgnore(dualKey)) return;
    super.addFirst(dualKey);
  }

  @Override
  public void addLast(DualValue dualKey) {
    if (shouldIgnore(dualKey)) return;
    super.addLast(dualKey);
  }

  private boolean shouldIgnore(DualValue dualKey) {
    return recursiveComparisonConfiguration.shouldIgnore(dualKey);
  }

  private boolean shouldAddDualKey(DualValue dualKey) {
    return !shouldIgnore(dualKey);
  }

}
