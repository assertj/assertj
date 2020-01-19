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

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursive;
import static org.assertj.core.error.ShouldBeEqualByComparingOnlyGivenFields.shouldBeEqualComparingOnlyGivenFields;
import static org.assertj.core.error.ShouldBeEqualToIgnoringFields.shouldBeEqualToIgnoringGivenFields;
import static org.assertj.core.error.ShouldBeExactlyInstanceOf.shouldBeExactlyInstance;
import static org.assertj.core.error.ShouldBeIn.shouldBeIn;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.error.ShouldBeInstanceOfAny.shouldBeInstanceOfAny;
import static org.assertj.core.error.ShouldBeOfClassIn.shouldBeOfClassIn;
import static org.assertj.core.error.ShouldBeSame.shouldBeSame;
import static org.assertj.core.error.ShouldHaveAllNullFields.shouldHaveAllNullFields;
import static org.assertj.core.error.ShouldHaveNoNullFields.shouldHaveNoNullFieldsExcept;
import static org.assertj.core.error.ShouldHavePropertyOrField.shouldHavePropertyOrField;
import static org.assertj.core.error.ShouldHavePropertyOrFieldWithValue.shouldHavePropertyOrFieldWithValue;
import static org.assertj.core.error.ShouldHaveSameClass.shouldHaveSameClass;
import static org.assertj.core.error.ShouldHaveSameHashCode.shouldHaveSameHashCode;
import static org.assertj.core.error.ShouldHaveToString.shouldHaveToString;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.error.ShouldNotBeExactlyInstanceOf.shouldNotBeExactlyInstance;
import static org.assertj.core.error.ShouldNotBeIn.shouldNotBeIn;
import static org.assertj.core.error.ShouldNotBeInstance.shouldNotBeInstance;
import static org.assertj.core.error.ShouldNotBeInstanceOfAny.shouldNotBeInstanceOfAny;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotBeOfClassIn.shouldNotBeOfClassIn;
import static org.assertj.core.error.ShouldNotBeSame.shouldNotBeSame;
import static org.assertj.core.error.ShouldNotHaveSameClass.shouldNotHaveSameClass;
import static org.assertj.core.internal.CommonValidations.checkTypeIsNotNull;
import static org.assertj.core.internal.DeepDifference.determineDifferences;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DeepDifference.Difference;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.core.util.introspection.IntrospectionError;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.assertj.core.util.introspection.PropertySupport;

