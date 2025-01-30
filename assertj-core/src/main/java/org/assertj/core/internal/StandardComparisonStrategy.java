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

  /**
   * Returns {@code true} if the arguments are deeply equal to each other, {@code false} otherwise.
   * <p>
   * It mimics the behavior of {@link java.util.Objects#deepEquals(Object, Object)}, but without performing a reference
   * check between the two arguments. According to {@code deepEquals} javadoc, the reference check should be delegated
   * to the {@link Object#equals equals} method of the first argument, but this is not happening. Bug JDK-8196069 also
   * mentions this gap.
   *
   * @param actual the object to compare to {@code other}
   * @param other the object to compare to {@code actual}
   * @return {@code true} if the arguments are deeply equal to each other, {@code false} otherwise
   *
   * @see <a href="https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8196069">JDK-8196069</a>
   *
   */
  @Override
  public boolean areEqual(Object actual, Object other) {
    if (actual == null) return other == null;
    Class<?> actualClass = actual.getClass();
    if (actualClass.isArray() && other != null) {
      Class<?> otherClass = other.getClass();
      if (otherClass.isArray()) {
        if (actualClass.getComponentType().isPrimitive() && otherClass.getComponentType().isPrimitive()) {
          if (actual instanceof byte[] bytes && other instanceof byte[] bytes1)
            return java.util.Arrays.equals(bytes, bytes1);
          if (actual instanceof short[] shorts && other instanceof short[] shorts1)
            return java.util.Arrays.equals(shorts, shorts1);
          if (actual instanceof int[] ints && other instanceof int[] ints1)
            return java.util.Arrays.equals(ints, ints1);
          if (actual instanceof long[] longs && other instanceof long[] longs1)
            return java.util.Arrays.equals(longs, longs1);
          if (actual instanceof char[] chars && other instanceof char[] chars1)
            return java.util.Arrays.equals(chars, chars1);
          if (actual instanceof float[] floats && other instanceof float[] floats1)
            return java.util.Arrays.equals(floats, floats1);
          if (actual instanceof double[] doubles && other instanceof double[] doubles1)
            return java.util.Arrays.equals(doubles, doubles1);
          if (actual instanceof boolean[] booleans && other instanceof boolean[] booleans1)
            return java.util.Arrays.equals(booleans, booleans1);
        }

        if (actual instanceof Object[] objects && other instanceof Object[] objects1)
          return java.util.Arrays.deepEquals(objects, objects1);
      }
    }
    return actual.equals(other);
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
    if (iterable instanceof Collection<?> collection) {
      collection.removeIf(o -> areEqual(o, value));
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
