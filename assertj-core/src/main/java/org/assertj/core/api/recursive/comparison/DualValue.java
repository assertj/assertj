/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.System.identityHashCode;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.assertj.core.internal.RecursiveHelper.isContainer;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.isArray;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
/**
 * Holds actual and expected values together with their location in an object graph.
 */
public final class DualValue {

  static final Class<?>[] DEFAULT_ORDERED_COLLECTION_TYPES = array(List.class, SortedSet.class, LinkedHashSet.class);

  final FieldLocation fieldLocation;
  final Object actual;
  final Object expected;
  private final DualValue parentDualValue;
  private final int hashCode;

  static DualValue rootDualValue(Object actual, Object expected) {
    return new DualValue(rootFieldLocation(), actual, expected, null);
  }

  /**
   * Creates a dual value.
   *
   * @param fieldLocation the field location
   * @param actualFieldValue the actual field value
   * @param expectedFieldValue the expected field value
   * @param parentDualValue the parent dual value
   */
  public DualValue(FieldLocation fieldLocation, Object actualFieldValue, Object expectedFieldValue, DualValue parentDualValue) {
    this.fieldLocation = requireNonNull(fieldLocation, "fieldLocation must not be null");
    actual = actualFieldValue;
    expected = expectedFieldValue;
    this.parentDualValue = parentDualValue;
    hashCode = computeHashCode();
  }

