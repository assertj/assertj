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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.Strings.join;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.stream.Stream;

// logically immutable
final class DualValue {

  static final Class<?>[] DEFAULT_ORDERED_COLLECTION_TYPES = array(List.class, SortedSet.class, LinkedHashSet.class);

  final List<String> path;
  final String concatenatedPath;
  final Object actual;
  final Object expected;
  private final int hashCode;


  DualValue(List<String> path, Object actual, Object expected) {
    this.path = path;
    this.concatenatedPath = join(path).with(".");
    this.actual = actual;
    this.expected = expected;
    int h1 = actual != null ? actual.hashCode() : 0;
    int h2 = expected != null ? expected.hashCode() : 0;
    hashCode = h1 + h2;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof DualValue)) return false;
    DualValue that = (DualValue) other;
    return actual == that.actual && expected == that.expected;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return format("DualValue [path=%s, actual=%s, expected=%s]", concatenatedPath, actual, expected);
  }

  public List<String> getPath() {
    return unmodifiableList(path);
  }

  public String getConcatenatedPath() {
    return concatenatedPath;
  }

  public boolean isJavaType() {
    if (actual == null) return false;
    return actual.getClass().getName().startsWith("java.");
  }

  public boolean isExpectedFieldAnArray() {
    return isArray(expected);
  }

  public boolean isActualFieldAnArray() {
    return isArray(actual);
  }

  public boolean hasIterableValues() {
    return actual instanceof Iterable && expected instanceof Iterable;
  }

  public boolean isActualFieldAnOptional() {
    return actual instanceof Optional;
  }

  public boolean isExpectedFieldAnOptional() {
    return expected instanceof Optional;
  }

  public boolean isActualFieldAMap() {
    return actual instanceof Map;
  }

  public boolean isExpectedFieldAMap() {
    return expected instanceof Map;
  }

  public boolean isActualFieldASortedMap() {
    return actual instanceof SortedMap;
  }

  public boolean isExpectedFieldASortedMap() {
    return expected instanceof SortedMap;
  }

  public boolean isActualFieldAnOrderedCollection() {
    return isAnOrderedCollection(actual);
  }

  public boolean isExpectedFieldAnOrderedCollection() {
    return isAnOrderedCollection(expected);
  }

  public boolean isActualFieldAnIterable() {
    return actual instanceof Iterable;
  }

  public boolean isExpectedFieldAnIterable() {
    return expected instanceof Iterable;
  }

  private static boolean isAnOrderedCollection(Object value) {
    return Stream.of(DEFAULT_ORDERED_COLLECTION_TYPES).anyMatch(type -> type.isInstance(value));
  }
}