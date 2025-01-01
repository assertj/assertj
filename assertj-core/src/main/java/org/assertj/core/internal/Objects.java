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

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.deepEquals;
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
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
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
import static org.assertj.core.error.ShouldNotHaveSameHashCode.shouldNotHaveSameHashCode;
import static org.assertj.core.error.ShouldNotHaveToString.shouldNotHaveToString;
import static org.assertj.core.internal.CommonValidations.checkTypeIsNotNull;
import static org.assertj.core.internal.DeepDifference.determineDifferences;
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
import org.assertj.core.error.GroupTypeDescription;
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
  private static final GroupTypeDescription FIELDS_GROUP_DESCRIPTION = new GroupTypeDescription("non static/synthetic fields of",
                                                                                                "fields");

  @VisibleForTesting
  final PropertySupport propertySupport = PropertySupport.instance();
  private final ComparisonStrategy comparisonStrategy;
  @VisibleForTesting
  Failures failures = Failures.instance();
  private final FieldSupport fieldSupport = FieldSupport.comparison();

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

  public ComparisonStrategy getComparisonStrategy() {
    return comparisonStrategy;
  }

  public Failures getFailures() {
    return failures;
  }

  public void assertIsInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (!isInstanceOfClass(actual, type, info)) throw failures.failure(info, shouldBeInstance(actual, type));
  }

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

  public void assertIsNotInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (isInstanceOfClass(actual, type, info)) throw failures.failure(info, shouldNotBeInstance(actual, type));
  }

  private boolean isInstanceOfClass(Object actual, Class<?> clazz, AssertionInfo info) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(clazz);
    return clazz.isInstance(actual);
  }

  public void assertIsNotInstanceOfAny(AssertionInfo info, Object actual, Class<?>[] types) {
    if (!objectIsInstanceOfOneOfGivenClasses(actual, types, info)) return;
    throw failures.failure(info, shouldNotBeInstanceOfAny(actual, types));
  }

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

  public void assertDoesNotHaveSameClassAs(AssertionInfo info, Object actual, Object other) {
    if (haveSameClass(actual, other, info)) throw failures.failure(info, shouldNotHaveSameClass(actual, other));
  }

  public void assertIsExactlyInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (!actualIsExactlyInstanceOfType(actual, type, info))
      throw failures.failure(info, shouldBeExactlyInstance(actual, type));
  }

  private boolean actualIsExactlyInstanceOfType(Object actual, Class<?> expectedType, AssertionInfo info) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(expectedType);
    return expectedType.equals(actual.getClass());
  }

  public void assertIsNotExactlyInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (actualIsExactlyInstanceOfType(actual, type, info))
      throw failures.failure(info, shouldNotBeExactlyInstance(actual, type));
  }

  public void assertIsOfAnyClassIn(AssertionInfo info, Object actual, Class<?>[] types) {
    boolean itemInArray = isOfOneOfGivenTypes(actual, types, info);
    if (!itemInArray) throw failures.failure(info, shouldBeOfClassIn(actual, types));
  }

  private boolean isOfOneOfGivenTypes(Object actual, Class<?>[] types, AssertionInfo info) {
    assertNotNull(info, actual);
    requireNonNull(types, "The given types should not be null");
    return isItemInArray(actual.getClass(), types);
  }

  public void assertIsNotOfAnyClassIn(AssertionInfo info, Object actual, Class<?>[] types) {
    boolean itemInArray = isOfOneOfGivenTypes(actual, types, info);
    if (itemInArray) throw failures.failure(info, shouldNotBeOfClassIn(actual, types));
  }

  private void checkIsNotNullAndIsNotEmpty(Class<?>[] types) {
    requireNonNull(types, "The given array of types should not be null");
    checkArgument(types.length > 0, "The given array of types should not be empty");
  }

  public void assertEqual(AssertionInfo info, Object actual, Object expected) {
    if (!areEqual(actual, expected))
      throw failures.failure(info, shouldBeEqual(actual, expected, comparisonStrategy, info.representation()));
  }

  public void assertNotEqual(AssertionInfo info, Object actual, Object other) {
    if (areEqual(actual, other)) throw failures.failure(info, shouldNotBeEqual(actual, other, comparisonStrategy));
  }

  private boolean areEqual(Object actual, Object other) {
    return comparisonStrategy.areEqual(actual, other);
  }

  public void assertNull(AssertionInfo info, Object actual) {
    if (actual != null)
      throw failures.failure(info, shouldBeEqual(actual, null, comparisonStrategy, info.representation()));
  }

  public void assertNotNull(AssertionInfo info, Object actual) {
    if (actual == null) throw failures.failure(info, shouldNotBeNull());
  }

  public void assertNotNull(AssertionInfo info, Object actual, String label) {
    if (actual == null) throw failures.failure(info, shouldNotBeNull(label));
  }

  public void assertSame(AssertionInfo info, Object actual, Object expected) {
    if (actual != expected) throw failures.failure(info, shouldBeSame(actual, expected));
  }

  public void assertNotSame(AssertionInfo info, Object actual, Object other) {
    if (actual == other) throw failures.failure(info, shouldNotBeSame(actual));
  }

  public void assertHasToString(AssertionInfo info, Object actual, String expectedToString) {
    assertNotNull(info, actual);
    String actualString = actual.toString();
    if (!actualString.equals(expectedToString))
      throw failures.failure(info, shouldHaveToString(actualString, expectedToString), actualString, expectedToString);
  }

  public void assertDoesNotHaveToString(AssertionInfo info, Object actual, String otherToString) {
    assertNotNull(info, actual);
    String actualToString = actual.toString();
    if (actualToString.equals(otherToString))
      throw failures.failure(info, shouldNotHaveToString(otherToString));
  }

  public void assertIsIn(AssertionInfo info, Object actual, Object[] values) {
    checkArrayIsNotNull(values);
    assertIsIn(info, actual, asList(values));
  }

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

  public void assertIsIn(AssertionInfo info, Object actual, Iterable<?> values) {
    checkNotNullIterable(values);
    if (!isActualIn(actual, values)) throw failures.failure(info, shouldBeIn(actual, values, comparisonStrategy));
  }

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
    Comparator typeComparator = comparatorByType.getComparatorForType(fieldType);
    if (typeComparator != null) return typeComparator.compare(actualFieldValue, otherFieldValue) == 0;
    // default comparison using equals
    return deepEquals(actualFieldValue, otherFieldValue);
  }

  private <A> boolean canReadFieldValue(Field field, A actual) {
    return fieldSupport.isAllowedToRead(field) || propertySupport.publicGetterExistsFor(field.getName(), actual);
  }

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

  private <A> Object getPropertyOrFieldValue(A a, String fieldName) {
    return PropertyOrFieldSupport.COMPARISON.getValueOf(fieldName, a);
  }

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
      error.getterInvocationException()
           .ifPresent(this::throwAsRuntime);
      throw failures.failure(info, shouldHavePropertyOrField(actual, name));
    }
  }

  public <A> void assertHasFieldOrPropertyWithValue(AssertionInfo info, A actual, String name, Object expectedValue) {
    assertHasFieldOrProperty(info, actual, name);
    Object value = extractPropertyOrField(actual, name);
    if (!deepEquals(value, expectedValue))
      throw failures.failure(info, shouldHavePropertyOrFieldWithValue(actual, name, expectedValue, value));
  }

  public <A> void assertHasOnlyFields(AssertionInfo info, A actual, String... names) {
    assertNotNull(info, actual);
    checkArgument(names != null, "Given fields/properties are null");
    List<String> expectedFields = stream(names).sorted().collect(toList());
    Field[] declaredFields = actual.getClass().getDeclaredFields();
    List<String> actualFields = stream(declaredFields).filter(field -> !isStatic(field.getModifiers()))
                                                      .filter(field -> !field.isSynthetic())
                                                      .map(Field::getName)
                                                      .sorted()
                                                      .collect(toList());
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

  public <A> void assertHasSameHashCodeAs(AssertionInfo info, A actual, Object other) {
    assertNotNull(info, actual);
    requireNonNull(other, "The object used to compare actual's hash code with should not be null");
    if (actual.hashCode() != other.hashCode()) throw failures.failure(info, shouldHaveSameHashCode(actual, other));
  }

  public <A> void assertDoesNotHaveSameHashCodeAs(AssertionInfo info, A actual, Object other) {
    assertNotNull(info, actual);
    requireNonNull(other, "The object used to compare actual's hash code with should not be null");
    if (actual.hashCode() == other.hashCode())
      throw failures.failure(info, shouldNotHaveSameHashCode(actual, other));
  }

  private static class ByFieldsComparison {

    private final List<String> fieldsNames;
    private final List<Object> expectedValues;
    private final List<Object> rejectedValues;

    private ByFieldsComparison(final List<String> fieldsNames,
                               final List<Object> expectedValues,
                               final List<Object> rejectedValues) {
      this.fieldsNames = fieldsNames;
      this.expectedValues = expectedValues;
      this.rejectedValues = rejectedValues;
    }

    private boolean isFieldsNamesEmpty() {
      return fieldsNames.isEmpty();
    }

    private boolean isFieldsNamesNotEmpty() {
      return !isFieldsNamesEmpty();
    }

  }

  private void throwAsRuntime(Throwable ex) {
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    throw new RuntimeException(ex);
  }
}
