package org.fest.assertions.internal;

/*
 * Created on Sep 17, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */

import static java.lang.String.format;

import java.util.Iterator;

import org.fest.util.Objects;

/**
 * Implements {@link ComparisonStrategy} contract with a comparison strategy based on {@link Object#equals(Object)} method, it is
 * also based on {@link Comparable#compareTo(Object)} when Object are {@link Comparable} method.
 *
 * @author Joel Costigliola
 */
public class StandardComparisonStrategy extends AbstractComparisonStrategy {

  private static final StandardComparisonStrategy INSTANCE = new StandardComparisonStrategy();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static StandardComparisonStrategy instance() {
    return INSTANCE;
  }

  /**
   * Creates a new </code>{@link StandardComparisonStrategy}</code>, comparison strategy being based on
   * {@link Object#equals(Object)}.
   */
  private StandardComparisonStrategy() {}

  /**
   * Returns true if actual and other are equal based on {@link Object#equals(Object)}, false otherwise.
   *
   * @param actual the object to compare to other
   * @param other the object to compare to actual
   * @return true if actual and other are equal based on {@link Object#equals(Object)}, false otherwise.
   */
  @Override
  public boolean areEqual(Object actual, Object other) {
    return Objects.areEqual(actual, other);
  }

  /**
   * Returns true if given {@link Iterable} contains given value based on {@link Object#equals(Object)}, false otherwise.<br>
   * If given {@link Iterable} is null, return false.
   *
   * @param iterable the {@link Iterable} to search value in
   * @param value the object to look for in given {@link Iterable}
   * @return true if given {@link Iterable} contains given value based on {@link Object#equals(Object)}, false otherwise.
   */
  @Override
  public boolean iterableContains(Iterable<?> iterable, Object value) {
    if (iterable == null) {
      return false;
    }
    for (Object next : iterable) {
      if (Objects.areEqual(next, value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void iterableRemoves(Iterable<?> iterable, Object value) {
    if (iterable == null) {
      return;
    }
    Iterator<?> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      if (Objects.areEqual(iterator.next(), value)) {
        iterator.remove();
      }
    }
  }

  /**
   * Returns any duplicate elements from the given collection according to {@link Object#equals(Object)} comparison strategy.
   *
   * @param iterable the given {@link Iterable} we want to extract duplicate elements.
   * @return an {@link Iterable} containing the duplicate elements of the given one. If no duplicates are found, an empty
   *         {@link Iterable} is returned.
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

  @Override
  @SuppressWarnings("unchecked")
  public boolean isGreaterThan(Object actual, Object other) {
    if (!(actual instanceof Comparable)) {
      throw new IllegalArgumentException(format("argument '%s' should be Comparable but is not", actual));
    }
    return Comparable.class.cast(actual).compareTo(other) > 0;
  }

}
