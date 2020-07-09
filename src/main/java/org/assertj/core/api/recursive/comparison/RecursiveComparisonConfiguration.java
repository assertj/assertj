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
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Strings.join;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.VisibleForTesting;

public class RecursiveComparisonConfiguration {

  public static final String INDENT_LEVEL_2 = "  -";
  private boolean strictTypeChecking = false;

  // fields to ignore section
  private boolean ignoreAllActualNullFields = false;
  private boolean ignoreAllActualEmptyOptionalFields = false;
  private boolean ignoreAllExpectedNullFields = false;
  private Set<FieldLocation> ignoredFields = new LinkedHashSet<>();
  private List<Pattern> ignoredFieldsRegexes = new ArrayList<>();
  private Set<Class<?>> ignoredTypes = new LinkedHashSet<>();

  // overridden equals method to ignore section
  private List<Class<?>> ignoredOverriddenEqualsForTypes = new ArrayList<>();
  private List<FieldLocation> ignoredOverriddenEqualsForFields = new ArrayList<>();
  private List<Pattern> ignoredOverriddenEqualsForFieldsMatchingRegexes = new ArrayList<>();
  private boolean ignoreAllOverriddenEquals = true;

  // ignore order in collections section
  private boolean ignoreCollectionOrder = false;
  private Set<FieldLocation> ignoredCollectionOrderInFields = new LinkedHashSet<>();
  private List<Pattern> ignoredCollectionOrderInFieldsMatchingRegexes = new ArrayList<>();

  // registered comparators section
  private TypeComparators typeComparators = defaultTypeComparators();
  private FieldComparators fieldComparators = new FieldComparators();

  private RecursiveComparisonConfiguration(Builder builder) {
    this.strictTypeChecking = builder.strictTypeChecking;
    this.ignoreAllActualNullFields = builder.ignoreAllActualNullFields;
    this.ignoreAllActualEmptyOptionalFields = builder.ignoreAllActualEmptyOptionalFields;
    this.ignoreAllExpectedNullFields = builder.ignoreAllExpectedNullFields;
    this.ignoredFields = new LinkedHashSet<>(FieldLocation.from(builder.ignoredFields));
    ignoreFieldsMatchingRegexes(builder.ignoredFieldsMatchingRegexes);
    ignoreFieldsOfTypes(builder.ignoredTypes);
    ignoreOverriddenEqualsForTypes(builder.ignoredOverriddenEqualsForTypes);
    this.ignoredOverriddenEqualsForFields = new ArrayList<>(FieldLocation.from(builder.ignoredOverriddenEqualsForFields));
    ignoreOverriddenEqualsForFieldsMatchingRegexes(builder.ignoredOverriddenEqualsForFieldsMatchingRegexes);
    this.ignoreAllOverriddenEquals = builder.ignoreAllOverriddenEquals;
    this.ignoreCollectionOrder = builder.ignoreCollectionOrder;
    this.ignoredCollectionOrderInFields = new LinkedHashSet<>(FieldLocation.from(builder.ignoredCollectionOrderInFields));
    ignoreCollectionOrderInFieldsMatchingRegexes(builder.ignoredCollectionOrderInFieldsMatchingRegexes);
    this.typeComparators = builder.typeComparators;
    this.fieldComparators = builder.fieldComparators;
  }

  public RecursiveComparisonConfiguration() {}

  public boolean hasComparatorForField(String fieldName) {
    return fieldComparators.hasComparatorForField(new FieldLocation(fieldName));
  }

  public Comparator<?> getComparatorForField(String fieldName) {
    return fieldComparators.getComparatorForField(new FieldLocation(fieldName));
  }

  public FieldComparators getFieldComparators() {
    return fieldComparators;
  }

  public boolean hasComparatorForType(Class<?> keyType) {
    return typeComparators.hasComparatorForType(keyType);
  }

  public boolean hasCustomComparators() {
    return !typeComparators.isEmpty() || !fieldComparators.isEmpty();
  }

  public Comparator<?> getComparatorForType(Class<?> fieldType) {
    return typeComparators.get(fieldType);
  }

  public TypeComparators getTypeComparators() {
    return typeComparators;
  }

  Stream<Entry<Class<?>, Comparator<?>>> comparatorByTypes() {
    return typeComparators.comparatorByTypes();
  }

  @VisibleForTesting
  boolean getIgnoreAllActualNullFields() {
    return ignoreAllActualNullFields;
  }

  @VisibleForTesting
  boolean getIgnoreAllExpectedNullFields() {
    return ignoreAllExpectedNullFields;
  }

  @VisibleForTesting
  boolean getIgnoreAllOverriddenEquals() {
    return ignoreAllOverriddenEquals;
  }

  /**
   * Sets whether actual empty optional fields are ignored in the recursive comparison.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringActualNullFields()} for code examples.
   *
   * @param ignoringAllActualEmptyOptionalFields whether to ignore actual empty optional fields in the recursive comparison
   */
  public void setIgnoreAllActualEmptyOptionalFields(boolean ignoringAllActualEmptyOptionalFields) {
    this.ignoreAllActualEmptyOptionalFields = ignoringAllActualEmptyOptionalFields;
  }

  @VisibleForTesting
  boolean getIgnoreAllActualEmptyOptionalFields() {
    return ignoreAllActualEmptyOptionalFields;
  }

  /**
   * Sets whether actual null fields are ignored in the recursive comparison.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringActualNullFields()} for code examples.
   *
   * @param ignoreAllActualNullFields whether to ignore actual null fields in the recursive comparison
   */
  public void setIgnoreAllActualNullFields(boolean ignoreAllActualNullFields) {
    this.ignoreAllActualNullFields = ignoreAllActualNullFields;
  }

