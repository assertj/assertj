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

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.LinkedList;

// special deque that can ignore DualKey according to RecursiveComparisonConfiguration.
// TODO: keep track of ignored fields ?
@SuppressWarnings("serial")
class DualKeyDeque extends LinkedList<DualKey> {
  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  public DualKeyDeque(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
  }

  @Override
  public boolean add(DualKey dualKey) {
    if (shouldIgnore(dualKey)) return false;
    return super.add(dualKey);
  }

  @Override
  public void add(int index, DualKey dualKey) {
    if (shouldIgnore(dualKey)) return;
    super.add(index, dualKey);
  }

  @Override
  public boolean addAll(int index, Collection<? extends DualKey> collection) {
    return super.addAll(index, collection.stream().filter(this::shouldAddDualKey).collect(toList()));
  }

  @Override
  public void addFirst(DualKey dualKey) {
    if (shouldIgnore(dualKey)) return;
    super.addFirst(dualKey);
  }

  @Override
  public void addLast(DualKey dualKey) {
    if (shouldIgnore(dualKey)) return;
    super.addLast(dualKey);
  }

  private boolean shouldIgnore(DualKey dualKey) {
    return recursiveComparisonConfiguration.shouldIgnore(dualKey);
  }

  private boolean shouldAddDualKey(DualKey dualKey) {
    return !shouldIgnore(dualKey);
  }

}
