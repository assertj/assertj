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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.isArray;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.stream.Stream;

// logically immutable
public final class DualValue {

  static final Class<?>[] DEFAULT_ORDERED_COLLECTION_TYPES = array(List.class, SortedSet.class, LinkedHashSet.class);

  final FieldLocation fieldLocation;
  final Object actual;
  final Object expected;
  private final int hashCode;

  public DualValue(List<String> path, Object actual, Object expected) {
    this(new FieldLocation(path), actual, expected);
  }

  static DualValue rootDualValue(Object actual, Object expected) {
    return new DualValue(rootFieldLocation(), actual, expected);
  }

  public DualValue(FieldLocation fieldLocation, Object actualFieldValue, Object expectedFieldValue) {
    this.fieldLocation = requireNonNull(fieldLocation, "fieldLocation must not be null");
    actual = actualFieldValue;
    expected = expectedFieldValue;
    hashCode = Objects.hash(actual, expected);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof DualValue)) return false;
    DualValue that = (DualValue) other;
    // it is critical to compare by reference when tracking visited dual values.
    // see should_fix_1854_minimal_test for an explanation
    // TODO add field location check?
    return actual == that.actual && expected == that.expected;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return format("DualValue [fieldLocation=%s, actual=%s, expected=%s]", fieldLocation, actual, expected);
  }

  public List<String> getDecomposedPath() {
    return unmodifiableList(fieldLocation.getDecomposedPath());
  }

  public String getConcatenatedPath() {
    return fieldLocation.getPathToUseInRules();
  }

  public String getFieldName() {
    return fieldLocation.getFieldName();
  }

  public boolean isActualJavaType() {
    if (actual == null) return false;
    return actual.getClass().getName().startsWith("java.");
  }

  public boolean isExpectedFieldAnArray() {
    return isArray(expected);
  }

  public boolean isActualFieldAnArray() {
    return isArray(actual);
  }

  public boolean isActualFieldAnOptional() {
    return actual instanceof Optional;
  }

  public boolean isActualFieldAnOptionalInt() {
    return actual instanceof OptionalInt;
  }

  public boolean isActualFieldAnOptionalLong() {
    return actual instanceof OptionalLong;
  }

  public boolean isActualFieldAnOptionalDouble() {
    return actual instanceof OptionalDouble;
  }

  public boolean isActualFieldAnEmptyOptionalOfAnyType() {
    return isActualFieldAnEmptyOptional()
           || isActualFieldAnEmptyOptionalInt()
           || isActualFieldAnEmptyOptionalLong()
           || isActualFieldAnEmptyOptionalDouble();
  }

  private boolean isActualFieldAnEmptyOptional() {
    return isActualFieldAnOptional() && !((Optional<?>) actual).isPresent();
  }

  private boolean isActualFieldAnEmptyOptionalInt() {
    return isActualFieldAnOptionalInt() && !((OptionalInt) actual).isPresent();
  }

  private boolean isActualFieldAnEmptyOptionalLong() {
    return isActualFieldAnOptionalLong() && !((OptionalLong) actual).isPresent();
  }

  private boolean isActualFieldAnEmptyOptionalDouble() {
    return isActualFieldAnOptionalDouble() && !((OptionalDouble) actual).isPresent();
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
    // ignore Path to be consistent with isExpectedFieldAnIterable
    return actual instanceof Iterable && !(actual instanceof Path);
  }

  public boolean isExpectedFieldAnIterable() {
    // Don't consider Path as an Iterable as recursively comparing them leads to a stack overflow, here's why:
    // Iterable are compared element by element recursively
    // Ex: /tmp/foo.txt path has /tmp as its first element
    // so /tmp is going to be compared recursively but /tmp first element is itself leading to an infinite recursion
    return expected instanceof Iterable && !(expected instanceof Path);
  }

  private static boolean isAnOrderedCollection(Object value) {
    return Stream.of(DEFAULT_ORDERED_COLLECTION_TYPES).anyMatch(type -> type.isInstance(value));
  }

  public boolean isExpectedAnEnum() {
    return expected.getClass().isEnum();
  }

  public boolean isActualAnEnum() {
    return actual != null && actual.getClass().isEnum();
  }

  public boolean hasNoContainerValues() {
    return !isContainer(actual) && !isContainer(expected);
  }

  public boolean hasNoNullValues() {
    return actual != null && expected != null;
  }

  private static boolean isContainer(Object o) {
    return o instanceof Iterable ||
           o instanceof Map ||
           o instanceof Optional ||
           isArray(o);
  }

  public boolean hasPotentialCyclingValues() {
    return isPotentialCyclingValue(actual) && isPotentialCyclingValue(expected);
  }

  private static boolean isPotentialCyclingValue(Object object) {
    if (object == null) return false;
    // java.lang are base types that can't cycle to themselves or other types
    // we could check more types, but that's a good start
    String canonicalName = object.getClass().getCanonicalName();
    // canonicalName is null for anonymous and local classes, return true as they can cycle back to other objects.
    if (canonicalName == null) return true;
    // enums can refer back to other object but since they are constants it is very unlikely that they generate cycles.
    if (object.getClass().isEnum()) return false;
    return !canonicalName.startsWith("java.lang");
  }

}
