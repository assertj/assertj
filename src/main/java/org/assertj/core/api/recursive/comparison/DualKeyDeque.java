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