  private int computeHashCode() {
    return identityHashCode(actual) + identityHashCode(expected) + fieldLocation.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof DualValue that)) return false;
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
    return "DualValue [fieldLocation=%s, actual=%s, expected=%s]".formatted(fieldLocation, actual, expected);
  }

  /**
   * Returns the expected value type description.
   *
   * @return the expected type description
   */
  public String getExpectedTypeDescription() {
    return expected == null ? "" : expected.getClass().getCanonicalName();
  }

  /**
   * Returns the actual value type description.
   *
   * @return the actual type description
   */
  public String getActualTypeDescription() {
    return actual == null ? "" : actual.getClass().getCanonicalName();
  }

  /**
   * Returns the decomposed field path.
   *
   * @return the decomposed path
   */
  public List<String> getDecomposedPath() {
    return fieldLocation.getDecomposedPath();
  }

  /**
   * Returns the concatenated field path.
   *
   * @return the concatenated path
   */
  public String getConcatenatedPath() {
    return fieldLocation.getPathToUseInRules();
  }

  /**
   * Returns the field name.
   *
   * @return the field name
   */
  public String getFieldName() {
    return fieldLocation.getFieldName();
  }

  /**
   * Checks whether the actual value has a Java type.
   *
   * @return whether the actual value has a Java type
   */
  public boolean isActualJavaType() {
    return isJavaType(actual);
  }

  /**
   * Checks whether the expected value has a Java type.
   *
   * @return whether the expected value has a Java type
   */
  public boolean isExpectedJavaType() {
    return isJavaType(expected);
  }

  /**
   * Checks whether either value has a Java type.
   *
   * @return whether either value has a Java type
   */
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

  /**
   * Checks whether the expected value is an array.
   *
   * @return whether the expected value is an array
   */
  public boolean isExpectedAnArray() {
    return isArray(expected);
  }

  /**
   * Checks whether the actual value is an array.
   *
   * @return whether the actual value is an array
   */
  public boolean isActualAnArray() {
    return isArray(actual);
  }

  /**
   * Checks whether the actual value is an optional.
   *
   * @return whether the actual value is an optional
   */
  public boolean isActualAnOptional() {
    return actual instanceof Optional;
  }

  /**
   * Checks whether the actual value is an optional integer.
   *
   * @return whether the actual value is an optional integer
   */
  public boolean isActualAnOptionalInt() {
    return actual instanceof OptionalInt;
  }

  /**
   * Checks whether the actual value is an optional long.
   *
   * @return whether the actual value is an optional long
   */
  public boolean isActualAnOptionalLong() {
    return actual instanceof OptionalLong;
  }

  /**
   * Checks whether the actual value is an optional double.
   *
   * @return whether the actual value is an optional double
   */
  public boolean isActualAnOptionalDouble() {
    return actual instanceof OptionalDouble;
  }

  /**
   * Checks whether the actual value is an empty optional of any supported type.
   *
   * @return whether the actual value is an empty optional
   */
  public boolean isActualAnEmptyOptionalOfAnyType() {
    return isActualAnEmptyOptional()
           || isActualAnEmptyOptionalInt()
           || isActualAnEmptyOptionalLong()
           || isActualAnEmptyOptionalDouble();
  }

  private boolean isActualAnEmptyOptional() {
    return isActualAnOptional() && ((Optional<?>) actual).isEmpty();
  }

  private boolean isActualAnEmptyOptionalInt() {
    return isActualAnOptionalInt() && ((OptionalInt) actual).isEmpty();
  }

  private boolean isActualAnEmptyOptionalLong() {
    return isActualAnOptionalLong() && ((OptionalLong) actual).isEmpty();
  }

  private boolean isActualAnEmptyOptionalDouble() {
    return isActualAnOptionalDouble() && ((OptionalDouble) actual).isEmpty();
  }

  /**
   * Checks whether the expected value is an optional.
   *
   * @return whether the expected value is an optional
   */
  public boolean isExpectedAnOptional() {
    return expected instanceof Optional;
  }

  /**
   * Checks whether the expected value is an atomic reference.
   *
   * @return whether the expected value is an atomic reference
   */
  public boolean isExpectedAnAtomicReference() {
    return expected instanceof AtomicReference;
  }

  /**
   * Checks whether the actual value is an atomic reference.
   *
   * @return whether the actual value is an atomic reference
   */
  public boolean isActualAnAtomicReference() {
    return actual instanceof AtomicReference;
  }

  /**
   * Checks whether the expected value is an atomic reference array.
   *
   * @return whether the expected value is an atomic reference array
   */
  public boolean isExpectedAnAtomicReferenceArray() {
    return expected instanceof AtomicReferenceArray;
  }

  /**
   * Checks whether the actual value is an atomic reference array.
   *
   * @return whether the actual value is an atomic reference array
   */
  public boolean isActualAnAtomicReferenceArray() {
    return actual instanceof AtomicReferenceArray;
  }

  /**
   * Checks whether the expected value is an atomic integer.
   *
   * @return whether the expected value is an atomic integer
   */
  public boolean isExpectedAnAtomicInteger() {
    return expected instanceof AtomicInteger;
  }

  /**
   * Checks whether the actual value is an atomic integer.
   *
   * @return whether the actual value is an atomic integer
   */
  public boolean isActualAnAtomicInteger() {
    return actual instanceof AtomicInteger;
  }

  /**
   * Checks whether the expected value is an atomic integer array.
   *
   * @return whether the expected value is an atomic integer array
   */
  public boolean isExpectedAnAtomicIntegerArray() {
    return expected instanceof AtomicIntegerArray;
  }

  /**
   * Checks whether the actual value is an atomic integer array.
   *
   * @return whether the actual value is an atomic integer array
   */
  public boolean isActualAnAtomicIntegerArray() {
    return actual instanceof AtomicIntegerArray;
  }

  /**
   * Checks whether the expected value is an atomic long.
   *
   * @return whether the expected value is an atomic long
   */
  public boolean isExpectedAnAtomicLong() {
    return expected instanceof AtomicLong;
  }

  /**
   * Checks whether the actual value is an atomic long.
   *
   * @return whether the actual value is an atomic long
   */
  public boolean isActualAnAtomicLong() {
    return actual instanceof AtomicLong;
  }

  /**
   * Checks whether the expected value is an atomic long array.
   *
   * @return whether the expected value is an atomic long array
   */
  public boolean isExpectedAnAtomicLongArray() {
    return expected instanceof AtomicLongArray;
  }

  /**
   * Checks whether the actual value is an atomic long array.
   *
   * @return whether the actual value is an atomic long array
   */
  public boolean isActualAnAtomicLongArray() {
    return actual instanceof AtomicLongArray;
  }

  /**
   * Checks whether the expected value is an atomic boolean.
   *
   * @return whether the expected value is an atomic boolean
   */
  public boolean isExpectedAnAtomicBoolean() {
    return expected instanceof AtomicBoolean;
  }

  /**
   * Checks whether the actual value is an atomic boolean.
   *
   * @return whether the actual value is an atomic boolean
   */
  public boolean isActualAnAtomicBoolean() {
    return actual instanceof AtomicBoolean;
  }

  /**
   * Checks whether the actual value is a map.
   *
   * @return whether the actual value is a map
   */
  public boolean isActualAMap() {
    return actual instanceof Map;
  }

  /**
   * Checks whether the expected value is a map.
   *
   * @return whether the expected value is a map
   */
  public boolean isExpectedAMap() {
    return expected instanceof Map;
  }

  /**
   * Checks whether the actual value is a sorted map.
   *
   * @return whether the actual value is a sorted map
   */
  public boolean isActualASortedMap() {
    return actual instanceof SortedMap;
  }

  /**
   * Checks whether the expected value is a sorted map.
   *
   * @return whether the expected value is a sorted map
   */
  public boolean isExpectedASortedMap() {
    return expected instanceof SortedMap;
  }

  /**
   * Checks whether the actual value is an ordered collection.
   *
   * @return whether the actual value is an ordered collection
   */
  public boolean isActualAnOrderedCollection() {
    return isAnOrderedCollection(actual);
  }

  /**
   * Checks whether the expected value is an ordered collection.
   *
   * @return whether the expected value is an ordered collection
   */
  public boolean isExpectedAnOrderedCollection() {
    return isAnOrderedCollection(expected);
  }

  /**
   * Checks whether the actual value is iterable.
   *
   * @return whether the actual value is iterable
   */
  public boolean isActualAnIterable() {
    return isAnIterable(actual);
  }

  /**
   * Checks whether the expected value is iterable.
   *
   * @return whether the expected value is iterable
   */
  public boolean isExpectedAnIterable() {
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

  /**
   * Checks whether the actual value is a throwable.
   *
   * @return whether the actual value is a throwable
   */
  public boolean isActualAThrowable() {
    return actual != null && actual instanceof Throwable;
  }

  /**
   * Checks whether the expected value is a throwable.
   *
   * @return whether the expected value is a throwable
   */
  public boolean isExpectedAThrowable() {
    return expected != null && expected instanceof Throwable;
  }

  /**
   * Checks whether the expected value is an enum.
   *
   * @return whether the expected value is an enum
   */
  public boolean isExpectedAnEnum() {
    return expected != null && expected.getClass().isEnum();
  }

  /**
   * Checks whether the actual value is an enum.
   *
   * @return whether the actual value is an enum
   */
  public boolean isActualAnEnum() {
    return actual != null && actual.getClass().isEnum();
  }

  /**
   * Checks whether neither value is a container.
   *
   * @return whether neither value is a container
   */
  public boolean hasNoContainerValues() {
    return !isContainer(actual) && !isExpectedAContainer();
  }

  /**
   * Checks whether the expected value is a container.
   *
   * @return whether the expected value is a container
   */
  public boolean isExpectedAContainer() {
    return isContainer(expected);
  }

  /**
   * Checks whether neither value is {@code null}.
   *
   * @return whether neither value is {@code null}
   */
  public boolean hasNoNullValues() {
    return actual != null && expected != null;
  }

  /**
   * Checks whether the values may form a comparison cycle.
   *
   * @return whether the values may cycle
   */
  public boolean hasPotentialCyclingValues() {
    return isPotentialCyclingValue(actual) && isPotentialCyclingValue(expected);
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

  boolean hasAncestor(DualValue dualValue) {
    DualValue ancestorDualValue = parentDualValue;
    while (ancestorDualValue != null) {
      if (ancestorDualValue.sameValues(dualValue)) return true;
      else ancestorDualValue = ancestorDualValue.parentDualValue;
    }
    return false;
  }
}