/**
 * Reusable assertions for {@code Object}s.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class Objects {

  private static final Objects INSTANCE = new Objects();
  @VisibleForTesting
  final PropertySupport propertySupport = PropertySupport.instance();
  private final ComparisonStrategy comparisonStrategy;
  @VisibleForTesting
  Failures failures = Failures.instance();
  private final FieldSupport fieldSupport = FieldSupport.comparison();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   *
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Objects instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Objects() {
    this(StandardComparisonStrategy.instance());
  }

  public Objects(ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    return comparisonStrategy instanceof ComparatorBasedComparisonStrategy
        ? ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator()
        : null;
  }

  @VisibleForTesting
  public ComparisonStrategy getComparisonStrategy() {
    return comparisonStrategy;
  }

  @VisibleForTesting
  public Failures getFailures() {
    return failures;
  }

  /**
   * Verifies that the given object is an instance of the given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param type the type to check the given object against.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the given object is {@code null}.
   * @throws AssertionError if the given object is not an instance of the given type.
   */
  public void assertIsInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (!isInstanceOfClass(actual, type, info)) throw failures.failure(info, shouldBeInstance(actual, type));
  }

  /**
   * Verifies that the given object is an instance of any of the given types.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param types the types to check the given object against.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws NullPointerException if the given array has {@code null} elements.
   * @throws AssertionError if the given object is {@code null}.
   * @throws AssertionError if the given object is not an instance of any of the given types.
   */
  public void assertIsInstanceOfAny(AssertionInfo info, Object actual, Class<?>[] types) {
    if (objectIsInstanceOfOneOfGivenClasses(actual, types, info)) return;
    throw failures.failure(info, shouldBeInstanceOfAny(actual, types));
  }

  private boolean objectIsInstanceOfOneOfGivenClasses(Object actual, Class<?>[] types, AssertionInfo info) {
    checkIsNotNullAndIsNotEmpty(types);
    assertNotNull(info, actual);
    for (Class<?> type : types) {
      String format = "The given array of types:<%s> should not have null elements";
      requireNonNull(type, format(format, info.representation().toStringOf(types)));
      if (type.isInstance(actual)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Verifies that the given object is not an instance of the given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param type the type to check the given object against.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the given object is {@code null}.
   * @throws AssertionError if the given object is an instance of the given type.
   */
  public void assertIsNotInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (isInstanceOfClass(actual, type, info)) throw failures.failure(info, shouldNotBeInstance(actual, type));
  }

  private boolean isInstanceOfClass(Object actual, Class<?> clazz, AssertionInfo info) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(clazz);
    return clazz.isInstance(actual);
  }

  /**
   * Verifies that the given object is not an instance of any of the given types.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param types the types to check the given object against.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws NullPointerException if the given array has {@code null} elements.
   * @throws AssertionError if the given object is {@code null}.
   * @throws AssertionError if the given object is an instance of any of the given types.
   */
  public void assertIsNotInstanceOfAny(AssertionInfo info, Object actual, Class<?>[] types) {
    if (!objectIsInstanceOfOneOfGivenClasses(actual, types, info)) return;
    throw failures.failure(info, shouldNotBeInstanceOfAny(actual, types));
  }

  /**
   * Verifies that the actual value has the same class as the given object.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to check type against.
   * @throws AssertionError if the actual has not the same type has the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  public void assertHasSameClassAs(AssertionInfo info, Object actual, Object other) {
    if (!haveSameClass(actual, other, info)) throw failures.failure(info, shouldHaveSameClass(actual, other));
  }

  private boolean haveSameClass(Object actual, Object other, AssertionInfo info) {
    assertNotNull(info, actual);
    requireNonNull(other, "The given object should not be null");
    Class<?> actualClass = actual.getClass();
    Class<?> otherClass = other.getClass();
    return actualClass.equals(otherClass);
  }

  /**
   * Verifies that the actual value does not have the same class as the given object.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to check type against.
   * @throws AssertionError if the actual has the same type has the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  public void assertDoesNotHaveSameClassAs(AssertionInfo info, Object actual, Object other) {
    if (haveSameClass(actual, other, info)) throw failures.failure(info, shouldNotHaveSameClass(actual, other));
  }

  /**
   * Verifies that the actual value is exactly an instance of given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param type the type to check the actual value against.
   * @throws AssertionError if the actual is not exactly an instance of given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  public void assertIsExactlyInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (!actualIsExactlyInstanceOfType(actual, type, info))
      throw failures.failure(info, shouldBeExactlyInstance(actual, type));
  }

  private boolean actualIsExactlyInstanceOfType(Object actual, Class<?> expectedType, AssertionInfo info) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(expectedType);
    return expectedType.equals(actual.getClass());
  }

  /**
   * Verifies that the actual value is not exactly an instance of given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param type the type to check the actual value against.
   * @throws AssertionError if the actual is exactly an instance of given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  public void assertIsNotExactlyInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (actualIsExactlyInstanceOfType(actual, type, info))
      throw failures.failure(info, shouldNotBeExactlyInstance(actual, type));
  }

  /**
   * Verifies that the actual value type is in given types.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param types the types to check the actual value against.
   * @throws AssertionError if the actual value type is in given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  public void assertIsOfAnyClassIn(AssertionInfo info, Object actual, Class<?>[] types) {
    boolean itemInArray = isOfOneOfGivenTypes(actual, types, info);
    if (!itemInArray) throw failures.failure(info, shouldBeOfClassIn(actual, types));
  }

  private boolean isOfOneOfGivenTypes(Object actual, Class<?>[] types, AssertionInfo info) {
    assertNotNull(info, actual);
    requireNonNull(types, "The given types should not be null");
    return isItemInArray(actual.getClass(), types);
  }

  /**
   * Verifies that the actual value type is not in given types.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param types the types to check the actual value against.
   * @throws AssertionError if the actual value type is in given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  public void assertIsNotOfAnyClassIn(AssertionInfo info, Object actual, Class<?>[] types) {
    boolean itemInArray = isOfOneOfGivenTypes(actual, types, info);
    if (itemInArray) throw failures.failure(info, shouldNotBeOfClassIn(actual, types));
  }

  private void checkIsNotNullAndIsNotEmpty(Class<?>[] types) {
    requireNonNull(types, "The given array of types should not be null");
    checkArgument(types.length > 0, "The given array of types should not be empty");
  }

  /**
   * Asserts that two objects are equal.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" object.
   * @param expected the "expected" object.
   * @throws AssertionError if {@code actual} is not equal to {@code expected}. This method will throw a
   *           {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the given objects are not
   *           equal.
   */
  public void assertEqual(AssertionInfo info, Object actual, Object expected) {
    if (!areEqual(actual, expected))
      throw failures.failure(info, shouldBeEqual(actual, expected, comparisonStrategy, info.representation()));
  }

  /**
   * Asserts that two objects are not equal.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @throws AssertionError if {@code actual} is equal to {@code other}.
   */
  public void assertNotEqual(AssertionInfo info, Object actual, Object other) {
    if (areEqual(actual, other)) throw failures.failure(info, shouldNotBeEqual(actual, other, comparisonStrategy));
  }

  /**
   * Compares actual and other with standard strategy (null safe equals check).
   *
   * @param actual the object to compare to other
   * @param other the object to compare to actual
   * @return true if actual and other are equal (null safe equals check), false otherwise.
   */
  private boolean areEqual(Object actual, Object other) {
    return comparisonStrategy.areEqual(actual, other);
  }

  /**
   * Asserts that the given object is {@code null}.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @throws AssertionError if the given object is not {@code null}.
   */
  public void assertNull(AssertionInfo info, Object actual) {
    if (actual != null) throw failures.failure(info, shouldBeEqual(actual, null, comparisonStrategy, info.representation()));
  }

  /**
   * Asserts that the given object is not {@code null}.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @throws AssertionError if the given object is {@code null}.
   */
  public void assertNotNull(AssertionInfo info, Object actual) {
    if (actual == null) throw failures.failure(info, shouldNotBeNull());
  }

  /**
   * Asserts that the given object is not {@code null}.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param label the label to represent actual in the error message
   * @throws AssertionError if the given object is {@code null}.
   */
  public void assertNotNull(AssertionInfo info, Object actual, String label) {
    if (actual == null) throw failures.failure(info, shouldNotBeNull(label));
  }

  /**
   * Asserts that two objects refer to the same object.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param expected the expected object.
   * @throws AssertionError if the given objects do not refer to the same object.
   */
  public void assertSame(AssertionInfo info, Object actual, Object expected) {
    if (actual != expected) throw failures.failure(info, shouldBeSame(actual, expected));
  }

  /**
   * Asserts that two objects do not refer to the same object.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @throws AssertionError if the given objects refer to the same object.
   */
  public void assertNotSame(AssertionInfo info, Object actual, Object other) {
    if (actual == other) throw failures.failure(info, shouldNotBeSame(actual));
  }

  public void assertHasToString(AssertionInfo info, Object actual, String expectedToString) {
    assertNotNull(info, actual);
    String actualString = actual.toString();
    if (!actualString.equals(expectedToString))
      throw failures.failure(info, shouldHaveToString(actualString, expectedToString), actualString, expectedToString);
  }

  /**
   * Asserts that the given object is present in the given array.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param values the given array.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws AssertionError if the given object is not present in the given array.
   */
  public void assertIsIn(AssertionInfo info, Object actual, Object[] values) {
    checkArrayIsNotNull(values);
    assertIsIn(info, actual, asList(values));
  }

  /**
   * Asserts that the given object is not present in the given array.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param values the given array.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws AssertionError if the given object is present in the given array.
   */
  public void assertIsNotIn(AssertionInfo info, Object actual, Object[] values) {
    checkArrayIsNotNull(values);
    assertIsNotIn(info, actual, asList(values));
  }

  private void checkArrayIsNotNull(Object[] values) {
    requireNonNull(values, "The given array should not be null");
  }

  /**
   * Returns <code>true</code> if given item is in given array, <code>false</code> otherwise.
   *
   * @param item the object to look for in arrayOfValues
   * @param arrayOfValues the array of values
   * @return <code>true</code> if given item is in given array, <code>false</code> otherwise.
   */
  private boolean isItemInArray(Object item, Object[] arrayOfValues) {
    for (Object value : arrayOfValues) {
      if (areEqual(value, item)) return true;
    }
    return false;
  }

  /**
   * Asserts that the given object is present in the given collection.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param values the given iterable.
   * @throws NullPointerException if the given collection is {@code null}.
   * @throws AssertionError if the given object is not present in the given collection.
   */
  public void assertIsIn(AssertionInfo info, Object actual, Iterable<?> values) {
    checkNotNullIterable(values);
    if (!isActualIn(actual, values)) throw failures.failure(info, shouldBeIn(actual, values, comparisonStrategy));
  }

  /**
   * Asserts that the given object is not present in the given collection.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param values the given collection.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws AssertionError if the given object is present in the given collection.
   */
  public void assertIsNotIn(AssertionInfo info, Object actual, Iterable<?> values) {
    checkNotNullIterable(values);
    if (isActualIn(actual, values)) throw failures.failure(info, shouldNotBeIn(actual, values, comparisonStrategy));
  }

  private void checkNotNullIterable(Iterable<?> values) {
    requireNonNull(values, "The given iterable should not be null");
  }

  private boolean isActualIn(Object actual, Iterable<?> values) {
    for (Object value : values) {
      if (areEqual(actual, value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Assert that the given object is lenient equals by ignoring null fields value on other object (including inherited
   * fields).
   *
   * @param <A> the actual type
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @param comparatorByPropertyOrField comparators use for specific fields
   * @param comparatorByType comparators use for specific types
   * @throws NullPointerException if the actual type is {@code null}.
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public <A> void assertIsEqualToIgnoringNullFields(AssertionInfo info, A actual, A other,
                                                    Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                    TypeComparators comparatorByType) {
    assertNotNull(info, actual);
    List<String> fieldsNames = new LinkedList<>();
    List<Object> rejectedValues = new LinkedList<>();
    List<Object> expectedValues = new LinkedList<>();
    List<String> nullFields = new LinkedList<>();
    for (Field field : getDeclaredFieldsIncludingInherited(actual.getClass())) {
      if (!canReadFieldValue(field, actual)) continue;
      String fieldName = field.getName();
      Object otherFieldValue = getPropertyOrFieldValue(other, fieldName);
      if (otherFieldValue == null) {
        nullFields.add(fieldName);
      } else {
        Object actualFieldValue = getPropertyOrFieldValue(actual, fieldName);
        if (!propertyOrFieldValuesAreEqual(actualFieldValue, otherFieldValue, fieldName,
                                           comparatorByPropertyOrField, comparatorByType)) {
          fieldsNames.add(fieldName);
          rejectedValues.add(actualFieldValue);
          expectedValues.add(otherFieldValue);
        }
      }
    }
    if (!fieldsNames.isEmpty())
      throw failures.failure(info, shouldBeEqualToIgnoringGivenFields(actual, fieldsNames,
                                                                      rejectedValues, expectedValues, nullFields));
  }

  /**
   * Assert that the given object is lenient equals to other object by comparing given fields value only.
   *
   * @param <A> the actual type
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @param comparatorByPropertyOrField comparators use for specific fields
   * @param comparatorByType comparators use for specific types
   * @param fields accepted fields
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   * @throws IntrospectionError if a field does not exist in actual.
   */
  public <A> void assertIsEqualToComparingOnlyGivenFields(AssertionInfo info, A actual, A other,
                                                          Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                          TypeComparators comparatorByType,
                                                          String... fields) {
    assertNotNull(info, actual);
    ByFieldsComparison byFieldsComparison = isEqualToComparingOnlyGivenFields(actual, other,
                                                                              comparatorByPropertyOrField,
                                                                              comparatorByType,
                                                                              fields);
    if (byFieldsComparison.isFieldsNamesNotEmpty())
      throw failures.failure(info, shouldBeEqualComparingOnlyGivenFields(actual, byFieldsComparison.fieldsNames,
                                                                         byFieldsComparison.rejectedValues,
                                                                         byFieldsComparison.expectedValues,
                                                                         newArrayList(fields)));
  }

  private <A> ByFieldsComparison isEqualToComparingOnlyGivenFields(A actual, A other,
                                                                   Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                                   TypeComparators comparatorByType,
                                                                   String[] fields) {
    List<String> rejectedFieldsNames = new LinkedList<>();
    List<Object> expectedValues = new LinkedList<>();
    List<Object> rejectedValues = new LinkedList<>();
    for (String fieldName : fields) {
      Object actualFieldValue = getPropertyOrFieldValue(actual, fieldName);
      Object otherFieldValue = getPropertyOrFieldValue(other, fieldName);
      if (!propertyOrFieldValuesAreEqual(actualFieldValue, otherFieldValue, fieldName, comparatorByPropertyOrField,
                                         comparatorByType)) {
        rejectedFieldsNames.add(fieldName);
        expectedValues.add(otherFieldValue);
        rejectedValues.add(actualFieldValue);
      }
    }
    return new ByFieldsComparison(rejectedFieldsNames, expectedValues, rejectedValues);
  }

  /**
   * Assert that the given object is lenient equals to the other by comparing all fields (including inherited fields)
   * unless given ignored ones.
   *
   * @param <A> the actual type
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @param comparatorByPropertyOrField comparators use for specific fields
   * @param comparatorByType comparators use for specific types
   * @param fields the fields to ignore in comparison
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public <A> void assertIsEqualToIgnoringGivenFields(AssertionInfo info, A actual, A other,
                                                     Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                     TypeComparators comparatorByType, String... fields) {
    assertNotNull(info, actual);
    ByFieldsComparison byFieldsComparison = isEqualToIgnoringGivenFields(actual, other, comparatorByPropertyOrField,
                                                                         comparatorByType, fields);
    if (byFieldsComparison.isFieldsNamesNotEmpty())
      throw failures.failure(info, shouldBeEqualToIgnoringGivenFields(actual, byFieldsComparison.fieldsNames,
                                                                      byFieldsComparison.rejectedValues,
                                                                      byFieldsComparison.expectedValues,
                                                                      newArrayList(fields)));
  }

  private <A> ByFieldsComparison isEqualToIgnoringGivenFields(A actual, A other,
                                                              Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                              TypeComparators comparatorByType,
                                                              String[] givenIgnoredFields) {
    Set<Field> declaredFieldsIncludingInherited = getDeclaredFieldsIncludingInherited(actual.getClass());
    List<String> fieldsNames = new LinkedList<>();
    List<Object> expectedValues = new LinkedList<>();
    List<Object> rejectedValues = new LinkedList<>();
    Set<String> ignoredFields = newLinkedHashSet(givenIgnoredFields);
    for (Field field : declaredFieldsIncludingInherited) {
      // ignore private field if user has decided not to use them in comparison
      String fieldName = field.getName();
      if (ignoredFields.contains(fieldName) || !canReadFieldValue(field, actual)) {
        continue;
      }
      Object actualFieldValue = getPropertyOrFieldValue(actual, fieldName);
      Object otherFieldValue = getPropertyOrFieldValue(other, fieldName);

      if (!propertyOrFieldValuesAreEqual(actualFieldValue, otherFieldValue, fieldName,
                                         comparatorByPropertyOrField, comparatorByType)) {
        fieldsNames.add(fieldName);
        rejectedValues.add(actualFieldValue);
        expectedValues.add(otherFieldValue);
      }
    }
    return new ByFieldsComparison(fieldsNames, expectedValues, rejectedValues);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  static boolean propertyOrFieldValuesAreEqual(Object actualFieldValue, Object otherFieldValue, String fieldName,
                                               Map<String, Comparator<?>> comparatorByPropertyOrField,
                                               TypeComparators comparatorByType) {
    // no need to look into comparators if objects are the same
    if (actualFieldValue == otherFieldValue) return true;
    // check field comparators as they take precedence over type comparators
    Comparator fieldComparator = comparatorByPropertyOrField.get(fieldName);
    if (fieldComparator != null) return fieldComparator.compare(actualFieldValue, otherFieldValue) == 0;
    // check if a type comparators exist for the field type
    Class fieldType = actualFieldValue != null ? actualFieldValue.getClass() : otherFieldValue.getClass();
    Comparator typeComparator = comparatorByType.get(fieldType);
    if (typeComparator != null) return typeComparator.compare(actualFieldValue, otherFieldValue) == 0;
    // default comparison using equals
    return org.assertj.core.util.Objects.areEqual(actualFieldValue, otherFieldValue);
  }

  private <A> boolean canReadFieldValue(Field field, A actual) {
    return fieldSupport.isAllowedToRead(field) || propertySupport.publicGetterExistsFor(field.getName(), actual);
  }

  /**
   * Assert that the given object has no null fields except the given ones.
   *
   * @param <A> the actual type.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param propertiesOrFieldsToIgnore the fields to ignore in comparison.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if some of the fields of the actual object are null.
   */
  public <A> void assertHasNoNullFieldsOrPropertiesExcept(AssertionInfo info, A actual,
                                                          String... propertiesOrFieldsToIgnore) {
    assertNotNull(info, actual);
    Set<Field> declaredFieldsIncludingInherited = getDeclaredFieldsIncludingInherited(actual.getClass());
    List<String> nullFieldNames = new LinkedList<>();
    Set<String> ignoredFields = newLinkedHashSet(propertiesOrFieldsToIgnore);
    for (Field field : declaredFieldsIncludingInherited) {
      // ignore private field if user has decided not to use them in comparison
      String fieldName = field.getName();
      if (ignoredFields.contains(fieldName) || !canReadFieldValue(field, actual)) continue;
      Object actualFieldValue = getPropertyOrFieldValue(actual, fieldName);
      if (actualFieldValue == null) nullFieldNames.add(fieldName);
    }
    if (!nullFieldNames.isEmpty())
      throw failures.failure(info, shouldHaveNoNullFieldsExcept(actual, nullFieldNames,
                                                                newArrayList(propertiesOrFieldsToIgnore)));
  }

  /**
   * Asserts that the given object has null fields except the given ones.
   *
   * @param <A> the actual type.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param propertiesOrFieldsToIgnore the fields to ignore in comparison.
   * @throws AssertionError is actual is {@code null}.
   * @throws AssertionError if some of the fields of the actual object are not null.
   */
  public <A> void assertHasAllNullFieldsOrPropertiesExcept(AssertionInfo info, A actual,
                                                           String... propertiesOrFieldsToIgnore) {
    assertNotNull(info, actual);
    Set<Field> declaredFields = getDeclaredFieldsIncludingInherited(actual.getClass());
    Set<String> ignoredFields = newLinkedHashSet(propertiesOrFieldsToIgnore);
    List<String> nonNullFieldNames = declaredFields.stream()
                                                   .filter(field -> !ignoredFields.contains(field.getName()))
                                                   .filter(field -> canReadFieldValue(field, actual))
                                                   .filter(field -> getPropertyOrFieldValue(actual, field.getName()) != null)
                                                   .map(Field::getName)
                                                   .collect(toList());
    if (!nonNullFieldNames.isEmpty()) {
      throw failures.failure(info, shouldHaveAllNullFields(actual, nonNullFieldNames, list(propertiesOrFieldsToIgnore)));
    }
  }

  /**
   * Assert that the given object is "deeply" equals to other by comparing all fields recursively.
   *
   * @param <A> the actual type
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param comparatorByPropertyOrField comparators use for specific fields
   * @param comparatorByType comparators use for specific types
   * @param other the object to compare {@code actual} to.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual and the given object are not "deeply" equal.
   */
  public <A> void assertIsEqualToComparingFieldByFieldRecursively(AssertionInfo info, Object actual, Object other,
                                                                  Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                                  TypeComparators comparatorByType) {
    assertNotNull(info, actual);
    List<Difference> differences = determineDifferences(actual, other, comparatorByPropertyOrField, comparatorByType);
    if (!differences.isEmpty()) {
      throw failures.failure(info, shouldBeEqualByComparingFieldByFieldRecursive(actual, other, differences,
                                                                                 info.representation()));
    }
  }

  /**
   * Get property value first and in case of error try field value.
   * <p>
   * This method supports nested field/property (e.g. "address.street.number").
   *
   * @param <A> the actual type
   * @param a the object to get field value from
   * @param fieldName Field name to read, can be nested
   * @return (nested) field value or property value if field was not accessible.
   * @throws IntrospectionError is field value can't get retrieved.
   */
  private <A> Object getPropertyOrFieldValue(A a, String fieldName) {
    return PropertyOrFieldSupport.COMPARISON.getValueOf(fieldName, a);
  }

  /**
   * Returns the declared fields of given class and its superclasses stopping at superclass in <code>java.lang</code>
   * package whose fields are not included.
   *
   * @param clazz the class we want the declared fields.
   * @return the declared fields of given class and its superclasses.
   */
  public static Set<Field> getDeclaredFieldsIncludingInherited(Class<?> clazz) {
    requireNonNull(clazz, "expecting Class parameter not to be null");
    Set<Field> declaredFields = getDeclaredFieldsIgnoringSyntheticAndStatic(clazz);
    // get fields declared in superclass
    Class<?> superclazz = clazz.getSuperclass();
    while (superclazz != null && !superclazz.getName().startsWith("java.lang")) {
      declaredFields.addAll(getDeclaredFieldsIgnoringSyntheticAndStatic(superclazz));
      superclazz = superclazz.getSuperclass();
    }
    return declaredFields;
  }

  public static Set<String> getFieldsNames(Class<?> clazz) {
    return getDeclaredFieldsIncludingInherited(clazz).stream()
                                                     .map(Field::getName)
                                                     .collect(toSet());
  }

  /**
   * Returns the declared fields of a given class excluding any synthetic or static fields.
   *
   * Synthetic fields are fields that are generated by the compiler for access purposes, or by instrumentation tools e.g. JaCoCo adds in a $jacocoData field
   * and therefore should be ignored when comparing fields.
   *
   * Static fields are used as constants, and are not associated with an object.
   *
   * @param clazz the class we want the declared fields.
   * @return the declared fields of given class excluding any synthetic fields.
   */
  private static Set<Field> getDeclaredFieldsIgnoringSyntheticAndStatic(Class<?> clazz) {
    return stream(clazz.getDeclaredFields()).filter(field -> !(field.isSynthetic()
                                                               || Modifier.isStatic(field.getModifiers())))
                                            .collect(toCollection(LinkedHashSet::new));
  }

  public boolean areEqualToIgnoringGivenFields(Object actual, Object other,
                                               Map<String, Comparator<?>> comparatorByPropertyOrField,
                                               TypeComparators comparatorByType, String... fields) {
    return isEqualToIgnoringGivenFields(actual, other, comparatorByPropertyOrField, comparatorByType,
                                        fields).isFieldsNamesEmpty();
  }

  public boolean areEqualToComparingOnlyGivenFields(Object actual, Object other,
                                                    Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                    TypeComparators comparatorByType, String... fields) {
    return isEqualToComparingOnlyGivenFields(actual, other, comparatorByPropertyOrField, comparatorByType,
                                             fields).isFieldsNamesEmpty();
  }

  public <A> void assertHasFieldOrProperty(AssertionInfo info, A actual, String name) {
    assertNotNull(info, actual);
    try {
      extractPropertyOrField(actual, name);
    } catch (IntrospectionError error) {
      throw failures.failure(info, shouldHavePropertyOrField(actual, name));
    }
  }

  public <A> void assertHasFieldOrPropertyWithValue(AssertionInfo info, A actual, String name, Object expectedValue) {
    assertHasFieldOrProperty(info, actual, name);
    Object value = extractPropertyOrField(actual, name);
    if (!org.assertj.core.util.Objects.areEqual(value, expectedValue))
      throw failures.failure(info, shouldHavePropertyOrFieldWithValue(actual, name, expectedValue, value));
  }

  private <A> Object extractPropertyOrField(A actual, String name) {
    return PropertyOrFieldSupport.EXTRACTION.getValueOf(name, actual);
  }

  /**
   * Asserts that the actual object has the same hashCode as the given object.
   *
   * @param <A> the actual type
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to check hashCode against.
   *
   * @throws AssertionError if the actual object is null.
   * @throws AssertionError if the given object is null.
   * @throws AssertionError if the actual object has not the same hashCode as the given object.
   */
  public <A> void assertHasSameHashCodeAs(AssertionInfo info, A actual, Object other) {
    assertNotNull(info, actual);
    requireNonNull(other, "The object used to compare actual's hash code with should not be null");
    if (actual.hashCode() != other.hashCode()) throw failures.failure(info, shouldHaveSameHashCode(actual, other));
  }

  public static class ByFieldsComparison {

    private final List<String> fieldsNames;
    private final List<Object> expectedValues;
    private final List<Object> rejectedValues;

    public ByFieldsComparison(final List<String> fieldsNames,
                              final List<Object> expectedValues,
                              final List<Object> rejectedValues) {
      this.fieldsNames = fieldsNames;
      this.expectedValues = expectedValues;
      this.rejectedValues = rejectedValues;
    }

    public ByFieldsComparison() {
      this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public boolean isFieldsNamesEmpty() {
      return fieldsNames.isEmpty();
    }

    public boolean isFieldsNamesNotEmpty() {
      return !isFieldsNamesEmpty();
    }
  }

}