  /**
   * Sets whether expected null fields are ignored in the recursive comparison.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringExpectedNullFields()} for code examples.
   *
   * @param ignoreAllExpectedNullFields whether to ignore expected null fields in the recursive comparison
   */
  public void setIgnoreAllExpectedNullFields(boolean ignoreAllExpectedNullFields) {
    this.ignoreAllExpectedNullFields = ignoreAllExpectedNullFields;
  }

  /**
   * Adds the given fields to the list of the object under test fields to ignore in the recursive comparison.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringFields(String...) RecursiveComparisonAssert#ignoringFields(String...)} for examples.
   *
   * @param fieldsToIgnore the fields of the object under test to ignore in the comparison.
   */
  public void ignoreFields(String... fieldsToIgnore) {
    List<FieldLocation> fieldLocations = FieldLocation.from(fieldsToIgnore);
    ignoredFields.addAll(fieldLocations);
  }

  /**
   * Allows to ignore in the recursive comparison the object under test fields matching the given regexes. The given regexes are added to the already registered ones.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringFieldsMatchingRegexes(String...) RecursiveComparisonAssert#ignoringFieldsMatchingRegexes(String...)} for examples.
   *
   * @param regexes regexes used to ignore fields in the comparison.
   */
  public void ignoreFieldsMatchingRegexes(String... regexes) {
    ignoredFieldsRegexes.addAll(Stream.of(regexes)
                                      .map(Pattern::compile)
                                      .collect(toList()));
  }

  /**
   * Adds the given types to the list of the object under test fields types to ignore in the recursive comparison.
   * The fields are ignored if their types exactly match one of the ignored types, if a field is a subtype of an ignored type it won't be ignored.
   * <p>
   * Note that if some object under test fields are null, they are not ignored by this method as their type can't be evaluated.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringFields(String...) RecursiveComparisonAssert#ignoringFieldsOfTypes(Class...)} for examples.
   *
   * @param types the types of the object under test to ignore in the comparison.
   */
  public void ignoreFieldsOfTypes(Class<?>... types) {
    stream(types).map(type -> asWrapperIfPrimitiveType(type)).forEach(ignoredTypes::add);
  }

  private static Class<?> asWrapperIfPrimitiveType(Class<?> type) {
    if (!type.isPrimitive()) return type;
    if (type.equals(boolean.class)) return Boolean.class;
    if (type.equals(byte.class)) return Byte.class;
    if (type.equals(int.class)) return Integer.class;
    if (type.equals(short.class)) return Short.class;
    if (type.equals(char.class)) return Character.class;
    if (type.equals(float.class)) return Float.class;
    if (type.equals(double.class)) return Double.class;
    // should not arrive here since we have tested primitive types first
    return type;
  }

  /**
   * Returns the list of the object under test fields to ignore in the recursive comparison.
   *
   * @return the list of the object under test fields to ignore in the recursive comparison.
   */
  public Set<FieldLocation> getIgnoredFields() {
    return ignoredFields;
  }

  /**
   * Returns the set of the object under test fields types to ignore in the recursive comparison.
   *
   * @return the set of the object under test fields types to ignore in the recursive comparison.
   */
  public Set<Class<?>> getIgnoredTypes() {
    return ignoredTypes;
  }

  /**
   * Force a recursive comparison on all fields (except java types).
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringAllOverriddenEquals()} for examples.
   */
  public void ignoreAllOverriddenEquals() {
    ignoreAllOverriddenEquals = true;
  }

  /**
   * Force a recursive comparison on all fields (except java types).
   * <p>
   * See {@link RecursiveComparisonAssert#usingOverriddenEquals()} for examples.
   */
  public void useOverriddenEquals() {
    ignoreAllOverriddenEquals = false;
  }

  /**
   * Adds the given fields to the list of fields to force a recursive comparison on.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringOverriddenEqualsForFields(String...) RecursiveComparisonAssert#ignoringOverriddenEqualsForFields(String...)} for examples.
   *
   * @param fields the fields to force a recursive comparison on.
   */
  public void ignoreOverriddenEqualsForFields(String... fields) {
    List<FieldLocation> fieldLocations = FieldLocation.from(fields);
    ignoredOverriddenEqualsForFields.addAll(fieldLocations);
  }

  /**
   * Adds the given regexes to the list of regexes used find the fields to force a recursive comparison on.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringOverriddenEqualsForFieldsMatchingRegexes(String...) RecursiveComparisonAssert#ignoringOverriddenEqualsForFieldsMatchingRegexes(String...)} for examples.
   *
   * @param regexes regexes used to specify the fields we want to force a recursive comparison on.
   */
  public void ignoreOverriddenEqualsForFieldsMatchingRegexes(String... regexes) {
    ignoredOverriddenEqualsForFieldsMatchingRegexes.addAll(Stream.of(regexes)
                                                                 .map(Pattern::compile)
                                                                 .collect(toList()));
  }

  /**
   * Adds the given types to the list of types to force a recursive comparison on.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringOverriddenEqualsForTypes(Class...) RecursiveComparisonAssert#ignoringOverriddenEqualsForTypes(Class...)} for examples.
   *
   * @param types the types to the list of types to force a recursive comparison on.
   */
  public void ignoreOverriddenEqualsForTypes(Class<?>... types) {
    ignoredOverriddenEqualsForTypes.addAll(list(types));
  }

  @VisibleForTesting
  boolean getIgnoreCollectionOrder() {
    return ignoreCollectionOrder;
  }

