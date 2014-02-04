/*
 * Created on Aug 4, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeExactlyInstanceOf.shouldBeExactlyInstance;
import static org.assertj.core.error.ShouldBeIn.shouldBeIn;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstanceButWasNull;
import static org.assertj.core.error.ShouldBeInstanceOfAny.shouldBeInstanceOfAny;
import static org.assertj.core.error.ShouldBeEqualByComparingOnlyGivenFields.shouldBeEqualComparingOnlyGivenFields;
import static org.assertj.core.error.ShouldBeEqualToIgnoringFields.shouldBeEqualToIgnoringGivenFields;
import static org.assertj.core.error.ShouldBeOfClassIn.shouldBeOfClassIn;
import static org.assertj.core.error.ShouldBeSame.shouldBeSame;
import static org.assertj.core.error.ShouldHaveSameClass.shouldHaveSameClass;
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
import static org.assertj.core.util.Lists.*;
import static org.assertj.core.util.Sets.*;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

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
    return comparisonStrategy instanceof ComparatorBasedComparisonStrategy ?
      ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator() : null;
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
    return comparisonStrategy.areEqual(other, actual);
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
  public <A> void assertIsLenientEqualsToIgnoringNullFields(AssertionInfo info, A actual, A other) {
    assertNotNull(info, actual);
    assertOtherTypeIsCompatibleWithActualClass(info, other, actual.getClass());
    List<String> fieldsNames = new LinkedList<String>();
    List<Object> rejectedValues = new LinkedList<Object>();
    List<Object> expectedValues = new LinkedList<Object>();
    List<String> nullFields = new LinkedList<String>();
    for (Field field : getDeclaredFieldsIncludingInherited(actual.getClass())) {
      try {
        Object otherFieldValue = getFieldOrPropertyValue(other, field);
        if (otherFieldValue == null) {
          nullFields.add(field.getName());
        } else {
          Object actualFieldValue = getFieldOrPropertyValue(actual, field);
          if (!otherFieldValue.equals(actualFieldValue)) {
            fieldsNames.add(field.getName());
            rejectedValues.add(actualFieldValue);
            expectedValues.add(otherFieldValue);
          }
        }
      } catch (IntrospectionError e) {
        // Not readable field, skip.
      }
    }
    if (!fieldsNames.isEmpty())
      throw failures.failure(info, shouldBeEqualToIgnoringGivenFields(actual, fieldsNames, rejectedValues, expectedValues, nullFields));
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
    assertOtherTypeIsCompatibleWithActualClass(info, other, actual.getClass());
    ByFieldsComparison byFieldsComparison = isEqualToComparingOnlyGivenFields(actual, other, fields);
    if (byFieldsComparison.isFieldsNamesNotEmpty())
      throw failures.failure(info, shouldBeEqualComparingOnlyGivenFields(actual, byFieldsComparison.fieldsNames,
                                                                         byFieldsComparison.rejectedValues,
                                                                         byFieldsComparison.expectedValues,
                                                                         newArrayList(fields)));
  }

  private <A> ByFieldsComparison isEqualToComparingOnlyGivenFields(A actual, A other, String[] fields) {
    List<String> rejectedFieldsNames = new LinkedList<String>();
    List<Object> expectedValues = new LinkedList<Object>();
    List<Object> rejectedValues = new LinkedList<Object>();
    final Set<Field> declaredFieldsIncludingInherited = getDeclaredFieldsIncludingInherited(actual.getClass());
    for (String fieldName : fields) {
      Object actualFieldValue = getFieldOrPropertyValue(actual, findField(fieldName,
                                                                          declaredFieldsIncludingInherited,
                                                                          actual.getClass()));
      Object otherFieldValue = getFieldOrPropertyValue(other, findField(fieldName, declaredFieldsIncludingInherited,
                                                                        other.getClass()));
      if (!org.assertj.core.util.Objects.areEqual(actualFieldValue, otherFieldValue)) {
        rejectedFieldsNames.add(fieldName);
        expectedValues.add(otherFieldValue);
        rejectedValues.add(actualFieldValue);
      }
    }
    return new ByFieldsComparison(rejectedFieldsNames, expectedValues, rejectedValues);
  }

  /**
   * Find field with given fieldName in fields of Class clazz.
   * @param fieldName the field name used to find field in fields
   * @param fields Fields to look into
   * @param clazz use for the exception to indicate to whihc class fields belonged.
   * @return the field with given field name
   * @throws IntrospectionError if no field with given fieldName can be found.
   */
  private Field findField(String fieldName, Set<Field> fields, Class<?> clazz) {
    if (fieldName == null) return null;
    for (Field field : fields) {
      if (fieldName.equals(field.getName())) return field;
    }
    throw new IntrospectionError(format("No field '%s' in %s", fieldName, clazz));
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
    assertOtherTypeIsCompatibleWithActualClass(info, other, actual.getClass());
    ByFieldsComparison byFieldsComparison = isEqualToIgnoringGivenFields(actual, other, fields);
    if (byFieldsComparison.isFieldsNamesNotEmpty())
      throw failures.failure(info, shouldBeEqualToIgnoringGivenFields(actual, byFieldsComparison.fieldsNames,
                                                                      byFieldsComparison.rejectedValues,
                                                                      byFieldsComparison.expectedValues,
                                                                      newArrayList(fields)));
  }

  private <A> ByFieldsComparison isEqualToIgnoringGivenFields(A actual, A other, String[] fields) {
    List<String> fieldsNames = new LinkedList<String>();
    List<Object> expectedValues = new LinkedList<Object>();
    List<Object> rejectedValues = new LinkedList<Object>();
    Set<String> ignoredFields = newLinkedHashSet(fields);
    for (Field field : getDeclaredFieldsIncludingInherited(actual.getClass())) {
      try {
        if (!ignoredFields.contains(field.getName())) {
          Object actualFieldValue = getFieldOrPropertyValue(actual, field);
          Object otherFieldValue = getFieldOrPropertyValue(other, field);
          if (!org.assertj.core.util.Objects.areEqual(actualFieldValue, otherFieldValue)) {
            fieldsNames.add(field.getName());
            rejectedValues.add(actualFieldValue);
            expectedValues.add(otherFieldValue);
          }
        }
      } catch (IntrospectionError e) {
        // Not readable field, skip.
      }
    }
    return new ByFieldsComparison(fieldsNames, expectedValues, rejectedValues);
  }

  /**
   * Get field value first and in case of error try its value from property getter (property name being field name)
   *
   * @param a     the object to get field value from
   * @param field Field to read
   * @param <A>   the type of object a
   * @return field value or property value if field was not accessible.
   * @throws IntrospectionError is field value can't get retrieved.
   */
  private <A> Object getFieldOrPropertyValue(A a, Field field)  {
    try {
      // read field value
      return field.get(a);
    } catch (IllegalAccessException e) {
      // field is not accessible, let's try to get its value from its getter if any.
      return propertySupport.propertyValue(field.getName(), Object.class, a);
    }
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

  /**
   * Verifies that other object is an instance of the given type.
   *
   * @param info contains information about the assertion.
   * @param other the object to check type against given class.
   * @param clazz the type to check the given object against.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if other is {@code null}.
   * @throws AssertionError if other is not an instance of the given type.
   */
  private void assertOtherTypeIsCompatibleWithActualClass(AssertionInfo info, Object other, Class<?> clazz) {
    if (other == null) throw failures.failure(info, shouldBeInstanceButWasNull("other", clazz));
    isInstanceOf(other, clazz, info);
  }

  private void isInstanceOf(Object object, Class<?> clazz, AssertionInfo info) {
    if (!clazz.isInstance(object)) throw failures.failure(info, shouldBeInstance(object, clazz));
  }

  public boolean areEqualToIgnoringGivenFields(Object actual, Object other, String... fields) {
    return isEqualToIgnoringGivenFields(actual, other, fields).isFieldsNamesEmpty();
  }

  public boolean areEqualToComparingOnlyGivenFields(Object actual, Object other, String... fields) {
    return isEqualToComparingOnlyGivenFields(actual, other, fields).isFieldsNamesEmpty();
  }

  private class ByFieldsComparison {

    private final List<String> fieldsNames;
    private final List<Object> expectedValues;
    private final List<Object> rejectedValues;

    public ByFieldsComparison(final List<String> fieldsNames, final List<Object> expectedValues,
                              final List<Object> rejectedValues) {
      this.fieldsNames = fieldsNames;
      this.expectedValues = expectedValues;
      this.rejectedValues = rejectedValues;
    }

    public boolean isFieldsNamesEmpty() {
      return fieldsNames.isEmpty();
    }

    public boolean isFieldsNamesNotEmpty() {
      return !isFieldsNamesEmpty();
    }
  }
}
