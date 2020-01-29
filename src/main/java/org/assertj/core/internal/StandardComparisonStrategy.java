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
package org.assertj.core.internal;

import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.assertj.core.util.Objects;
import org.assertj.core.util.Streams;

/**
 * Implements {@link ComparisonStrategy} contract with a comparison strategy based on
 * {@link java.util.Objects#deepEquals(Object, Object)} method, it is also based on {@link Comparable#compareTo(Object)} when Object
 * are {@link Comparable} method.
 *
 * @author Joel Costigliola
 */
public class StandardComparisonStrategy extends AbstractComparisonStrategy {

  private static final StandardComparisonStrategy INSTANCE = new StandardComparisonStrategy();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static StandardComparisonStrategy instance() {
    return INSTANCE;
  }

  /**
   * Creates a new <code>{@link StandardComparisonStrategy}</code>, comparison strategy being based on
   * {@link java.util.Objects#deepEquals(Object, Object)}.
   */
  protected StandardComparisonStrategy() {
    // empty
  }

  @Override
  protected Set<Object> newSetUsingComparisonStrategy() {
    // define a comparator so that we can use areEqual to compare objects in Set collections
    // the "less than" comparison does not make much sense here but need to be defined.
    return new TreeSet<>((o1, o2) -> {
      if (areEqual(o1, o2)) return 0;
      return Objects.hashCodeFor(o1) < Objects.hashCodeFor(o2) ? -1 : 1;
    });
  }

  @Override
  public String asText() {
    return "";
  }

  /**
   * Returns true if actual and other are equal based on {@link java.util.Objects#deepEquals(Object, Object)}, false otherwise.
   *
   * @param actual the object to compare to other
   * @param other the object to compare to actual
   * @return true if actual and other are equal based on {@link java.util.Objects#deepEquals(Object, Object)}, false otherwise.
   */
  @Override
  public boolean areEqual(Object actual, Object other) {
    return java.util.Objects.deepEquals(actual, other);
  }

  /**
   * Returns true if given {@link Iterable} contains given value based on {@link java.util.Objects#deepEquals(Object, Object)},
   * false otherwise.<br>
   * If given {@link Iterable} is null, return false.
   *
   * @param iterable the {@link Iterable} to search value in
   * @param value the object to look for in given {@link Iterable}
   * @return true if given {@link Iterable} contains given value based on {@link java.util.Objects#deepEquals(Object, Object)},
   *         false otherwise.
   */
  @Override
  public boolean iterableContains(Iterable<?> iterable, Object value) {
    if (iterable == null) {
      return false;
    }
    return Streams.stream(iterable).anyMatch(object -> areEqual(object, value));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void iterableRemoves(Iterable<?> iterable, Object value) {
    if (iterable == null) {
      return;
    }
    // Avoid O(N^2) complexity of serial removal from an iterator of collections like ArrayList
    if (iterable instanceof Collection) {
      ((Collection<?>) iterable).removeIf(o -> areEqual(o, value));
    } else {
      Iterator<?> iterator = iterable.iterator();
      while (iterator.hasNext()) {
        if (areEqual(iterator.next(), value)) {
          iterator.remove();
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void iterablesRemoveFirst(Iterable<?> iterable, Object value) {
    if (iterable == null) {
      return;
    }
    Iterator<?> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      if (areEqual(iterator.next(), value)) {
        iterator.remove();
        return;
      }
    }
  }

  /**
   * Returns any duplicate elements from the given collection according to {@link java.util.Objects#deepEquals(Object, Object)}
   * comparison strategy.
   *
   * @param iterable the given {@link Iterable} we want to extract duplicate elements.
   * @return an {@link Iterable} containing the duplicate elements of the given one. If no duplicates are found, an
   *         empty {@link Iterable} is returned.
   */
  // overridden to write javadoc.
  @Override
  public Iterable<?> duplicatesFrom(Iterable<?> iterable) {
    return super.duplicatesFrom(iterable);
  }

  @Override
  public boolean stringStartsWith(String string, String prefix) {
    return string.startsWith(prefix);
  }

  @Override
  public boolean stringEndsWith(String string, String suffix) {
    return string.endsWith(suffix);
  }

  @Override
  public boolean stringContains(String string, String sequence) {
    return string.contains(sequence);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public boolean isGreaterThan(Object actual, Object other) {
    checkArgumentIsComparable(actual);
    return ((Comparable) actual).compareTo(other) > 0;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public boolean isLessThan(Object actual, Object other) {
    checkArgumentIsComparable(actual);
    return ((Comparable) actual).compareTo(other) < 0;
  }

  private void checkArgumentIsComparable(Object actual) {
    checkArgument(actual instanceof Comparable, "argument '%s' should be Comparable but is not", actual);
  }

  @Override
  public boolean isStandard() {
    return true;
  }

}
