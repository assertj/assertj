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
package org.assertj.core.api;

import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.util.Streams.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.internal.Iterables;

/**
 * Assertion methods for <code>{@link HashSet}</code>, honoring the hash code comparison.
 * <p>
 * The base goal is to follow <code>{@link HashSet#contains(Object)}</code> logic from Java
 * - as soon as <code>hashCode</code> of the element changes, <code>{@link HashSet}</code> doesn't recognize it anymore.
 * <p>
 * Methods checking all the elements (like <code>{@link #areAtLeast(int, Condition)}</code>) or methods requiring further
 * assertions on the elements anyway (like <code>{@link #isSubsetOf(Iterable)}</code>), behave as usual,
 * but e.g. <code>{@link #contains(Object...)}</code>, <code>{@link #doesNotContainSubsequence(Iterable)}</code>, etc.
 * honors the hash code comparison.
 * <p>
 * When more relaxed approach is needed, use <code>{@link #skippingHashCodeComparison()}</code> to start checking the actual
 * <code>{@link Set}</code> elements one by one,  as for ordinary collections and iterables.
 *
 * @param <ELEMENT> the type of elements stored in <code>{@link HashSet}</code>.
 * 
 * @author Mateusz Chrzonstowski
 */
public class HashSetAssert<ELEMENT>
    extends AbstractCollectionAssert<HashSetAssert<ELEMENT>, HashSet<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  private final Iterables originalIterables;

  public HashSetAssert(HashSet<? extends ELEMENT> elements) {
    super(elements, HashSetAssert.class);
    originalIterables = iterables;
    iterables = new Iterables(new InHashSetComparisonStrategy(actual));
  }

  /**
   * Starts to check the actual <code>{@link Set}</code> elements one by one, skipping the hash code comparisons.
   * Same as using <code>{@link AbstractCollectionAssert}</code>.
   * 
   * @return <code>{@link AbstractCollectionAssert}</code> which ignores hash code comparisons.
   */
  public AbstractCollectionAssert<?, Collection<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> skippingHashCodeComparison() {
    return assertThat((Collection<? extends ELEMENT>) actual);
  }

  @Override
  protected ObjectAssert<ELEMENT> toAssert(ELEMENT value, String description) {
    return new ObjectAssert<>(value).as(description);
  }

  @Override
  protected HashSetAssert<ELEMENT> newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    HashSet<ELEMENT> setPreservingTheOrder = stream(iterable).collect(toCollection(LinkedHashSet::new));
    return new HashSetAssert<>(setPreservingTheOrder);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HashSetAssert<ELEMENT> isSubsetOf(Iterable<? extends ELEMENT> values) {
    originalIterables.assertIsSubsetOf(info, actual, values);
    return myself;
  }

  @Override
  protected HashSetAssert<ELEMENT> isSubsetOfForProxy(ELEMENT[] values) {
    originalIterables.assertIsSubsetOf(info, actual, Arrays.asList(values));
    return myself;
  }

  private static class InHashSetComparisonStrategy extends StandardComparisonStrategy {
    private final HashSet<?> originalSet;

    InHashSetComparisonStrategy(HashSet<?> originalSet) {
      super();
      this.originalSet = originalSet;
    }

    @Override
    public boolean iterableContains(Iterable<?> iterable, Object value) {
      return originalSet.contains(value) && super.iterableContains(iterable, value);
    }

    @Override
    public boolean areEqual(Object actual, Object other) {
      return originalSet.contains(actual) && super.areEqual(actual, other);
    }

    @Override
    public String asText() {
      return "(elements were checked as in HashSet, as soon as their hashCode change, the HashSet won't find them anymore - use skippingHashCodeComparison to get a collection like comparison)";
    }

    @Override
    public String toString() {
      return asText();
    }
  }
}
