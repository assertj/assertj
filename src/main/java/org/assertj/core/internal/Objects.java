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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualByComparingOnlyGivenFields.shouldBeEqualComparingOnlyGivenFields;
import static org.assertj.core.error.ShouldBeEqualToIgnoringFields.shouldBeEqualToIgnoringGivenFields;
import static org.assertj.core.error.ShouldBeExactlyInstanceOf.shouldBeExactlyInstance;
import static org.assertj.core.error.ShouldBeIn.shouldBeIn;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.error.ShouldBeInstanceOfAny.shouldBeInstanceOfAny;
import static org.assertj.core.error.ShouldBeOfClassIn.shouldBeOfClassIn;
import static org.assertj.core.error.ShouldBeSame.shouldBeSame;
import static org.assertj.core.error.ShouldHavePropertyOrField.shouldHavePropertyOrField;
import static org.assertj.core.error.ShouldHavePropertyOrFieldWithValue.shouldHavePropertyOrFieldWithValue;
import static org.assertj.core.error.ShouldHaveSameClass.shouldHaveSameClass;
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
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
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
        ? ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator() : null;
  }

  @VisibleForTesting
  public ComparisonStrategy getComparisonStrategy() {
    return comparisonStrategy;
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
      if (type == null) {
        String format = "The given array of types:<%s> should not have null elements";
        throw new NullPointerException(format(format, info.representation().toStringOf(types)));
      }
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
   * @throws AssertionError if the actual has not the same type has the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  public void assertHasSameClassAs(AssertionInfo info, Object actual, Object other) {
    if (!haveSameClass(actual, other, info)) throw failures.failure(info, shouldHaveSameClass(actual, other));
  }

  private boolean haveSameClass(Object actual, Object other, AssertionInfo info) {
    assertNotNull(info, actual);
    if (other == null) {
      throw new NullPointerException("The given object should not be null");
    }
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
   * Verifies that the actual value is exactly a instance of given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param type the type to check the actual value against.
   * @throws AssertionError if the actual is not exactly a instance of given type.
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
   * Verifies that the actual value is not exactly a instance of given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param type the type to check the actual value against.
   * @throws AssertionError if the actual is exactly a instance of given type.
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
    if (types == null) throw new NullPointerException("The given types should not be null");
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
    if (types == null) {
      throw new NullPointerException("The given array of types should not be null");
    }
    if (types.length == 0) {
      throw new IllegalArgumentException("The given array of types should not be empty");
    }
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
    if (areEqual(actual, expected)) {
      return;
    }
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
    if (!areEqual(actual, other)) {
      return;
    }
    throw failures.failure(info, shouldNotBeEqual(actual, other, comparisonStrategy));
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
    if (actual == null) {
      return;
    }
    throw failures.failure(info, shouldBeEqual(actual, null, comparisonStrategy, info.representation()));
  }

  /**
   * Asserts that the given object is not {@code null}.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @throws AssertionError if the given object is {@code null}.
   */
  public void assertNotNull(AssertionInfo info, Object actual) {
    if (actual != null) {
      return;
    }
    throw failures.failure(info, shouldNotBeNull());
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
    if (actual == expected) {
      return;
    }
    throw failures.failure(info, shouldBeSame(actual, expected));
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
    if (actual != other) {
      return;
    }
    throw failures.failure(info, shouldNotBeSame(actual));
  }

  public void assertHasToString(AssertionInfo info, Object actual, String expectedToString) {
    assertNotNull(info, actual);
    if (!actual.toString().equals(expectedToString))
      throw failures.failure(info, shouldHaveToString(actual, expectedToString));
  }

  /**
   * Asserts that the given object is present in the given array.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param values the given array.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the given object is not present in the given array.
   */
  public void assertIsIn(AssertionInfo info, Object actual, Object[] values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, actual);
    if (isItemInArray(actual, values)) {
      return;
    }
    throw failures.failure(info, shouldBeIn(actual, values, comparisonStrategy));
  }

  /**
   * Asserts that the given object is not present in the given array.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param values the given array.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the given object is present in the given array.
   */
  public void assertIsNotIn(AssertionInfo info, Object actual, Object[] values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, actual);
    if (!isItemInArray(actual, values)) {
      return;
    }
    throw failures.failure(info, shouldNotBeIn(actual, values, comparisonStrategy));
  }

  private void checkIsNotNullAndNotEmpty(Object[] values) {
    if (values == null) {
      throw new NullPointerException("The given array should not be null");
    }
    if (values.length == 0) {
      throw new IllegalArgumentException("The given array should not be empty");
    }
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
   * @throws IllegalArgumentException if the given collection is empty.
   * @throws AssertionError if the given object is not present in the given collection.
   */
  public void assertIsIn(AssertionInfo info, Object actual, Iterable<?> values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, actual);
    if (isActualIn(actual, values)) {
      return;
    }
    throw failures.failure(info, shouldBeIn(actual, values, comparisonStrategy));
  }

  /**
   * Asserts that the given object is not present in the given collection.
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param values the given collection.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws IllegalArgumentException if the given collection is empty.
   * @throws AssertionError if the given object is present in the given collection.
   */
  public void assertIsNotIn(AssertionInfo info, Object actual, Iterable<?> values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, actual);
    if (!isActualIn(actual, values)) {
      return;
    }
    throw failures.failure(info, shouldNotBeIn(actual, values, comparisonStrategy));
  }

  private void checkIsNotNullAndNotEmpty(Iterable<?> values) {
    if (values == null) {
      throw new NullPointerException("The given iterable should not be null");
    }
    if (!values.iterator().hasNext()) {
      throw new IllegalArgumentException("The given iterable should not be empty");
    }
  }

  private boolean isActualIn(Object actual, Iterable<?> values) {
    for (Object value : values) {
      if (areEqual(value, actual)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Assert that the given object is lenient equals by ignoring null fields value on other object (including inherited
   * fields).
   *
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @throws NullPointerException if the actual type is {@code null}.
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public <A> void assertIsEqualToIgnoringNullFields(AssertionInfo info, A actual, A other) {
    assertNotNull(info, actual);
    List<String> fieldsNames = new LinkedList<>();
    List<Object> rejectedValues = new LinkedList<>();
    List<Object> expectedValues = new LinkedList<>();
    List<String> nullFields = new LinkedList<>();
    for (Field field : getDeclaredFieldsIncludingInherited(actual.getClass())) {
      if (!canReadFieldValue(field, actual)) continue;
      Object otherFieldValue = getPropertyOrFieldValue(other, field.getName());
      if (otherFieldValue == null) {
        nullFields.add(field.getName());
      } else {
        Object actualFieldValue = getPropertyOrFieldValue(actual, field.getName());
        if (!otherFieldValue.equals(actualFieldValue)) {
          fieldsNames.add(field.getName());
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
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @param fields accepted fields
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   * @throws IntrospectionError if a field does not exist in actual.
   */
  public <A> void assertIsEqualToComparingOnlyGivenFields(AssertionInfo info, A actual, A other, String... fields) {
    assertNotNull(info, actual);
    ByFieldsComparison byFieldsComparison = isEqualToComparingOnlyGivenFields(actual, other, fields);
    if (byFieldsComparison.isFieldsNamesNotEmpty())
      throw failures.failure(info, shouldBeEqualComparingOnlyGivenFields(actual, byFieldsComparison.fieldsNames,
                                                                         byFieldsComparison.rejectedValues,
                                                                         byFieldsComparison.expectedValues,
                                                                         newArrayList(fields)));
  }

  private <A> ByFieldsComparison isEqualToComparingOnlyGivenFields(A actual, A other, String[] fields) {
    List<String> rejectedFieldsNames = new LinkedList<>();
    List<Object> expectedValues = new LinkedList<>();
    List<Object> rejectedValues = new LinkedList<>();
    for (String fieldName : fields) {
      Object actualFieldValue = getPropertyOrFieldValue(actual, fieldName);
      Object otherFieldValue = getPropertyOrFieldValue(other, fieldName);
      if (!org.assertj.core.util.Objects.areEqual(actualFieldValue, otherFieldValue)) {
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
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @param fields the fields to ignore in comparison
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public <A> void assertIsEqualToIgnoringGivenFields(AssertionInfo info, A actual, A other, String... fields) {
    assertNotNull(info, actual);
    ByFieldsComparison byFieldsComparison = isEqualToIgnoringGivenFields(actual, other, fields);
    if (byFieldsComparison.isFieldsNamesNotEmpty())
      throw failures.failure(info, shouldBeEqualToIgnoringGivenFields(actual, byFieldsComparison.fieldsNames,
                                                                      byFieldsComparison.rejectedValues,
                                                                      byFieldsComparison.expectedValues,
                                                                      newArrayList(fields)));
  }

  private <A> ByFieldsComparison isEqualToIgnoringGivenFields(A actual, A other, String[] givenIgnoredFields) {
    Set<Field> declaredFieldsIncludingInherited = getDeclaredFieldsIncludingInherited(actual.getClass());
    List<String> fieldsNames = new LinkedList<>();
    List<Object> expectedValues = new LinkedList<>();
    List<Object> rejectedValues = new LinkedList<>();
    Set<String> ignoredFields = newLinkedHashSet(givenIgnoredFields);
    for (Field field : declaredFieldsIncludingInherited) {
      // ignore private field if user has decided not to use them in comparison
      if (ignoredFields.contains(field.getName()) || !canReadFieldValue(field, actual)) {
        continue;
      }
      Object actualFieldValue = getPropertyOrFieldValue(actual, field.getName());
      Object otherFieldValue = getPropertyOrFieldValue(other, field.getName());
      if (!org.assertj.core.util.Objects.areEqual(actualFieldValue, otherFieldValue)) {
        fieldsNames.add(field.getName());
        rejectedValues.add(actualFieldValue);
        expectedValues.add(otherFieldValue);
      }
    }
    return new ByFieldsComparison(fieldsNames, expectedValues, rejectedValues);
  }

  private <A> boolean canReadFieldValue(Field field, A actual) {
    return fieldSupport.isAllowedToRead(field) || propertySupport.publicGetterExistsFor(field.getName(), actual);
  }

  /**
   * Get property value first and in case of error try field value.
   * <p>
   * This method supports nested field/property (e.g. "address.street.number").
   *
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
  private static Set<Field> getDeclaredFieldsIncludingInherited(Class<?> clazz) {
    if (clazz == null) throw new NullPointerException("expecting Class parameter not to be null");
    Set<Field> declaredFields = newLinkedHashSet(clazz.getDeclaredFields());
    // get fields declared in superclass
    Class<?> superclazz = clazz.getSuperclass();
    while (superclazz != null && !superclazz.getName().startsWith("java.lang")) {
      declaredFields.addAll(newLinkedHashSet(superclazz.getDeclaredFields()));
      superclazz = superclazz.getSuperclass();
    }
    return declaredFields;
  }

  public boolean areEqualToIgnoringGivenFields(Object actual, Object other, String... fields) {
    return isEqualToIgnoringGivenFields(actual, other, fields).isFieldsNamesEmpty();
  }

  public boolean areEqualToComparingOnlyGivenFields(Object actual, Object other, String... fields) {
    return isEqualToComparingOnlyGivenFields(actual, other, fields).isFieldsNamesEmpty();
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
    if (!java.util.Objects.equals(value, expectedValue))
      throw failures.failure(info, shouldHavePropertyOrFieldWithValue(actual, name, expectedValue, value));
  }

  private <A> Object extractPropertyOrField(A actual, String name) {
    return PropertyOrFieldSupport.EXTRACTION.getValueOf(name, actual);
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
      this(new ArrayList<String>(), new ArrayList<>(), new ArrayList<>());
    }

    public boolean isFieldsNamesEmpty() {
      return fieldsNames.isEmpty();
    }

    public boolean isFieldsNamesNotEmpty() {
      return !isFieldsNamesEmpty();
    }
  }
}
