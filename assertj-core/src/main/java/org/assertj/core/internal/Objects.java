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
package org.assertj.core.internal;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.deepEquals;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeExactlyInstanceOf.shouldBeExactlyInstance;
import static org.assertj.core.error.ShouldBeIn.shouldBeIn;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.error.ShouldBeInstanceOfAny.shouldBeInstanceOfAny;
import static org.assertj.core.error.ShouldBeOfClassIn.shouldBeOfClassIn;
import static org.assertj.core.error.ShouldBeSame.shouldBeSame;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.error.ShouldHaveAllNullFields.shouldHaveAllNullFields;
import static org.assertj.core.error.ShouldHaveFields.shouldHaveDeclaredFields;
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
import static org.assertj.core.error.ShouldNotHaveSameHashCode.shouldNotHaveSameHashCode;
import static org.assertj.core.error.ShouldNotHaveToString.shouldNotHaveToString;
import static org.assertj.core.internal.CommonValidations.checkTypeIsNotNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.introspection.ClassUtils.isInJavaLangPackage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.error.GroupTypeDescription;
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
  private static final GroupTypeDescription FIELDS_GROUP_DESCRIPTION = new GroupTypeDescription("non static/synthetic fields of",
                                                                                                "fields");

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  final PropertySupport propertySupport = PropertySupport.instance();
  private final ComparisonStrategy comparisonStrategy;
  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Failures failures = Failures.instance();
  private final FieldSupport fieldSupport = FieldSupport.comparison();

  /**
   * Returns the shared object assertions instance.
   *
   * @return the shared instance
   */
  public static Objects instance() {
    return INSTANCE;
  }

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Objects() {
    this(StandardComparisonStrategy.instance());
  }

  /**
   * Creates object assertions using the given comparison strategy.
   *
   * @param comparisonStrategy the comparison strategy
   */
  public Objects(ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
  }

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  /**
   * Returns the comparator used by the comparison strategy.
   *
   * @return the configured comparator
   */
  public Comparator<?> getComparator() {
    return comparisonStrategy instanceof ComparatorBasedComparisonStrategy strategy ? strategy.getComparator() : null;
  }

  /**
   * Returns the comparison strategy.
   *
   * @return the comparison strategy
   */
  public ComparisonStrategy getComparisonStrategy() {
    return comparisonStrategy;
  }

  /**
   * Returns the assertion failure provider.
   *
   * @return the failure provider
   */
  public Failures getFailures() {
    return failures;
  }

  /**
   * Verifies that the object is an instance of the given type.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param type the expected type
   */
  public void assertIsInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (!isInstanceOfClass(actual, type, info)) throw failures.failure(info, shouldBeInstance(actual, type));
  }

  /**
   * Verifies that the object is an instance of any given type.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param types the expected types
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
      requireNonNull(type, format.formatted(info.representation().toStringOf(types)));
      if (type.isInstance(actual)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Verifies that the object is not an instance of the given type.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param type the prohibited type
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
   * Verifies that the object is not an instance of any given type.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param types the prohibited types
   */
  public void assertIsNotInstanceOfAny(AssertionInfo info, Object actual, Class<?>[] types) {
    if (!objectIsInstanceOfOneOfGivenClasses(actual, types, info)) return;
    throw failures.failure(info, shouldNotBeInstanceOfAny(actual, types));
  }

  /**
   * Verifies that two objects have the same class.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param other the comparison object
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
   * Verifies that two objects have different classes.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param other the comparison object
   */
  public void assertDoesNotHaveSameClassAs(AssertionInfo info, Object actual, Object other) {
    if (haveSameClass(actual, other, info)) throw failures.failure(info, shouldNotHaveSameClass(actual, other));
  }

  /**
   * Verifies that the object has exactly the given type.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param type the expected type
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
   * Verifies that the object does not have exactly the given type.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param type the prohibited exact type
   */
  public void assertIsNotExactlyInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (actualIsExactlyInstanceOfType(actual, type, info))
      throw failures.failure(info, shouldNotBeExactlyInstance(actual, type));
  }

  /**
   * Verifies that the object's class is one of the given classes.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param types the expected classes
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
   * Verifies that the object's class is none of the given classes.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param types the prohibited classes
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
   * Verifies that two objects are equal.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param expected the expected object
   */
  public void assertEqual(AssertionInfo info, Object actual, Object expected) {
    if (!areEqual(actual, expected))
      throw failures.failure(info, shouldBeEqual(actual, expected, comparisonStrategy, info.representation()));
  }

  /**
   * Verifies that two objects are not equal.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param other the comparison object
   */
  public void assertNotEqual(AssertionInfo info, Object actual, Object other) {
    if (areEqual(actual, other)) throw failures.failure(info, shouldNotBeEqual(actual, other, comparisonStrategy));
  }

  private boolean areEqual(Object actual, Object other) {
    return comparisonStrategy.areEqual(actual, other);
  }

  /**
   * Verifies that the object is {@code null}.
   *
   * @param info assertion information
   * @param actual the actual object
   */
  public void assertNull(AssertionInfo info, Object actual) {
    if (actual != null)
      throw failures.failure(info, shouldBeEqual(actual, null, comparisonStrategy, info.representation()));
  }

  /**
   * Verifies that the object is not {@code null}.
   *
   * @param info assertion information
   * @param actual the actual object
   */
  public void assertNotNull(AssertionInfo info, Object actual) {
    if (actual == null) throw failures.failure(info, shouldNotBeNull());
  }

  /**
   * Verifies that the labeled object is not {@code null}.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param label the object label
   */
  public void assertNotNull(AssertionInfo info, Object actual, String label) {
    if (actual == null) throw failures.failure(info, shouldNotBeNull(label));
  }

  /**
   * Verifies that two references are identical.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param expected the expected reference
   */
  public void assertSame(AssertionInfo info, Object actual, Object expected) {
    if (actual != expected) throw failures.failure(info, shouldBeSame(actual, expected));
  }

  /**
   * Verifies that two references are different.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param other the comparison reference
   */
  public void assertNotSame(AssertionInfo info, Object actual, Object other) {
    if (actual == other) throw failures.failure(info, shouldNotBeSame(actual));
  }

  /**
   * Verifies the object's string representation.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param expectedToString the expected representation
   */
  public void assertHasToString(AssertionInfo info, Object actual, String expectedToString) {
    assertNotNull(info, actual);
    String actualString = actual.toString();
    if (!actualString.equals(expectedToString))
      throw failures.failure(info, shouldHaveToString(actualString, expectedToString), actualString, expectedToString);
  }

  /**
   * Verifies that the object has a different string representation.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param otherToString the prohibited representation
   */
  public void assertDoesNotHaveToString(AssertionInfo info, Object actual, String otherToString) {
    assertNotNull(info, actual);
    String actualToString = actual.toString();
    if (actualToString.equals(otherToString))
      throw failures.failure(info, shouldNotHaveToString(otherToString));
  }

  /**
   * Verifies that the object is in the given array.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param values the candidate values
   */
  public void assertIsIn(AssertionInfo info, Object actual, Object[] values) {
    checkArrayIsNotNull(values);
    assertIsIn(info, actual, asList(values));
  }

  /**
   * Verifies that the object is not in the given array.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param values the prohibited values
   */
  public void assertIsNotIn(AssertionInfo info, Object actual, Object[] values) {
    checkArrayIsNotNull(values);
    assertIsNotIn(info, actual, asList(values));
  }

  private void checkArrayIsNotNull(Object[] values) {
    requireNonNull(values, "The given array should not be null");
  }

  private boolean isItemInArray(Object item, Object[] arrayOfValues) {
    for (Object value : arrayOfValues) {
      if (areEqual(value, item)) return true;
    }
    return false;
  }

  /**
   * Verifies that the object is in the given iterable.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param values the candidate values
   */
  public void assertIsIn(AssertionInfo info, Object actual, Iterable<?> values) {
    checkNotNullIterable(values);
    if (!isActualIn(actual, values)) throw failures.failure(info, shouldBeIn(actual, values, comparisonStrategy));
  }

  /**
   * Verifies that the object is not in the given iterable.
   *
   * @param info assertion information
   * @param actual the actual object
   * @param values the prohibited values
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
    Comparator typeComparator = comparatorByType.getComparatorForType(fieldType);
    if (typeComparator != null) return typeComparator.compare(actualFieldValue, otherFieldValue) == 0;
    // default comparison using equals
    return deepEquals(actualFieldValue, otherFieldValue);
  }

  private <A> boolean canReadFieldValue(Field field, A actual) {
    return fieldSupport.isAllowedToRead(field) || propertySupport.publicGetterExistsFor(field.getName(), actual);
  }

  /**
   * Verifies that readable fields are non-null except those ignored.
   *
   * @param <A> the actual object type
   * @param info assertion information
   * @param actual the actual object
   * @param propertiesOrFieldsToIgnore the fields or properties to ignore
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
      if (ignoredFields.remove(fieldName)) continue;
      if (!canReadFieldValue(field, actual)) continue;
      Object actualFieldValue = getPropertyOrFieldValue(actual, fieldName);
      if (actualFieldValue == null) nullFieldNames.add(fieldName);
    }
    if (!ignoredFields.isEmpty()) {
      WritableAssertionInfo amendedInfo = new WritableAssertionInfo(info.representation());
      String newDescription = info.description() == null ? "" : info.description().value() + " - ";
      amendedInfo.description(newDescription + "ignored fields existence check");
      if (info.overridingErrorMessage() != null) {
        amendedInfo.overridingErrorMessage(info.overridingErrorMessage());
      }
      throw failures.failure(amendedInfo,
                             shouldHaveDeclaredFields(actual.getClass(), newLinkedHashSet(propertiesOrFieldsToIgnore),
                                                      ignoredFields));
    }
    if (!nullFieldNames.isEmpty())
      throw failures.failure(info, shouldHaveNoNullFieldsExcept(actual, nullFieldNames,
                                                                newArrayList(propertiesOrFieldsToIgnore)));
  }

  /**
   * Verifies that readable fields are null except those ignored.
   *
   * @param <A> the actual object type
   * @param info assertion information
   * @param actual the actual object
   * @param propertiesOrFieldsToIgnore the fields or properties to ignore
   */
  public <A> void assertHasAllNullFieldsOrPropertiesExcept(AssertionInfo info, A actual,
                                                           String... propertiesOrFieldsToIgnore) {
    assertNotNull(info, actual);
    Set<Field> declaredFields = getDeclaredFieldsIncludingInherited(actual.getClass());
    Set<String> ignoredFields = newLinkedHashSet(propertiesOrFieldsToIgnore);
    List<String> nonNullFieldNames = declaredFields.stream()
                                                   .filter(field -> !ignoredFields.contains(field.getName()))
                                                   .filter(field -> canReadFieldValue(field, actual))
                                                   .map(Field::getName)
                                                   .filter(name -> getPropertyOrFieldValue(actual, name) != null)
                                                   .collect(toList());
    if (!nonNullFieldNames.isEmpty()) {
      throw failures.failure(info, shouldHaveAllNullFields(actual, nonNullFieldNames, list(propertiesOrFieldsToIgnore)));
    }
  }

  private <A> Object getPropertyOrFieldValue(A a, String fieldName) {
    return PropertyOrFieldSupport.COMPARISON.getValueOf(fieldName, a);
  }

  /**
   * Returns non-static, non-synthetic fields declared by a class and its ancestors.
   *
   * @param clazz the class to inspect
   * @return the declared and inherited fields
   */
  public static Set<Field> getDeclaredFieldsIncludingInherited(Class<?> clazz) {
    requireNonNull(clazz, "expecting Class parameter not to be null");
    Set<Field> declaredFields = getDeclaredFieldsIgnoringSyntheticAndStatic(clazz);
    // get fields declared in superClass
    Class<?> superClass = clazz.getSuperclass();
    while (!isInJavaLangPackage(superClass)) {
      declaredFields.addAll(getDeclaredFieldsIgnoringSyntheticAndStatic(superClass));
      superClass = superClass.getSuperclass();
    }
    return declaredFields;
  }

  /**
   * Returns names of non-static, non-synthetic fields.
   *
   * @param clazz the class to inspect
   * @return the field names
   */
  public static Set<String> getFieldsNames(Class<?> clazz) {
    return getDeclaredFieldsIncludingInherited(clazz).stream()
                                                     .map(Field::getName)
                                                     .collect(toSet());
  }

  private static Set<Field> getDeclaredFieldsIgnoringSyntheticAndStatic(Class<?> clazz) {
    Field[] declaredFields = clazz.getDeclaredFields();
    return stream(declaredFields).filter(field -> !(field.isSynthetic() || Modifier.isStatic(field.getModifiers())))
                                 .collect(toCollection(LinkedHashSet::new));
  }

  /**
   * Verifies that the object has the named field or property.
   *
   * @param <A> the actual object type
   * @param info assertion information
   * @param actual the actual object
   * @param name the field or property name
   */
  public <A> void assertHasFieldOrProperty(AssertionInfo info, A actual, String name) {
    assertNotNull(info, actual);
    try {
      extractPropertyOrField(actual, name);
    } catch (IntrospectionError error) {
      error.getterInvocationException()
           .ifPresent(this::throwAsRuntime);
      throw failures.failure(info, shouldHavePropertyOrField(actual, name));
    }
  }

  /**
   * Verifies the value of a named field or property.
   *
   * @param <A> the actual object type
   * @param info assertion information
   * @param actual the actual object
   * @param name the field or property name
   * @param expectedValue the expected value
   */
  public <A> void assertHasFieldOrPropertyWithValue(AssertionInfo info, A actual, String name, Object expectedValue) {
    assertHasFieldOrProperty(info, actual, name);
    Object value = extractPropertyOrField(actual, name);
    if (!deepEquals(value, expectedValue))
      throw failures.failure(info, shouldHavePropertyOrFieldWithValue(actual, name, expectedValue, value));
  }

  /**
   * Verifies that the object declares only the named fields.
   *
   * @param <A> the actual object type
   * @param info assertion information
   * @param actual the actual object
   * @param names the expected field names
   */
  public <A> void assertHasOnlyFields(AssertionInfo info, A actual, String... names) {
    assertNotNull(info, actual);
    checkArgument(names != null, "Given fields/properties are null");
    List<String> expectedFields = stream(names).sorted().toList();
    Field[] declaredFields = actual.getClass().getDeclaredFields();
    List<String> actualFields = stream(declaredFields).filter(field -> !isStatic(field.getModifiers()))
                                                      .filter(field -> !field.isSynthetic())
                                                      .map(Field::getName)
                                                      .sorted()
                                                      .toList();
    if (!expectedFields.equals(actualFields)) {
      List<String> fieldsNotFound = stream(names).filter(name -> !actualFields.contains(name)).collect(toList());
      List<String> extraFields = actualFields.stream()
                                             .filter(actualField -> !expectedFields.contains(actualField))
                                             .collect(toList());
      throw failures.failure(info, shouldContainOnly(actual, names, fieldsNotFound, extraFields, FIELDS_GROUP_DESCRIPTION));
    }
  }

  private <A> Object extractPropertyOrField(A actual, String name) {
    return PropertyOrFieldSupport.EXTRACTION.getValueOf(name, actual);
  }

  /**
   * Verifies that two objects have the same hash code.
   *
   * @param <A> the actual object type
   * @param info assertion information
   * @param actual the actual object
   * @param other the comparison object
   */
  public <A> void assertHasSameHashCodeAs(AssertionInfo info, A actual, Object other) {
    assertNotNull(info, actual);
    requireNonNull(other, "The object used to compare actual's hash code with should not be null");
    if (actual.hashCode() != other.hashCode()) throw failures.failure(info, shouldHaveSameHashCode(actual, other));
  }

  /**
   * Verifies that two objects have different hash codes.
   *
   * @param <A> the actual object type
   * @param info assertion information
   * @param actual the actual object
   * @param other the comparison object
   */
  public <A> void assertDoesNotHaveSameHashCodeAs(AssertionInfo info, A actual, Object other) {
    assertNotNull(info, actual);
    requireNonNull(other, "The object used to compare actual's hash code with should not be null");
    if (actual.hashCode() == other.hashCode())
      throw failures.failure(info, shouldNotHaveSameHashCode(actual, other));
  }

  private void throwAsRuntime(Throwable ex) {
    if (ex instanceof RuntimeException exception) {
      throw exception;
    }
    throw new RuntimeException(ex);
  }
}