  /**
   * Sets whether to ignore collection order in the comparison.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringCollectionOrder()} for code examples.
   *
   * @param ignoreCollectionOrder whether to ignore collection order in the comparison.
   */
  public void ignoreCollectionOrder(boolean ignoreCollectionOrder) {
    this.ignoreCollectionOrder = ignoreCollectionOrder;
  }

  /**
   * Adds the given fields to the list of the object under test fields to ignore collection order in the recursive comparison.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...) RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...)} for examples.
   *
   * @param fieldsToIgnoreCollectionOrder the fields of the object under test to ignore collection order in the comparison.
   */
  public void ignoreCollectionOrderInFields(String... fieldsToIgnoreCollectionOrder) {
    List<FieldLocation> fieldLocations = FieldLocation.from(fieldsToIgnoreCollectionOrder);
    ignoredCollectionOrderInFields.addAll(fieldLocations);
  }

  /**
   * Returns the list of the object under test fields to ignore collection order in the recursive comparison.
   *
   * @return the list of the object under test fields to ignore collection order in the recursive comparison.
   */
  public Set<FieldLocation> getIgnoredCollectionOrderInFields() {
    return ignoredCollectionOrderInFields;
  }

  /**
   * Adds the given regexes to the list of regexes used to find the object under test fields to ignore collection order in the recursive comparison.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...) RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...)} for examples.
   *
   * @param regexes regexes used to find the object under test fields to ignore collection order in in the comparison.
   */
  public void ignoreCollectionOrderInFieldsMatchingRegexes(String... regexes) {
    ignoredCollectionOrderInFieldsMatchingRegexes.addAll(Stream.of(regexes)
                                                               .map(Pattern::compile)
                                                               .collect(toList()));
  }

  /**
   * Returns the list of regexes used to find the object under test fields to ignore collection order in the recursive comparison.
   *
   * @return the list of regexes used to find the object under test fields to ignore collection order in the recursive comparison.
   */
  public List<Pattern> getIgnoredCollectionOrderInFieldsMatchingRegexes() {
    return ignoredCollectionOrderInFieldsMatchingRegexes;
  }

  /**
   * Registers the given {@link Comparator} to compare the fields with the given type.
   * <p>
   * Comparators specified by this method have less precedence than comparators added with {@link #registerComparatorForFields(Comparator, String...)}.
   * <p>
   * See {@link RecursiveComparisonAssert#withComparatorForType(Comparator, Class)} for examples.
   *
   * @param <T> the class type to register a comparator for
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given field
   * @param type the type to be compared with the given comparator.
   */
  public <T> void registerComparatorForType(Comparator<? super T> comparator, Class<T> type) {
    typeComparators.put(type, comparator);
  }

  /**
   * @deprecated Since 3.17.0 use {@link #registerComparatorForFields(Comparator, String...)} instead.
   * @param comparator comparator to use
   * @param fieldLocation the field location
   */
  @Deprecated
  public void registerComparatorForField(Comparator<?> comparator, FieldLocation fieldLocation) {
    fieldComparators.registerComparator(fieldLocation, comparator);
  }

  /**
   * Registers the given {@link Comparator} to compare the fields with the given locations.
   * <p>
   * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
   * one can register to a comparator for Foo and Bar's {@code id} by calling:
   * <pre><code class='java'> registerComparatorForFields(idComparator, "foo.id", "bar.id")</code></pre>
   * <p>
   * Comparators specified by this method have precedence over comparators added with {@link #registerComparatorForType(Comparator, Class)}.
   * <p>
   * See {@link RecursiveComparisonAssert#withComparatorForFields(Comparator, String...) RecursiveComparisonAssert#withComparatorForFields(Comparator, String...)} for examples.
   *
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given field
   * @param fieldLocations the locations from the root object of the fields the comparator should be used for
   */
  public void registerComparatorForFields(Comparator<?> comparator, String... fieldLocations) {
    Stream.of(fieldLocations)
          .map(FieldLocation::new)
          .forEach(fieldLocation -> fieldComparators.registerComparator(fieldLocation, comparator));
  }

  /**
   * Sets whether the recursive comparison will check that actual's type is compatible with expected's type (the same applies for each field).
   * Compatible means that the expected's type is the same or a subclass of actual's type.
   * <p>
   * See {@link RecursiveComparisonAssert#withStrictTypeChecking()} for code examples.
   *
   * @param strictTypeChecking whether the recursive comparison will check that actual's type is compatible with expected's type.
   */
  public void strictTypeChecking(boolean strictTypeChecking) {
    this.strictTypeChecking = strictTypeChecking;
  }

  public boolean isInStrictTypeCheckingMode() {
    return strictTypeChecking;
  }

  public List<Pattern> getIgnoredFieldsRegexes() {
    return ignoredFieldsRegexes;
  }

  public List<Class<?>> getIgnoredOverriddenEqualsForTypes() {
    return ignoredOverriddenEqualsForTypes;
  }

  public List<FieldLocation> getIgnoredOverriddenEqualsForFields() {
    return ignoredOverriddenEqualsForFields;
  }

  public List<Pattern> getIgnoredOverriddenEqualsForFieldsMatchingRegexes() {
    return ignoredOverriddenEqualsForFieldsMatchingRegexes;
  }

  public Stream<Entry<FieldLocation, Comparator<?>>> comparatorByFields() {
    return fieldComparators.comparatorByFields();
  }

