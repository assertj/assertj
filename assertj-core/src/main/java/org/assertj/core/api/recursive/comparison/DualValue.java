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
 * Copyright 2012-2023 the original author or authors.
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
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
    return actual == that.actual && expected == that.expected && fieldLocation.equals(that.fieldLocation);
  }

  /**
   * If we want to detect potential cycles in the recursive comparison, we need to check if an object has already been visited.
   * <p>
   * We must ignore the {@link FieldLocation} otherwise we would not find cycles. Let's take for example a {@code Person} class
   * with a neighbor field. We have a cycle between the person instance and its neighbor instance, ex: Jack has Tim as neighbor
   * and vice versa, when we navigate to Tim we find that its neighbor is Jack, we have already visited it but the location is
   * different, Jack is both the root object and root.neighbor.neighbor (Jack=root, Tim=root.neighbor and Tim.neighbor=Jack)
   *
   * @param dualValue the {@link DualValue} to compare
   * @return true if dual values references the same values (ignoring the field location)
   */
  public boolean sameValues(DualValue dualValue) {
    return actual == dualValue.actual && expected == dualValue.expected;
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
    return isJavaType(actual);
  }

  public boolean isExpectedJavaType() {
    return isJavaType(expected);
  }

  public boolean hasSomeJavaTypeValue() {
    return isActualJavaType() || isExpectedJavaType();
  }

  private static boolean isJavaType(Object o) {
    if (o == null) return false;
    String className = o.getClass().getName();
    return className.startsWith("java.")
           || className.startsWith("javax.")
           || className.startsWith("sun.")
           || className.startsWith("com.sun.");
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

  public boolean isExpectedFieldAnAtomicReference() {
    return expected instanceof AtomicReference;
  }

  public boolean isActualFieldAnAtomicReference() {
    return actual instanceof AtomicReference;
  }

  public boolean isExpectedFieldAnAtomicReferenceArray() {
    return expected instanceof AtomicReferenceArray;
  }

  public boolean isActualFieldAnAtomicReferenceArray() {
    return actual instanceof AtomicReferenceArray;
  }

  public boolean isExpectedFieldAnAtomicInteger() {
    return expected instanceof AtomicInteger;
  }

  public boolean isActualFieldAnAtomicInteger() {
    return actual instanceof AtomicInteger;
  }

  public boolean isExpectedFieldAnAtomicIntegerArray() {
    return expected instanceof AtomicIntegerArray;
  }

  public boolean isActualFieldAnAtomicIntegerArray() {
    return actual instanceof AtomicIntegerArray;
  }

  public boolean isExpectedFieldAnAtomicLong() {
    return expected instanceof AtomicLong;
  }

  public boolean isActualFieldAnAtomicLong() {
    return actual instanceof AtomicLong;
  }

  public boolean isExpectedFieldAnAtomicLongArray() {
    return expected instanceof AtomicLongArray;
  }

  public boolean isActualFieldAnAtomicLongArray() {
    return actual instanceof AtomicLongArray;
  }

  public boolean isExpectedFieldAnAtomicBoolean() {
    return expected instanceof AtomicBoolean;
  }

  public boolean isActualFieldAnAtomicBoolean() {
    return actual instanceof AtomicBoolean;
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
    return isAnIterable(actual);
  }

  public boolean isExpectedFieldAnIterable() {
    return isAnIterable(expected);
  }

  private static boolean isAnIterable(Object value) {
    // Don't consider Path as an Iterable as recursively comparing them leads to a stack overflow, here's why:
    // Iterable are compared element by element recursively
    // Ex: /tmp/foo.txt path has /tmp as its first element
    // so /tmp is going to be compared recursively but /tmp first element is itself leading to an infinite recursion
    // Don't consider ValueNode as an Iterable as they only contain one value and iterating them does not make sense.
    // Don't consider or ObjectNode as an Iterable as it holds a map but would only iterate on values and not entries.
    return value instanceof Iterable && !(value instanceof Path || isAJsonValueNode(value) || isAnObjectNode(value));
  }

  private static boolean isAJsonValueNode(Object value) {
    try {
      Class<?> valueNodeClass = Class.forName("com.fasterxml.jackson.databind.node.ValueNode");
      return valueNodeClass.isInstance(value);
    } catch (ClassNotFoundException e) {
      // value cannot be a ValueNode because the class couldn't be located
      return false;
    }
  }

  private static boolean isAnObjectNode(Object value) {
    try {
      Class<?> objectNodeClass = Class.forName("com.fasterxml.jackson.databind.node.ObjectNode");
      return objectNodeClass.isInstance(value);
    } catch (ClassNotFoundException e) {
      // value cannot be an ObjectNode because the class couldn't be located
      return false;
    }
  }

  private static boolean isAnOrderedCollection(Object value) {
    return Stream.of(DEFAULT_ORDERED_COLLECTION_TYPES).anyMatch(type -> type.isInstance(value));
  }

  public boolean isExpectedAnEnum() {
    return expected != null && expected.getClass().isEnum();
  }

  public boolean isActualAnEnum() {
    return actual != null && actual.getClass().isEnum();
  }

  public boolean hasNoContainerValues() {
    return !isContainer(actual) && !isExpectedAContainer();
  }

  // TODO test
  public boolean isExpectedAContainer() {
    return isContainer(expected);
  }

  public boolean hasNoNullValues() {
    return actual != null && expected != null;
  }

  private static boolean isContainer(Object o) {
    return o instanceof Iterable ||
           o instanceof Map ||
           o instanceof Optional ||
           o instanceof AtomicReference ||
           o instanceof AtomicReferenceArray ||
           o instanceof AtomicBoolean ||
           o instanceof AtomicInteger ||
           o instanceof AtomicIntegerArray ||
           o instanceof AtomicLong ||
           o instanceof AtomicLongArray ||
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
