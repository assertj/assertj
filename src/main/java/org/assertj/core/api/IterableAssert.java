/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.CommonValidations.checkIsNotNull;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;

import org.assertj.core.internal.Failures;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for {@link Iterable}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Iterable)}</code>.
 * </p>
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Matthieu Baechler
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Julien Meddah
 */
public class IterableAssert<ELEMENT> extends
    FactoryBasedNavigableIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  public IterableAssert(Iterable<? extends ELEMENT> actual) {
    super(actual, IterableAssert.class, new ObjectAssertFactory<ELEMENT>());
  }

  public IterableAssert(Iterator<? extends ELEMENT> actual) {
    this(toLazyIterable(actual));
  }

  @Override
  public IterableAssert<ELEMENT> isEqualTo(Object expected) {
    if (actual instanceof LazyIterable) {
      objects.assertEqual(info, asLazyIterable().iterator, expected);
      return myself;
    }
    return super.isEqualTo(expected);
  }

  @Override
  public IterableAssert<ELEMENT> isInstanceOf(Class<?> type) {
    if (actual instanceof LazyIterable) {
      objects.assertIsInstanceOf(info, asLazyIterable().iterator, type);
      return myself;
    }
    return super.isInstanceOf(type);
  }

  @Override
  public IterableAssert<ELEMENT> isInstanceOfAny(Class<?>... types) {
    if (actual instanceof LazyIterable) {
      objects.assertIsInstanceOfAny(info, asLazyIterable().iterator, types);
      return myself;
    }
    return super.isInstanceOfAny(types);
  }

  @Override
  public IterableAssert<ELEMENT> isOfAnyClassIn(Class<?>... types) {
    if (actual instanceof LazyIterable) {
      objects.assertIsOfAnyClassIn(info, asLazyIterable().iterator, types);
      return myself;
    }
    return super.isOfAnyClassIn(types);
  }

  @Override
  public IterableAssert<ELEMENT> isExactlyInstanceOf(Class<?> type) {
    if (actual instanceof LazyIterable) {
      objects.assertIsExactlyInstanceOf(info, asLazyIterable().iterator, type);
      return myself;
    }
    return super.isExactlyInstanceOf(type);
  }

  @Override
  public IterableAssert<ELEMENT> isNotInstanceOf(Class<?> type) {
    if (actual instanceof LazyIterable) {
      objects.assertIsNotInstanceOf(info, asLazyIterable().iterator, type);
      return myself;
    }
    return super.isNotInstanceOf(type);
  }

  @Override
  public IterableAssert<ELEMENT> isNotInstanceOfAny(Class<?>... types) {
    if (actual instanceof LazyIterable) {
      objects.assertIsNotInstanceOfAny(info, asLazyIterable().iterator, types);
      return myself;
    }
    return super.isNotInstanceOfAny(types);
  }

  @Override
  public IterableAssert<ELEMENT> isNotOfAnyClassIn(Class<?>... types) {
    if (actual instanceof LazyIterable) {
      objects.assertIsNotOfAnyClassIn(info, asLazyIterable().iterator, types);
      return myself;
    }
    return super.isNotOfAnyClassIn(types);
  }

  @Override
  public IterableAssert<ELEMENT> isNotExactlyInstanceOf(Class<?> type) {
    if (actual instanceof LazyIterable) {
      objects.assertIsNotExactlyInstanceOf(info, asLazyIterable().iterator, type);
      return myself;
    }
    return super.isNotExactlyInstanceOf(type);
  }
  
  @Override
  public IterableAssert<ELEMENT> isSameAs(Object expected) {
    if (actual instanceof LazyIterable) {
      objects.assertSame(info, asLazyIterable().iterator, expected);
      return myself;
    }
    return super.isSameAs(expected);
  }

  @Override
  public IterableAssert<ELEMENT> isNotSameAs(Object expected) {
    if (actual instanceof LazyIterable) {
      objects.assertNotSame(info, asLazyIterable().iterator, expected);
      return myself;
    }
    return super.isNotSameAs(expected);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> startsWith(ELEMENT... sequence) {
    if (!(actual instanceof LazyIterable)) {
      return super.startsWith(sequence);
    }
    objects.assertNotNull(info, actual);
    checkIsNotNull(sequence);
    // To handle infinite iterator we use the internal iterator instead of iterator() that consumes it totally.
    @SuppressWarnings({ "rawtypes", "unchecked" })
    Iterator<? extends ELEMENT> iterator = ((LazyIterable) actual).iterator;
    if (sequence.length == 0 && iterator.hasNext()) throw new AssertionError("actual is not empty");
    int i = 0;
    while (iterator.hasNext()) {
      if (i >= sequence.length) break;
      if (iterables.getComparisonStrategy().areEqual(iterator.next(), sequence[i++])) continue;
      throw actualDoesNotStartWithSequence(info, actual, sequence);
    }
    if (sequence.length > i) {
      // sequence has more elements than actual
      throw actualDoesNotStartWithSequence(info, actual, sequence);
    }
    return myself;
  }

  private AssertionError actualDoesNotStartWithSequence(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    return Failures.instance().failure(info, shouldStartWith(actual, sequence, iterables.getComparisonStrategy()));
  }

  @SuppressWarnings("rawtypes")
  private LazyIterable asLazyIterable() {
    return (LazyIterable) actual;
  }

  // will only consume iterator when needed
  @VisibleForTesting
  static class LazyIterable<T> extends AbstractCollection<T> {
    private Iterator<T> iterator;
    private Iterable<T> iterable;

    public LazyIterable(Iterator<T> iterator) {
      this.iterator = iterator;
    }

    @Override
    public Iterator<T> iterator() {
      if (iterable == null) {
        iterable = toIterable(iterator);
      }
      return iterable.iterator();
    }

    @Override
    public int size() {
      int size = 0;
      Iterator<T> localIterator = iterator();
      while (localIterator.hasNext()) {
        localIterator.next();
        size++;
      }
      return size;
    }

  }

  private static <T> Iterable<T> toIterable(Iterator<T> iterator) {
    ArrayList<T> list = new ArrayList<>();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    return list;
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> contains(ELEMENT... values) {
    return super.contains(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsOnly(ELEMENT... values) {
    return super.containsOnly(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsOnlyOnce(ELEMENT... values) {
    return super.containsOnlyOnce(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsExactly(ELEMENT... values) {
    return super.containsExactly(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsExactlyInAnyOrder(ELEMENT... values) {
    return super.containsExactlyInAnyOrder(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> isSubsetOf(ELEMENT... values) {
    return super.isSubsetOf(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsSequence(ELEMENT... sequence) {
    return super.containsSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> doesNotContainSequence(ELEMENT... sequence) {
    return super.doesNotContainSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsSubsequence(ELEMENT... sequence) {
    return super.containsSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> doesNotContainSubsequence(ELEMENT... sequence) {
    return super.doesNotContainSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> doesNotContain(ELEMENT... values) {
    return super.doesNotContain(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> endsWith(ELEMENT first, ELEMENT... rest) {
    return super.endsWith(first, rest);
  }

}