  @Override
  public String toString() {
    return multiLineDescription(CONFIGURATION_PROVIDER.representation());
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(fieldComparators, ignoreAllActualEmptyOptionalFields, ignoreAllActualNullFields,
                                  ignoreAllExpectedNullFields, ignoreAllOverriddenEquals, ignoreCollectionOrder,
                                  ignoredCollectionOrderInFields, ignoredCollectionOrderInFieldsMatchingRegexes, ignoredFields,
                                  ignoredFieldsRegexes, ignoredOverriddenEqualsForFields, ignoredOverriddenEqualsForTypes,
                                  ignoredOverriddenEqualsForFieldsMatchingRegexes, ignoredTypes, strictTypeChecking,
                                  typeComparators);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    RecursiveComparisonConfiguration other = (RecursiveComparisonConfiguration) obj;
    return java.util.Objects.equals(fieldComparators, other.fieldComparators)
           && ignoreAllActualEmptyOptionalFields == other.ignoreAllActualEmptyOptionalFields
           && ignoreAllActualNullFields == other.ignoreAllActualNullFields
           && ignoreAllExpectedNullFields == other.ignoreAllExpectedNullFields
           && ignoreAllOverriddenEquals == other.ignoreAllOverriddenEquals
           && ignoreCollectionOrder == other.ignoreCollectionOrder
           && java.util.Objects.equals(ignoredCollectionOrderInFields, other.ignoredCollectionOrderInFields)
           && java.util.Objects.equals(ignoredFields, other.ignoredFields)
           && java.util.Objects.equals(ignoredFieldsRegexes, other.ignoredFieldsRegexes)
           && java.util.Objects.equals(ignoredOverriddenEqualsForFields, other.ignoredOverriddenEqualsForFields)
           && java.util.Objects.equals(ignoredOverriddenEqualsForTypes, other.ignoredOverriddenEqualsForTypes)
           && java.util.Objects.equals(ignoredOverriddenEqualsForFieldsMatchingRegexes,
                                       other.ignoredOverriddenEqualsForFieldsMatchingRegexes)
           && java.util.Objects.equals(ignoredTypes, other.ignoredTypes) && strictTypeChecking == other.strictTypeChecking
           && java.util.Objects.equals(typeComparators, other.typeComparators)
           && java.util.Objects.equals(ignoredCollectionOrderInFieldsMatchingRegexes,
                                       other.ignoredCollectionOrderInFieldsMatchingRegexes);
  }

  public String multiLineDescription(Representation representation) {
    StringBuilder description = new StringBuilder();
    describeIgnoreAllActualNullFields(description);
    describeIgnoreAllActualEmptyOptionalFields(description);
    describeIgnoreAllExpectedNullFields(description);
    describeIgnoredFields(description);
    describeIgnoredFieldsRegexes(description);
    describeIgnoredFieldsForTypes(description);
    describeOverriddenEqualsMethodsUsage(description, representation);
    describeIgnoreCollectionOrder(description);
    describeIgnoredCollectionOrderInFields(description);
    describeIgnoredCollectionOrderInFieldsMatchingRegexes(description);
    describeRegisteredComparatorByTypes(description);
    describeRegisteredComparatorForFields(description);
    describeTypeCheckingStrictness(description);
    return description.toString();
  }

  boolean shouldIgnore(DualValue dualValue) {
    String concatenatedPath = dualValue.concatenatedPath;
    return matchesAnIgnoredField(concatenatedPath)
           || matchesAnIgnoredFieldRegex(concatenatedPath)
           || shouldIgnoreFieldButWithoutNeedingEvaluatingFieldName(dualValue);
  }

  Set<String> getNonIgnoredActualFieldNames(DualValue dualValue) {
    Set<String> actualFieldsNames = Objects.getFieldsNames(dualValue.actual.getClass());
    // we are doing the same as shouldIgnore(DualValue dualValue) but in two steps for performance reasons:
    // - we filter first ignored field by names that don't need building DualValues
    // - then we filter field DualValues with the remaining criteria (shouldIgnoreNotEvalutingFieldName)
    // DualValues are built introspecting fields which is expensive.
    return actualFieldsNames.stream()
                            // evaluate field name ignoring criteria
                            .filter(fieldName -> !shouldIgnore(dualValue.path, fieldName))
                            .map(fieldName -> dualValueForField(dualValue, fieldName))
                            // evaluate field value ignoring criteria
                            .filter(fieldDualValue -> !shouldIgnoreFieldButWithoutNeedingEvaluatingFieldName(fieldDualValue))
                            // back to field name
                            .map(DualValue::getFieldName)
                            .filter(fieldName -> !fieldName.isEmpty())
                            .collect(toSet());
  }

  // non public stuff

  private boolean shouldIgnoreFieldButWithoutNeedingEvaluatingFieldName(DualValue dualValue) {
    return matchesAnIgnoredNullField(dualValue)
           || matchesAnIgnoredFieldType(dualValue)
           || matchesAnIgnoredEmptyOptionalField(dualValue);
  }

  private boolean shouldIgnore(List<String> parentConcatenatedPath, String fieldName) {
    List<String> fieldConcatenatedPathList = newArrayList(parentConcatenatedPath);
    fieldConcatenatedPathList.add(fieldName);
    String fieldConcatenatedPath = join(fieldConcatenatedPathList).with(".");
    return matchesAnIgnoredField(fieldConcatenatedPath) || matchesAnIgnoredFieldRegex(fieldConcatenatedPath);
  }

  private static DualValue dualValueForField(DualValue parentDualValue, String fieldName) {
    List<String> path = newArrayList(parentDualValue.path);
    path.add(fieldName);
    Object actualFieldValue = COMPARISON.getSimpleValue(fieldName, parentDualValue.actual);
    // no guarantees we have a field in expected named as fieldName
    Object expectedFieldValue;
    try {
      expectedFieldValue = COMPARISON.getSimpleValue(fieldName, parentDualValue.expected);
    } catch (@SuppressWarnings("unused") Exception e) {
      // set the field to null to express it is absent, this not 100% accurate as the value could be null
      // but it works to evaluate if dualValue should be ignored with matchesAnIgnoredFieldType
      expectedFieldValue = null;
    }
    return new DualValue(path, actualFieldValue, expectedFieldValue);
  }

