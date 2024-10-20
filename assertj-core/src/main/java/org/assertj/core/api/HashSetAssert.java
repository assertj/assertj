package org.assertj.core.api;

import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.StandardComparisonStrategy;

import java.util.HashSet;

import static org.assertj.core.util.Sets.newHashSet;

public class HashSetAssert<ELEMENT>
  extends AbstractCollectionAssert<HashSetAssert<ELEMENT>, HashSet<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  protected Iterables hashSets = new Iterables(new HashSetComparisonStrategy());

  public HashSetAssert(HashSet<? extends ELEMENT> elements) {
    super(elements, HashSetAssert.class);
  }

  @Override
  protected ObjectAssert<ELEMENT> toAssert(ELEMENT value, String description) {
    return new ObjectAssert<>(value).as(description);
  }

  @Override
  protected HashSetAssert<ELEMENT> newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return new HashSetAssert<>(newHashSet(iterable));
  }

  @Override
  protected HashSetAssert<ELEMENT> containsForProxy(ELEMENT[] values) {
    super.containsForProxy(values);
    hashSets.assertContains(info, actual, values);
    return myself;
  }

  private static class HashSetComparisonStrategy extends StandardComparisonStrategy {
    @Override
    public boolean iterableContains(Iterable<?> iterable, Object value) {
      return ((HashSet<?>) iterable).contains(value);
    }

    @Override
    public String asText() {
      return "(technically element is in the HashSet, but its hashCode value changed)";
    }
  }
}