  boolean hasCustomComparator(DualValue dualValue) {
    String fieldName = dualValue.getConcatenatedPath();
    if (hasComparatorForField(fieldName)) return true;
    if (dualValue.actual == null && dualValue.expected == null) return false;
    // best effort assuming actual and expected have the same type (not 100% true as we can compare object of differennt types)
    Class<?> valueType = dualValue.actual != null ? dualValue.actual.getClass() : dualValue.expected.getClass();
    return hasComparatorForType(valueType);
  }

  boolean shouldIgnoreOverriddenEqualsOf(DualValue dualValue) {
    // we must compare java basic types otherwise the recursive comparison loops infinitely!
    if (dualValue.isActualJavaType()) return false;
    // enums don't have fields, comparing them field by field has no sense, we need to use equals which is overridden and final
    if (dualValue.isActualAnEnum()) return false;
    return ignoreAllOverriddenEquals
           || matchesAnIgnoredOverriddenEqualsField(dualValue)
           || (dualValue.actual != null && shouldIgnoreOverriddenEqualsOf(dualValue.actual.getClass()));
  }

  @VisibleForTesting
  boolean shouldIgnoreOverriddenEqualsOf(Class<? extends Object> clazz) {
    return matchesAnIgnoredOverriddenEqualsRegex(clazz) || matchesAnIgnoredOverriddenEqualsType(clazz);
  }

  boolean shouldIgnoreCollectionOrder(DualValue dualValue) {
    return ignoreCollectionOrder
           || matchesAnIgnoredCollectionOrderInField(dualValue)
           || matchesAnIgnoredCollectionOrderInFieldRegex(dualValue);
  }

  private void describeIgnoredFieldsRegexes(StringBuilder description) {
    if (!ignoredFieldsRegexes.isEmpty())
      description.append(format("- the fields matching the following regexes were ignored in the comparison: %s%n",
                                describeRegexes(ignoredFieldsRegexes)));
  }

  private void describeIgnoredFields(StringBuilder description) {
    if (!ignoredFields.isEmpty())
      description.append(format("- the following fields were ignored in the comparison: %s%n", describeIgnoredFields()));
  }

  private void describeIgnoredFieldsForTypes(StringBuilder description) {
    if (!ignoredTypes.isEmpty())
      description.append(format("- the following types were ignored in the comparison: %s%n", describeIgnoredTypes()));
  }

  private void describeIgnoreAllActualNullFields(StringBuilder description) {
    if (ignoreAllActualNullFields) description.append(format("- all actual null fields were ignored in the comparison%n"));
  }

  private void describeIgnoreAllActualEmptyOptionalFields(StringBuilder description) {
    if (getIgnoreAllActualEmptyOptionalFields())
      description.append(format("- all actual empty optional fields were ignored in the comparison (including Optional, OptionalInt, OptionalLong and OptionalDouble)%n"));
  }

  private void describeIgnoreAllExpectedNullFields(StringBuilder description) {
    if (ignoreAllExpectedNullFields) description.append(format("- all expected null fields were ignored in the comparison%n"));
  }

  private void describeOverriddenEqualsMethodsUsage(StringBuilder description, Representation representation) {
    String header = ignoreAllOverriddenEquals
        ? "- no overridden equals methods were used in the comparison (except for java types)"
        : "- overridden equals methods were used in the comparison";
    description.append(header);
    if (isConfiguredToIgnoreSomeButNotAllOverriddenEqualsMethods()) {
      description.append(format(" except for:%n"));
      describeIgnoredOverriddenEqualsMethods(description, representation);
    } else {
      description.append(format("%n"));
    }
  }

  private void describeIgnoredOverriddenEqualsMethods(StringBuilder description, Representation representation) {
    if (!ignoredOverriddenEqualsForFields.isEmpty())
      description.append(format("%s the following fields: %s%n", INDENT_LEVEL_2,
                                describeIgnoredOverriddenEqualsForFields()));
    if (!ignoredOverriddenEqualsForTypes.isEmpty())
      description.append(format("%s the following types: %s%n", INDENT_LEVEL_2,
                                describeIgnoredOverriddenEqualsForTypes(representation)));
    if (!ignoredOverriddenEqualsForFieldsMatchingRegexes.isEmpty())
      description.append(format("%s the types matching the following regexes: %s%n", INDENT_LEVEL_2,
                                describeRegexes(ignoredOverriddenEqualsForFieldsMatchingRegexes)));
  }

  private String describeIgnoredOverriddenEqualsForTypes(Representation representation) {
    List<String> fieldsDescription = ignoredOverriddenEqualsForTypes.stream()
                                                                    .map(representation::toStringOf)
                                                                    .collect(toList());
    return join(fieldsDescription).with(", ");
  }

  private String describeIgnoredOverriddenEqualsForFields() {
    List<String> fieldsDescription = ignoredOverriddenEqualsForFields.stream()
                                                                     .map(FieldLocation::getFieldPath)
                                                                     .collect(toList());
    return join(fieldsDescription).with(", ");
  }

  private void describeIgnoreCollectionOrder(StringBuilder description) {
    if (ignoreCollectionOrder) description.append(format("- collection order was ignored in all fields in the comparison%n"));
  }

  private void describeIgnoredCollectionOrderInFields(StringBuilder description) {
    if (!ignoredCollectionOrderInFields.isEmpty())
      description.append(format("- collection order was ignored in the following fields in the comparison: %s%n",
                                describeIgnoredCollectionOrderInFields()));
  }

  private void describeIgnoredCollectionOrderInFieldsMatchingRegexes(StringBuilder description) {
    if (!ignoredCollectionOrderInFieldsMatchingRegexes.isEmpty())
      description.append(format("- collection order was ignored in the fields matching the following regexes in the comparison: %s%n",
                                describeRegexes(ignoredCollectionOrderInFieldsMatchingRegexes)));
  }

  private boolean matchesAnIgnoredOverriddenEqualsRegex(Class<?> clazz) {
    if (ignoredOverriddenEqualsForFieldsMatchingRegexes.isEmpty()) return false; // shortcut
    String canonicalName = clazz.getCanonicalName();
    return ignoredOverriddenEqualsForFieldsMatchingRegexes.stream()
                                                          .anyMatch(regex -> regex.matcher(canonicalName).matches());
  }

  private boolean matchesAnIgnoredOverriddenEqualsType(Class<?> clazz) {
    return ignoredOverriddenEqualsForTypes.contains(clazz);
  }

  private boolean matchesAnIgnoredOverriddenEqualsField(DualValue dualValue) {
    return ignoredOverriddenEqualsForFields.stream()
                                           .anyMatch(fieldLocation -> fieldLocation.matches(dualValue.concatenatedPath));
  }

  private boolean matchesAnIgnoredNullField(DualValue dualValue) {
    return (ignoreAllActualNullFields && dualValue.actual == null)
           || (ignoreAllExpectedNullFields && dualValue.expected == null);
  }

  private boolean matchesAnIgnoredEmptyOptionalField(DualValue dualValue) {
    return ignoreAllActualEmptyOptionalFields
           && dualValue.isActualFieldAnEmptyOptionalOfAnyType();
  }

  private boolean matchesAnIgnoredFieldRegex(String fieldConcatenatedPath) {
    return ignoredFieldsRegexes.stream()
                               .anyMatch(regex -> regex.matcher(fieldConcatenatedPath).matches());
  }

  private boolean matchesAnIgnoredFieldType(DualValue dualValue) {
    Object actual = dualValue.actual;
    if (actual != null) return ignoredTypes.contains(actual.getClass());
    Object expected = dualValue.expected;
    // actual is null => we can't evaluate its type, we can only reliably check dualValue.expected's type if
    // strictTypeChecking is enabled which guarantees expected is of the same type.
    if (strictTypeChecking && expected != null) return ignoredTypes.contains(expected.getClass());
    // if strictTypeChecking is disabled, we can't safely ignore the field (if we did, we would ignore all null fields!).
    return false;
  }

  private boolean matchesAnIgnoredField(String fieldConcatenatedPath) {
    return ignoredFields.stream()
                        .anyMatch(fieldLocation -> fieldLocation.matches(fieldConcatenatedPath));
  }

  private boolean matchesAnIgnoredCollectionOrderInField(DualValue dualValue) {
    return ignoredCollectionOrderInFields.stream()
                                         .anyMatch(fieldLocation -> fieldLocation.matches(dualValue.concatenatedPath));
  }

  private boolean matchesAnIgnoredCollectionOrderInFieldRegex(DualValue dualValue) {
    return ignoredCollectionOrderInFieldsMatchingRegexes.stream()
                                                        .anyMatch(regex -> regex.matcher(dualValue.concatenatedPath).matches());
  }

  private String describeIgnoredFields() {
    List<String> fieldsDescription = ignoredFields.stream()
                                                  .map(FieldLocation::getFieldPath)
                                                  .collect(toList());
    return join(fieldsDescription).with(", ");
  }

  private String describeIgnoredTypes() {
    List<String> typesDescription = ignoredTypes.stream()
                                                .map(Class::getName)
                                                .collect(toList());
    return join(typesDescription).with(", ");
  }

  private String describeIgnoredCollectionOrderInFields() {
    List<String> fieldsDescription = ignoredCollectionOrderInFields.stream()
                                                                   .map(FieldLocation::getFieldPath)
                                                                   .collect(toList());
    return join(fieldsDescription).with(", ");
  }

  private String describeRegexes(List<Pattern> regexes) {
    List<String> fieldsDescription = regexes.stream()
                                            .map(Pattern::pattern)
                                            .collect(toList());
    return join(fieldsDescription).with(", ");
  }

  private boolean isConfiguredToIgnoreSomeButNotAllOverriddenEqualsMethods() {
    boolean ignoreSomeOverriddenEqualsMethods = !ignoredOverriddenEqualsForFieldsMatchingRegexes.isEmpty()
                                                || !ignoredOverriddenEqualsForTypes.isEmpty()
                                                || !ignoredOverriddenEqualsForFields.isEmpty();
    return !ignoreAllOverriddenEquals && ignoreSomeOverriddenEqualsMethods;
  }

  private void describeRegisteredComparatorByTypes(StringBuilder description) {
    if (!typeComparators.isEmpty()) {
      description.append(format("- these types were compared with the following comparators:%n"));
      describeComparatorForTypes(description);
    }
  }

  private void describeComparatorForTypes(StringBuilder description) {
    typeComparators.comparatorByTypes()
                   .map(this::formatRegisteredComparatorByType)
                   .forEach(description::append);
  }

  private String formatRegisteredComparatorByType(Entry<Class<?>, Comparator<?>> next) {
    return format("%s %s -> %s%n", INDENT_LEVEL_2, next.getKey().getName(), next.getValue());
  }

  private void describeRegisteredComparatorForFields(StringBuilder description) {
    if (!fieldComparators.isEmpty()) {
      description.append(format("- these fields were compared with the following comparators:%n"));
      describeComparatorForFields(description);
      if (!typeComparators.isEmpty()) {
        description.append(format("- field comparators take precedence over type comparators.%n"));
      }
    }
  }

  private void describeComparatorForFields(StringBuilder description) {
    fieldComparators.comparatorByFields()
                    .map(this::formatRegisteredComparatorForField)
                    .forEach(description::append);
  }

  private String formatRegisteredComparatorForField(Entry<FieldLocation, Comparator<?>> comparatorForField) {
    return format("%s %s -> %s%n", INDENT_LEVEL_2, comparatorForField.getKey().getFieldPath(), comparatorForField.getValue());
  }

  private void describeTypeCheckingStrictness(StringBuilder description) {
    String str = strictTypeChecking
        ? "- actual and expected objects and their fields were considered different when of incompatible types (i.e. expected type does not extend actual's type) even if all their fields match, for example a Person instance will never match a PersonDto (call strictTypeChecking(false) to change that behavior).%n"
        : "- actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call strictTypeChecking(true) to change that behavior).%n";
    description.append(format(str));
  }

  /**
   * Creates builder to build {@link RecursiveComparisonConfiguration}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link RecursiveComparisonConfiguration}.
   */
  public static final class Builder {
    private boolean strictTypeChecking;
    private boolean ignoreAllActualNullFields;
    private boolean ignoreAllActualEmptyOptionalFields;
    private boolean ignoreAllExpectedNullFields;
    private String[] ignoredFields = {};
    private String[] ignoredFieldsMatchingRegexes = {};
    private Class<?>[] ignoredTypes = {};
    private Class<?>[] ignoredOverriddenEqualsForTypes = {};
    private String[] ignoredOverriddenEqualsForFields = {};
    private String[] ignoredOverriddenEqualsForFieldsMatchingRegexes = {};
    private boolean ignoreAllOverriddenEquals;
    private boolean ignoreCollectionOrder;
    private String[] ignoredCollectionOrderInFields = {};
    private String[] ignoredCollectionOrderInFieldsMatchingRegexes = {};
    private TypeComparators typeComparators = new TypeComparators();
    private FieldComparators fieldComparators = new FieldComparators();

    private Builder() {}

    /**
     * Sets whether the recursive comparison will check that actual's type is compatible with expected's type (the same applies for each field).
     * Compatible means that the expected's type is the same or a subclass of actual's type.
     * <p>
     * See {@link RecursiveComparisonAssert#withStrictTypeChecking()} for code examples.
     *
     * @param strictTypeChecking whether the recursive comparison will check that actual's type is compatible with expected's type.
     * @return this builder.
     */
    public Builder withStrictTypeChecking(boolean strictTypeChecking) {
      this.strictTypeChecking = strictTypeChecking;
      return this;
    }

    /**
     * Sets whether actual null fields are ignored in the recursive comparison.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringActualNullFields()} for code examples.
     *
     * @param ignoreAllActualNullFields whether to ignore actual null fields in the recursive comparison
     * @return this builder.
     */
    public Builder withIgnoreAllActualNullFields(boolean ignoreAllActualNullFields) {
      this.ignoreAllActualNullFields = ignoreAllActualNullFields;
      return this;
    }

    /**
     * Sets whether actual empty optional fields are ignored in the recursive comparison.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringActualEmptyOptionalFields()} for code examples.
     *
     * @param ignoreAllActualEmptyOptionalFields whether to ignore actual empty optional fields in the recursive comparison
     * @return this builder.
     */
    public Builder withIgnoreAllActualEmptyOptionalFields(boolean ignoreAllActualEmptyOptionalFields) {
      this.ignoreAllActualEmptyOptionalFields = ignoreAllActualEmptyOptionalFields;
      return this;
    }

    /**
     * Sets whether expected null fields are ignored in the recursive comparison.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringExpectedNullFields()} for code examples.
     *
     * @param ignoreAllExpectedNullFields whether to ignore expected null fields in the recursive comparison
     * @return this builder.
     */
    public Builder withIgnoreAllExpectedNullFields(boolean ignoreAllExpectedNullFields) {
      this.ignoreAllExpectedNullFields = ignoreAllExpectedNullFields;
      return this;
    }

    /**
     * Adds the given fields to the list of the object under test fields to ignore in the recursive comparison. Nested fields can be specified like this: home.address.street.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringFields(String...) RecursiveComparisonAssert#ignoringFields(String...)} for examples.
     *
     * @param fieldsToIgnore the fields of the object under test to ignore in the comparison.
     * @return this builder.
     */
    public Builder withIgnoredFields(String... fieldsToIgnore) {
      this.ignoredFields = fieldsToIgnore;
      return this;
    }

    /**
     * Allows to ignore in the recursive comparison the object under test fields matching the given regexes. The given regexes are added to the already registered ones.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringFieldsMatchingRegexes(String...) RecursiveComparisonAssert#ignoringFieldsMatchingRegexes(String...)} for examples.
     *
     * @param regexes regexes used to ignore fields in the comparison.
     * @return this builder.
     */
    public Builder withIgnoredFieldsMatchingRegexes(String... regexes) {
      this.ignoredFieldsMatchingRegexes = regexes;
      return this;
    }

    /**
     * Adds the given types to the list of the object under test fields types to ignore in the recursive comparison.
     * The fields are ignored if their types exactly match one of the ignored types, if a field is a subtype of an ignored type it won't be ignored.
     * <p>
     * Note that if some object under test fields are null, they are not ignored by this method as their type can't be evaluated.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringFields(String...) RecursiveComparisonAssert#ignoringFieldsOfTypes(Class...)} for examples.
     *
     * @param types the types of the object under test to ignore in the comparison.
     * @return this builder.
     */
    public Builder withIgnoredFieldsOfTypes(Class<?>... types) {
      this.ignoredTypes = types;
      return this;
    }

    /**
     * Adds the given types to the list of types to force a recursive comparison on.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringOverriddenEqualsForTypes(Class...) RecursiveComparisonAssert#ignoringOverriddenEqualsForTypes(Class...)} for examples.
     *
     * @param types the types to the list of types to force a recursive comparison on.
     * @return this builder.
     */
    public Builder withIgnoredOverriddenEqualsForTypes(Class<?>... types) {
      this.ignoredOverriddenEqualsForTypes = types;
      return this;
    }

    /**
     * Adds the given fields to the list of fields to force a recursive comparison on.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringOverriddenEqualsForFields(String...) RecursiveComparisonAssert#ignoringOverriddenEqualsForFields(String...)} for examples.
     *
     * @param fields the fields to force a recursive comparison on.
     * @return this builder.
     */
    public Builder withIgnoredOverriddenEqualsForFields(String... fields) {
      this.ignoredOverriddenEqualsForFields = fields;
      return this;
    }

    /**
     * Adds the given regexes to the list of regexes used find the fields to force a recursive comparison on.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringOverriddenEqualsForFieldsMatchingRegexes(String...) RecursiveComparisonAssert#ignoringOverriddenEqualsForFieldsMatchingRegexes(String...)} for examples.
     *
     * @param regexes regexes used to specify the fields we want to force a recursive comparison on.
     * @return this builder.
     */
    public Builder withIgnoredOverriddenEqualsForFieldsMatchingRegexes(String... regexes) {
      this.ignoredOverriddenEqualsForFieldsMatchingRegexes = regexes;
      return this;
    }

    /**
     * Force a recursive comparison on all fields (except java types) if true.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringAllOverriddenEquals()} for examples.
     *
     * @param ignoreAllOverriddenEquals whether to force a recursive comparison on all fields (except java types) or not.
     * @return this builder.
     */
    public Builder withIgnoreAllOverriddenEquals(boolean ignoreAllOverriddenEquals) {
      this.ignoreAllOverriddenEquals = ignoreAllOverriddenEquals;
      return this;
    }

    /**
     * Sets whether to ignore collection order in the comparison.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringCollectionOrder()} for code examples.
     *
     * @param ignoreCollectionOrder whether to ignore collection order in the comparison.
     * @return this builder.
     */
    public Builder withIgnoreCollectionOrder(boolean ignoreCollectionOrder) {
      this.ignoreCollectionOrder = ignoreCollectionOrder;
      return this;
    }

    /**
     * Adds the given fields to the list of the object under test fields to ignore collection order in the recursive comparison.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...) RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...)} for examples.
     *
     * @param fieldsToIgnoreCollectionOrder the fields of the object under test to ignore collection order in the comparison.
     * @return this builder.
     */
    public Builder withIgnoredCollectionOrderInFields(String... fieldsToIgnoreCollectionOrder) {
      this.ignoredCollectionOrderInFields = fieldsToIgnoreCollectionOrder;
      return this;
    }

    /**
     * Adds the given regexes to the list of regexes used to find the object under test fields to ignore collection order in the recursive comparison.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...) RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...)} for examples.
     *
     * @param regexes regexes used to find the object under test fields to ignore collection order in in the comparison.
     * @return this builder.
     */
    public Builder withIgnoredCollectionOrderInFieldsMatchingRegexes(String... regexes) {
      this.ignoredCollectionOrderInFieldsMatchingRegexes = regexes;
      return this;
    }

    /**
     * Registers the given {@link Comparator} to compare the fields with the given type.
     * <p>
     * Comparators specified by this method have less precedence than comparators added with {@link #withComparatorForFields(Comparator, String...)}.
     * <p>
     * See {@link RecursiveComparisonAssert#withComparatorForType(Comparator, Class)} for examples.
     *
     * @param <T> the class type to register a comparator for
     * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given field
     * @param type the type to be compared with the given comparator.
     * @return this builder.
     */
    public <T> Builder withComparatorForType(Comparator<? super T> comparator, Class<T> type) {
      this.typeComparators.put(type, comparator);
      return this;
    }

    /**
     * Registers the given {@link Comparator} to compare the fields with the given locations.
     * <p>
     * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
     * one can register to a comparator for Foo and Bar's {@code id} by calling:
     * <pre><code class='java'> registerComparatorForFields(idComparator, "foo.id", "bar.id")</code></pre>
     * <p>
     * Comparators specified by this method have precedence over comparators added with {@link #withComparatorForType(Comparator, Class)}.
     * <p>
     * See {@link RecursiveComparisonAssert#withComparatorForFields(Comparator, String...) RecursiveComparisonAssert#withComparatorForFields(Comparator comparator, String...fields)} for examples.
     *
     * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given field
     * @param fields the fields the comparator should be used for
     * @return this builder.
     */
    public Builder withComparatorForFields(Comparator<?> comparator, String... fields) {
      Stream.of(fields)
            .map(FieldLocation::new)
            .forEach(fieldLocation -> fieldComparators.registerComparator(fieldLocation, comparator));
      return this;
    }

    public RecursiveComparisonConfiguration build() {
      return new RecursiveComparisonConfiguration(this);
    }
  }

}
