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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.internal.RecursiveHelper.isContainer;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.recursive.AbstractRecursiveOperationConfiguration;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.internal.TypeMessages;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.VisibleForTesting;

public class RecursiveComparisonConfiguration extends AbstractRecursiveOperationConfiguration {

  private static final boolean DEFAULT_IGNORE_ALL_OVERRIDDEN_EQUALS = true;
  public static final String INDENT_LEVEL_2 = "  -";
  public static final DefaultRecursiveComparisonIntrospectionStrategy DEFAULT_RECURSIVE_COMPARISON_INTROSPECTION_STRATEGY = new DefaultRecursiveComparisonIntrospectionStrategy();
  private boolean strictTypeChecking = false;

  // fields to ignore section
  private boolean ignoreAllActualNullFields = false;
  private boolean ignoreAllActualEmptyOptionalFields = false;
  private boolean ignoreAllExpectedNullFields = false;

  // fields to compare (no other field will be)
  private Set<FieldLocation> comparedFields = new LinkedHashSet<>();

  // fields of types to compare (no other field will be)
  private Set<Class<?>> comparedTypes = new LinkedHashSet<>();

  // overridden equals method to ignore section
  private final List<Class<?>> ignoredOverriddenEqualsForTypes = new ArrayList<>();
  private List<String> ignoredOverriddenEqualsForFields = new ArrayList<>();
  private final List<Pattern> ignoredOverriddenEqualsForFieldsMatchingRegexes = new ArrayList<>();
  private boolean ignoreAllOverriddenEquals = DEFAULT_IGNORE_ALL_OVERRIDDEN_EQUALS;

  // ignore order in collections section
  private boolean ignoreCollectionOrder = false;
  private Set<String> ignoredCollectionOrderInFields = new LinkedHashSet<>();
  private final List<Pattern> ignoredCollectionOrderInFieldsMatchingRegexes = new ArrayList<>();

  // registered comparators section
  private TypeComparators typeComparators = defaultTypeComparators();
  private FieldComparators fieldComparators = new FieldComparators();

  // registered messages section
  private TypeMessages typeMessages = new TypeMessages();
  private FieldMessages fieldMessages = new FieldMessages();

  // track field locations of fields of type to compare, needed to compare child nodes
  // for example if we want to compare Person type, we must compare Person fields too event though they are not of type Person
  private final Set<FieldLocation> fieldLocationsToCompareBecauseOfTypesToCompare = new LinkedHashSet<>();

  public void registerFieldLocationToCompareBecauseOfTypesToCompare(FieldLocation fieldLocation) {
    fieldLocationsToCompareBecauseOfTypesToCompare.add(fieldLocation);
  }

  private RecursiveComparisonIntrospectionStrategy introspectionStrategy = DEFAULT_RECURSIVE_COMPARISON_INTROSPECTION_STRATEGY;

  private boolean compareEnumAgainstString = false;

  private RecursiveComparisonConfiguration(Builder builder) {
    super(builder);
    this.ignoreAllActualNullFields = builder.ignoreAllActualNullFields;
    this.ignoreAllActualEmptyOptionalFields = builder.ignoreAllActualEmptyOptionalFields;
    this.strictTypeChecking = builder.strictTypeChecking;
    this.ignoreAllExpectedNullFields = builder.ignoreAllExpectedNullFields;
    this.comparedFields = newLinkedHashSet(builder.comparedFields);
    this.comparedTypes = newLinkedHashSet(builder.comparedTypes);
    ignoreOverriddenEqualsForTypes(builder.ignoredOverriddenEqualsForTypes);
    this.ignoredOverriddenEqualsForFields = list(builder.ignoredOverriddenEqualsForFields);
    ignoreOverriddenEqualsForFieldsMatchingRegexes(builder.ignoredOverriddenEqualsForFieldsMatchingRegexes);
    this.ignoreAllOverriddenEquals = builder.ignoreAllOverriddenEquals;
    this.ignoreCollectionOrder = builder.ignoreCollectionOrder;
    this.ignoredCollectionOrderInFields = newLinkedHashSet(builder.ignoredCollectionOrderInFields);
    ignoreCollectionOrderInFieldsMatchingRegexes(builder.ignoredCollectionOrderInFieldsMatchingRegexes);
    this.typeComparators = builder.typeComparators;
    this.fieldComparators = builder.fieldComparators;
    this.fieldMessages = builder.fieldMessages;
    this.typeMessages = builder.typeMessages;
    this.introspectionStrategy = builder.introspectionStrategy;
  }

  public RecursiveComparisonConfiguration() {
    super();
  }

  public boolean hasComparatorForField(String fieldName) {
    return fieldComparators.hasComparatorForField(fieldName);
  }

  public Comparator<?> getComparatorForField(String fieldName) {
    return fieldComparators.getComparatorForField(fieldName);
  }

  public boolean hasCustomMessageForField(String fieldName) {
    return fieldMessages.hasMessageForField(fieldName);
  }

  public String getMessageForField(String fieldName) {
    return fieldMessages.getMessageForField(fieldName);
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
    return typeComparators.getComparatorForType(fieldType);
  }

  public boolean hasCustomMessageForType(Class<?> fieldType) {
    return typeMessages.hasMessageForType(fieldType);
  }

  public String getMessageForType(Class<?> fieldType) {
    return typeMessages.getMessageForType(fieldType);
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
   * Adds the given fields and their subfields to the set of fields from the object under test to compare (no other fields will be compared).
   * <p>
   * The fields are specified by name, not by value, you can specify {@code person.name} but not {@code "Jack"} as {@code "Jack"} is not a field value.
   * <p>
   * Specifying a field will make all its subfields to be compared, for example specifying {@code person} will lead to compare {@code person.name}, {@code person.address} ...
   * on the other hand if you specify {@code person.name}, {@code person} won't be compared but {@code person.name} will be.
   * <p>
   * See {@link RecursiveComparisonAssert#comparingOnlyFields(String...) RecursiveComparisonAssert#comparingOnlyFields(String...)} for examples.
   * <p>
   * Note that the recursive comparison checks whether the fields actually exist and throws an {@link IllegalArgumentException} if some of them don't,
   * this is done to catch typos.
   *
   * @param fieldNamesToCompare the fields of the object under test to compare in the comparison.
   */
  public void compareOnlyFields(String... fieldNamesToCompare) {
    Stream.of(fieldNamesToCompare).map(FieldLocation::new).forEach(comparedFields::add);
  }

  /**
   * Adds the given fields of types and their subfields to the set of fields from the object under test to compare (fields of other types will not be compared).
   * <p>
   * Specifying a field of type will make all its subfields to be compared, for example specifying {@code Person} will lead to compare {@code Person.name}, {@code Person.address} ...
   * <p>
   * See {@link RecursiveComparisonAssert#comparingOnlyFieldsOfTypes(Class...) RecursiveComparisonAssert#comparingOnlyFieldsOfTypes(Class...)} for examples.
   *
   * @param typesToCompare the types to compare in the recursive comparison.
   */
  public void compareOnlyFieldsOfTypes(Class<?>... typesToCompare) {
    stream(typesToCompare).map(AbstractRecursiveOperationConfiguration::asWrapperIfPrimitiveType)
                          .forEach(comparedTypes::add);
  }

  /**
   * Returns the set of fields to compare from the object under test (no other fields will be compared).
   *
   * @return the set of fields from the object under test to compare.
   */
  public Set<FieldLocation> getComparedFields() {
    return comparedFields;
  }

  boolean someComparedFieldsHaveBeenSpecified() {
    return !comparedFields.isEmpty();
  }

  boolean isOrIsChildOfAnyComparedFields(FieldLocation currentFieldLocation) {
    return comparedFields.stream()
                         .anyMatch(comparedField -> comparedField.equals(currentFieldLocation)
                                                    || comparedField.hasChild(currentFieldLocation));
  }

  /**
   * Returns the set of type to compare from the object under test (fields of other types will not be compared).
   *
   * @return the set of types from the object under test to compare.
   */
  public Set<Class<?>> getComparedTypes() {
    return comparedTypes;
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
    List<String> fieldLocations = list(fields);
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
   * <b>Important:</b> ignoring collection order has a high performance cost because each element of the actual collection must
   * be compared to each element of the expected collection which is a O(n&sup2;) operation. For example with a collection of 100
   * elements, the number of comparisons is 100x100 = 10 000!
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringCollectionOrder()} for code examples.
   *
   * @param ignoreCollectionOrder whether to ignore collection order in the comparison.
   */
  public void ignoreCollectionOrder(boolean ignoreCollectionOrder) {
    this.ignoreCollectionOrder = ignoreCollectionOrder;
  }

  /**
   * Adds the given fields to the list fields from the object under test to ignore collection order in the recursive comparison.
   * <p>
   * <b>Important:</b> ignoring collection order has a high performance cost because each element of the actual collection must
   * be compared to each element of the expected collection which is a O(n&sup2;) operation. For example with a collection of 100
   * elements, the number of comparisons is 100x100 = 10 000!
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...) RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...)} for examples.
   *
   * @param fieldsToIgnoreCollectionOrder the fields of the object under test to ignore collection order in the comparison.
   */
  public void ignoreCollectionOrderInFields(String... fieldsToIgnoreCollectionOrder) {
    List<String> fieldLocations = list(fieldsToIgnoreCollectionOrder);
    ignoredCollectionOrderInFields.addAll(fieldLocations);
  }

  /**
   * Returns the list fields from the object under test to ignore collection order in the recursive comparison.
   *
   * @return the list fields from the object under test to ignore collection order in the recursive comparison.
   */
  public Set<String> getIgnoredCollectionOrderInFields() {
    return ignoredCollectionOrderInFields;
  }

  /**
   * Adds the given regexes to the list of regexes used to find the object under test fields to ignore collection order in the recursive comparison.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...) RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...)} for examples.
   *
   * @param regexes regexes used to find the object under test fields to ignore collection order in the comparison.
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
   * Comparators registered with this method have less precedence than comparators registered with
   * {@link #registerComparatorForFields(Comparator, String...)}.
   * <p>
   * Note that registering a {@link Comparator} for a given type will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * See {@link RecursiveComparisonAssert#withComparatorForType(Comparator, Class)} for examples.
   *
   * @param <T> the class type to register a comparator for
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given type
   * @param type the type to be compared with the given comparator.
   * @throws NullPointerException if the given comparator is null.
   */
  public <T> void registerComparatorForType(Comparator<? super T> comparator, Class<T> type) {
    requireNonNull(comparator, "Expecting a non null Comparator");
    typeComparators.registerComparator(type, comparator);
  }

  /**
   * Registers the given {@link BiPredicate} to compare the fields with the given type.
   * <p>
   * BiPredicates specified with this method have less precedence than the ones registered with
   * {@link #registerEqualsForFields(BiPredicate, String...)}
   * or comparators registered with {@link #registerComparatorForFields(Comparator, String...)}.
   * <p>
   * Note that registering a {@link BiPredicate} for a given type will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * See {@link RecursiveComparisonAssert#withEqualsForType(BiPredicate, Class)} for examples.
   *
   * @param <T> the class type to register a comparator for
   * @param equals the equals implementation to compare the given type
   * @param type the type to be compared with the given equals implementation .
   * @throws NullPointerException if the given BiPredicate is null.
   * @since 3.17.0
   */
  @SuppressWarnings("unchecked")
  public <T> void registerEqualsForType(BiPredicate<? super T, ? super T> equals, Class<T> type) {
    registerComparatorForType(toComparator(equals), type);
  }

  /**
   * Registers the given {@link Comparator} to compare the fields at the given locations.
   * <p>
   * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
   * one can register a comparator for Foo and Bar's {@code id} by calling:
   * <pre><code class='java'> registerComparatorForFields(idComparator, "foo.id", "foo.bar.id")</code></pre>
   * <p>
   * Comparators registered with this method have precedence over comparators registered with {@link #registerComparatorForType(Comparator, Class)}.
   * <p>
   * Note that registering a {@link Comparator} for a given field will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * See {@link RecursiveComparisonAssert#withComparatorForFields(Comparator, String...) RecursiveComparisonAssert#withComparatorForFields(Comparator, String...)} for examples.
   *
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given field
   * @param fieldLocations the locations from the root object of the fields the comparator should be used for
   * @throws NullPointerException if the given comparator is null.
   */
  public void registerComparatorForFields(Comparator<?> comparator, String... fieldLocations) {
    requireNonNull(comparator, "Expecting a non null Comparator");
    Stream.of(fieldLocations).forEach(fieldLocation -> fieldComparators.registerComparator(fieldLocation, comparator));
  }

  /**
   * Registers the given {@link BiPredicate} to compare the fields at the given locations.
   * <p>
   * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
   * one can register a BiPredicate for Foo and Bar's {@code id} by calling:
   * <pre><code class='java'> registerEqualsForFields(idBiPredicate, "foo.id", "foo.bar.id")</code></pre>
   * <p>
   * BiPredicates registered with this method have precedence over the ones registered with {@link #registerEqualsForType(BiPredicate, Class)}
   * or the comparators registered with {@link #registerComparatorForType(Comparator, Class)}.
   * <p>
   * Note that registering a {@link BiPredicate} for a given field will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * See {@link RecursiveComparisonAssert#withEqualsForFields(BiPredicate, String...) RecursiveComparisonAssert#withEqualsForFields(BiPredicate, String...)} for examples.
   *
   * @param equals the equals implementation to compare the given fields.
   * @param fieldLocations the locations from the root object of the fields the comparator should be used for
   * @throws NullPointerException if the given BiPredicate is null.
   * @since 3.17.0
   */
  public void registerEqualsForFields(BiPredicate<?, ?> equals, String... fieldLocations) {
    registerComparatorForFields(toComparator(equals), fieldLocations);
  }

  /**
   * Allows to register a {@link BiPredicate} to compare fields whose location matches the given regexes.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * The fields are evaluated from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
   * one can register a BiPredicate for Foo and Bar's {@code id} by calling:
   * <pre><code class='java'> registerEqualsForFieldsMatchingRegexes(idBiPredicate, ".*id")</code></pre>
   * or
   * <pre><code class='java'> registerEqualsForFieldsMatchingRegexes(idBiPredicate, "foo.*id")</code></pre>
   * <p>
   * BiPredicates registered with this method have precedence over the ones registered with {@link #registerEqualsForType(BiPredicate, Class)}
   * or the comparators registered with {@link #registerComparatorForType(Comparator, Class)} but don't have precedence over the
   * ones registered with exact location match: {@link #registerEqualsForFields(BiPredicate, String...)} or {@link #registerComparatorForFields(Comparator, String...)}
   * <p>
   * If registered regexes for different {@link BiPredicate} match a given field, the latest registered regexes {@link BiPredicate} wins.
   * <p>
   * Example: see {@link RecursiveComparisonAssert#withEqualsForFieldsMatchingRegexes(BiPredicate, String...)}
   *
   * @param equals the {@link BiPredicate} to use to compare the fields matching the given regexes
   * @param regexes the regexes from the root object of the fields location the BiPredicate should be used for
   *
   * @throws NullPointerException if the given BiPredicate is null.
   * @since 3.24.0
   */
  public void registerEqualsForFieldsMatchingRegexes(BiPredicate<?, ?> equals, String... regexes) {
    fieldComparators.registerComparatorForFieldsMatchingRegexes(regexes, toComparator(equals));
  }

  /**
   * Registers the giving message which would be shown when differences in the given fields while comparison occurred.
   * <p>
   * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both
   * have an {@code id} field, one can register a message for Foo and Bar's {@code id} by calling:
   * <pre><code class='java'> registerErrorMessageForFields("some message", "foo.id", "foo.bar.id")</code></pre>
   * <p>
   * Messages registered with this method have precedence over the ones registered with {@link #registerErrorMessageForType(String, Class)}.
   * <p>
   * In case of {@code null} as message the default error message will be used (See {@link ComparisonDifference#DEFAULT_TEMPLATE}).
   *
   * @param message the error message that will be thrown when comparison error occurred
   * @param fieldLocations the field locations the error message should be used for
   */
  public void registerErrorMessageForFields(String message, String... fieldLocations) {
    Stream.of(fieldLocations).forEach(fieldLocation -> fieldMessages.registerMessage(fieldLocation, message));
  }

  /**
   * Registers the giving message which would be shown when differences for the giving type while comparison
   * occurred.
   * <p>
   * Message registered with this method have less precedence than the ones registered with {@link #registerErrorMessageForFields(String, String...)}.
   * <p>
   * In case of {@code null} as message the default error message will be used (See {@link ComparisonDifference#DEFAULT_TEMPLATE}).
   *
   * @param message the error message that will be thrown when comparison error occurred
   * @param clazz the type the error message should be used for
   */
  public void registerErrorMessageForType(String message, Class<?> clazz) {
    typeMessages.registerMessage(clazz, message);
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

  public List<Class<?>> getIgnoredOverriddenEqualsForTypes() {
    return ignoredOverriddenEqualsForTypes;
  }

  public List<String> getIgnoredOverriddenEqualsForFields() {
    return ignoredOverriddenEqualsForFields;
  }

  public List<Pattern> getIgnoredOverriddenEqualsForFieldsMatchingRegexes() {
    return ignoredOverriddenEqualsForFieldsMatchingRegexes;
  }

  public Stream<Entry<String, Comparator<?>>> comparatorByFields() {
    return fieldComparators.comparatorByFields();
  }

  RecursiveComparisonIntrospectionStrategy getIntrospectionStrategy() {
    return introspectionStrategy;
  }

  /**
   * Defines how objects are introspected in the recursive comparison.
   * <p>
   * Default to {@link DefaultRecursiveComparisonIntrospectionStrategy}.
   *
   * @param introspectionStrategy the {@link RecursiveComparisonIntrospectionStrategy} to use
   */
  public void setIntrospectionStrategy(RecursiveComparisonIntrospectionStrategy introspectionStrategy) {
    this.introspectionStrategy = introspectionStrategy;
  }

  /**
   * Allows the recursive comparison to compare an enum field against a string field.
   * <p>
   * See {@link RecursiveComparisonAssert#withEnumStringComparison()} for code examples.
   *
   * @param compareEnumAgainstString whether to allow the recursive comparison to compare enum field against string field.
   */
  public void allowComparingEnumAgainstString(boolean compareEnumAgainstString) {
    this.compareEnumAgainstString = compareEnumAgainstString;
  }

  public boolean isComparingEnumAgainstStringAllowed() {
    return this.compareEnumAgainstString;
  }

  @Override
  public String toString() {
    return multiLineDescription(CONFIGURATION_PROVIDER.representation());
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(fieldComparators, ignoreAllActualEmptyOptionalFields, ignoreAllActualNullFields,
                                  ignoreAllExpectedNullFields, ignoreAllOverriddenEquals, ignoreCollectionOrder,
                                  ignoredCollectionOrderInFields, ignoredCollectionOrderInFieldsMatchingRegexes,
                                  getIgnoredFields(), getIgnoredFieldsRegexes(), ignoredOverriddenEqualsForFields,
                                  ignoredOverriddenEqualsForTypes, ignoredOverriddenEqualsForFieldsMatchingRegexes,
                                  getIgnoredTypes(), strictTypeChecking, typeComparators, comparedFields, comparedTypes,
                                  fieldMessages, typeMessages, compareEnumAgainstString);
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
           && java.util.Objects.equals(getIgnoredFields(), other.getIgnoredFields())
           && java.util.Objects.equals(comparedFields, other.comparedFields)
           && java.util.Objects.equals(comparedTypes, other.comparedTypes)
           && java.util.Objects.equals(getIgnoredFieldsRegexes(), other.getIgnoredFieldsRegexes())
           && java.util.Objects.equals(ignoredOverriddenEqualsForFields, other.ignoredOverriddenEqualsForFields)
           && java.util.Objects.equals(ignoredOverriddenEqualsForTypes, other.ignoredOverriddenEqualsForTypes)
           && java.util.Objects.equals(ignoredOverriddenEqualsForFieldsMatchingRegexes,
                                       other.ignoredOverriddenEqualsForFieldsMatchingRegexes)
           && java.util.Objects.equals(getIgnoredTypes(), other.getIgnoredTypes())
           && strictTypeChecking == other.strictTypeChecking
           && java.util.Objects.equals(typeComparators, other.typeComparators)
           && java.util.Objects.equals(ignoredCollectionOrderInFieldsMatchingRegexes,
                                       other.ignoredCollectionOrderInFieldsMatchingRegexes)
           && java.util.Objects.equals(fieldMessages, other.fieldMessages)
           && java.util.Objects.equals(typeMessages, other.typeMessages);
  }

  public String multiLineDescription(Representation representation) {
    StringBuilder description = new StringBuilder();
    describeIgnoreAllActualNullFields(description);
    describeIgnoreAllActualEmptyOptionalFields(description);
    describeIgnoreAllExpectedNullFields(description);
    describeComparedFields(description);
    describeComparedTypes(description);
    describeIgnoredFields(description);
    describeIgnoredFieldsRegexes(description);
    describeIgnoredTypes(description);
    describeIgnoredTypesRegexes(description);
    describeOverriddenEqualsMethodsUsage(description, representation);
    describeIgnoreCollectionOrder(description);
    describeIgnoredCollectionOrderInFields(description);
    describeIgnoredCollectionOrderInFieldsMatchingRegexes(description);
    describeRegisteredComparatorByTypes(description);
    describeRegisteredComparatorForFields(description);
    describeTypeCheckingStrictness(description);
    describeRegisteredErrorMessagesForFields(description);
    describeRegisteredErrorMessagesForTypes(description);
    describeIntrospectionStrategy(description);
    describeCompareEnumAgainstString(description);
    return description.toString();
  }

  boolean shouldNotEvaluate(DualValue dualValue) {
    // if we have some compared types, we can't discard any values since they could have fields we need to compare.
    if (!comparedTypes.isEmpty()) return false;
    return shouldIgnore(dualValue);
  }

  boolean shouldIgnore(DualValue dualValue) {
    return shouldIgnoreFieldBasedOnFieldLocation(dualValue.fieldLocation)
           || shouldIgnoreFieldBasedOnFieldValue(dualValue);
  }

  private boolean shouldBeCompared(DualValue dualValue) {
    // no comparedFields and comparedTypes <=> no restriction on compared fields => everything must be compared
    if (comparedFields.isEmpty() && comparedTypes.isEmpty()) return true;
    // if we have compared types, we can't ignore any values since they could have fields of types to compare
    if (!comparedTypes.isEmpty()) return true;
    return comparedFields.stream().anyMatch(matchesComparedField(dualValue.fieldLocation));
  }

  private static Predicate<FieldLocation> matchesComparedField(FieldLocation field) {
    // a field f must be compared if any compared fields is f itself (obviously), a parent of f or a child of f.
    // - "name.first" must be compared if "name" is a compared field so will other "name" subfields like "name.last"
    // - "name" must be compared if "name.first" is a compared field otherwise "name" is ignored and "name.first" too
    return comparedField -> field.isRoot() // always compare root!
                            || field.exactlyMatches(comparedField)
                            || field.hasParent(comparedField) // ex: field "name.first" and "name" compared field
                            || field.hasChild(comparedField); // ex: field "name" and "name.first" compared field
  }

  Set<String> getActualChildrenNodeNamesToCompare(DualValue dualValue) {
    Set<String> actualChildrenNodeNames = getChildrenNodeNamesOf(dualValue.actual);
    // if we have some compared types, we can't discard any nodes since they could have fields we need to compare.
    // we could evaluate the whole graphs to figure that but that would be bad performance wise so add everything
    // and exclude later on any differences that were on fields not to compare
    if (!comparedTypes.isEmpty()) {
      registerFieldLocationOfFieldsOfTypesToCompare(dualValue);
      return actualChildrenNodeNames;
    }
    // we are doing the same as shouldIgnore(DualValue dualValue) but in two steps for performance reasons:
    // - we filter first ignored nodes by names that don't need building DualValues
    // - then we filter field DualValues with the remaining criteria that need to get the node value
    // DualValues are built by introspecting node values which is expensive.
    return actualChildrenNodeNames.stream()
                                  // evaluate field name ignoring criteria on dualValue field location + field name
                                  .filter(fieldName -> !shouldIgnoreFieldBasedOnFieldLocation(dualValue.fieldLocation.field(fieldName)))
                                  .map(fieldName -> dualValueForField(dualValue, fieldName))
                                  // evaluate field value ignoring criteria
                                  .filter(fieldDualValue -> !shouldIgnoreFieldBasedOnFieldValue(fieldDualValue))
                                  .filter(this::shouldBeCompared)
                                  // back to field name
                                  .map(DualValue::getFieldName)
                                  .filter(fieldName -> !fieldName.isEmpty())
                                  .collect(toSet());
  }

  Set<String> getChildrenNodeNamesOf(Object instance) {
    return introspectionStrategy.getChildrenNodeNamesOf(instance);
  }

  Object getValue(String name, Object instance) {
    return introspectionStrategy.getChildNodeValue(name, instance);
  }

  // non accessible stuff

  private boolean shouldIgnoreFieldBasedOnFieldValue(DualValue dualValue) {
    return matchesAnIgnoredNullField(dualValue)
           || matchesAnIgnoredFieldType(dualValue)
           || matchesAnIgnoredEmptyOptionalField(dualValue);
  }

  private boolean shouldIgnoreFieldBasedOnFieldLocation(FieldLocation fieldLocation) {
    return matchesAnIgnoredField(fieldLocation) || matchesAnIgnoredFieldRegex(fieldLocation);
  }

  private DualValue dualValueForField(DualValue parentDualValue, String fieldName) {
    Object actualFieldValue = getValue(fieldName, parentDualValue.actual);
    // no guarantees we have a field in expected named as fieldName
    Object expectedFieldValue;
    try {
      expectedFieldValue = getValue(fieldName, parentDualValue.expected);
    } catch (@SuppressWarnings("unused") Exception e) {
      // set the field to null to express it is absent, this not 100% accurate as the value could be null,
      // but it works to evaluate if dualValue should be ignored with matchesAnIgnoredFieldType
      expectedFieldValue = null;
    }
    FieldLocation fieldLocation = parentDualValue.fieldLocation.field(fieldName);
    return new DualValue(fieldLocation, actualFieldValue, expectedFieldValue);
  }

  boolean hasCustomComparator(DualValue dualValue) {
    String fieldName = dualValue.getConcatenatedPath();
    if (hasComparatorForField(fieldName)) return true;
    if (dualValue.actual == null && dualValue.expected == null) return false;
    // best effort assuming actual and expected have the same type (not 100% true as we can compare object of different types)
    Class<?> valueType = dualValue.actual != null ? dualValue.actual.getClass() : dualValue.expected.getClass();
    return hasComparatorForType(valueType);
  }

  boolean shouldIgnoreOverriddenEqualsOf(DualValue dualValue) {
    // root objects are not compared with equals as it makes the recursive comparison pointless (use isEqualsTo instead)
    if (dualValue.fieldLocation.isRoot()) return true;
    // we must compare java basic types otherwise the recursive comparison loops infinitely!
    if (dualValue.isActualJavaType()) return false;
    // enums don't have fields, comparing them field by field makes no sense; we need to use equals, which is overridden and final
    if (dualValue.isActualAnEnum()) return false;
    // if there are some compared fields, we must only honor overridden equals on them, if the value is not a compared
    // field then we treat as usual and ignore its equals method and introspects it
    if (someComparedFieldsHaveBeenSpecified() && !exactlyMatchesAnyComparedFields(dualValue)) return true;
    return ignoreAllOverriddenEquals
           || matchesAnIgnoredOverriddenEqualsField(dualValue)
           || (dualValue.actual != null && shouldIgnoreOverriddenEqualsOf(dualValue.actual.getClass()));
  }

  @VisibleForTesting
  boolean shouldIgnoreOverriddenEqualsOf(Class<?> clazz) {
    return matchesAnIgnoredOverriddenEqualsType(clazz);
  }

  boolean shouldIgnoreCollectionOrder(FieldLocation fieldLocation) {
    return ignoreCollectionOrder
           || matchesAnIgnoredCollectionOrderInField(fieldLocation)
           || matchesAnIgnoredCollectionOrderInFieldRegex(fieldLocation);
  }

  private void describeComparedFields(StringBuilder description) {
    if (!comparedFields.isEmpty())
      description.append(format("- the comparison was performed on the following fields: %s%n", describeComparedFields()));
  }

  private void describeComparedTypes(StringBuilder description) {
    if (!comparedTypes.isEmpty())
      description.append(format("- the comparison was performed on any fields with types: %s%n", describeComparedTypes()));
  }

  private void describeIgnoredTypes(StringBuilder description) {
    if (!getIgnoredTypes().isEmpty())
      description.append(format("- the following types were ignored in the comparison: %s%n", describeIgnoredTypes()));
  }

  private void describeIgnoredTypesRegexes(StringBuilder description) {
    if (!getIgnoredTypesRegexes().isEmpty())
      description.append(format("- the types matching the following regexes were ignored in the comparison: %s%n",
                                describeRegexes(getIgnoredTypesRegexes())));
  }

  protected void describeIgnoreAllActualNullFields(StringBuilder description) {
    if (ignoreAllActualNullFields) description.append(format("- all actual null fields were ignored in the comparison%n"));
  }

  protected void describeIgnoreAllActualEmptyOptionalFields(StringBuilder description) {
    if (ignoreAllActualEmptyOptionalFields)
      description.append(format("- all actual empty optional fields were ignored in the comparison (including Optional, OptionalInt, OptionalLong and OptionalDouble)%n"));
  }

  private void describeIgnoreAllExpectedNullFields(StringBuilder description) {
    if (ignoreAllExpectedNullFields) description.append(format("- all expected null fields were ignored in the comparison%n"));
  }

  private void describeOverriddenEqualsMethodsUsage(StringBuilder description, Representation representation) {
    String header = ignoreAllOverriddenEquals
        ? "- no equals methods were used in the comparison EXCEPT for java JDK types since introspecting JDK types is forbidden in java 17+ (use withEqualsForType to register a specific way to compare a JDK type if you need it)"
        : "- equals methods were used in the comparison";
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
      description.append(format("%s the fields matching the following regexes: %s%n", INDENT_LEVEL_2,
                                describeRegexes(ignoredOverriddenEqualsForFieldsMatchingRegexes)));
  }

  private String describeIgnoredOverriddenEqualsForTypes(Representation representation) {
    List<String> fieldsDescription = ignoredOverriddenEqualsForTypes.stream()
                                                                    .map(representation::toStringOf)
                                                                    .collect(toList());
    return join(fieldsDescription);
  }

  private String describeIgnoredOverriddenEqualsForFields() {
    return join(ignoredOverriddenEqualsForFields);
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

  private void describeIntrospectionStrategy(StringBuilder description) {
    description.append(format("- the introspection strategy used was: %s%n", introspectionStrategy.getDescription()));
  }

  private void describeCompareEnumAgainstString(StringBuilder description) {
    if (compareEnumAgainstString)
      description.append(format("- enums can be compared against strings (and vice versa), e.g. Color.RED and \"RED\" are considered equal%n"));
  }

  private boolean matchesAnIgnoredOverriddenEqualsRegex(FieldLocation fieldLocation) {
    if (ignoredOverriddenEqualsForFieldsMatchingRegexes.isEmpty()) return false; // shortcut
    String pathToUseInRules = fieldLocation.getPathToUseInRules();
    return ignoredOverriddenEqualsForFieldsMatchingRegexes.stream().anyMatch(regex -> regex.matcher(pathToUseInRules).matches());
  }

  private boolean matchesAnIgnoredOverriddenEqualsType(Class<?> clazz) {
    return ignoredOverriddenEqualsForTypes.contains(clazz);
  }

  private boolean matchesAnIgnoredOverriddenEqualsField(DualValue dualValue) {
    FieldLocation fieldLocation = dualValue.fieldLocation;
    return ignoredOverriddenEqualsForFields.stream().anyMatch(fieldLocation::exactlyMatches)
           || matchesAnIgnoredOverriddenEqualsRegex(fieldLocation);
  }

  private boolean matchesAnIgnoredNullField(DualValue dualValue) {
    return (ignoreAllActualNullFields && dualValue.actual == null)
           || (ignoreAllExpectedNullFields && dualValue.expected == null);
  }

  private boolean matchesAnIgnoredEmptyOptionalField(DualValue dualValue) {
    return ignoreAllActualEmptyOptionalFields
           && dualValue.isActualFieldAnEmptyOptionalOfAnyType();
  }

  private boolean matchesAnIgnoredFieldType(DualValue dualValue) {
    Object actual = dualValue.actual;
    if (actual != null) return matchesAnIgnoredType(actual);
    Object expected = dualValue.expected;
    // actual is null => we can't evaluate its type, we can only reliably check dualValue.expected's type if
    // strictTypeChecking is enabled which guarantees expected is of the same type.
    if (strictTypeChecking && expected != null) return matchesAnIgnoredType(expected);
    // if strictTypeChecking is disabled, we can't safely ignore the field (if we did, we would ignore all null fields!).
    return false;
  }

  private boolean matchesAnIgnoredType(Object actual) {
    Class<?> actualType = actual.getClass();
    return getIgnoredTypes().contains(actualType)
           || getIgnoredTypesRegexes().stream().anyMatch(regex -> regex.matcher(actualType.getName()).matches());
  }

  private void registerFieldLocationOfFieldsOfTypesToCompare(DualValue dualValue) {
    if (comparedTypes.isEmpty()) return;
    // We check actual type against the types to compare or expected type in case actual was null assuming expected
    // has the same type as actual
    if ((dualValue.actual != null && comparedTypes.contains(dualValue.actual.getClass()))
        || (dualValue.expected != null && comparedTypes.contains(dualValue.expected.getClass()))) {
      fieldLocationsToCompareBecauseOfTypesToCompare.add(dualValue.fieldLocation);
    }
  }

  private boolean matchesAnIgnoredCollectionOrderInField(FieldLocation fieldLocation) {
    return ignoredCollectionOrderInFields.stream().anyMatch(fieldLocation::exactlyMatches);
  }

  private boolean matchesAnIgnoredCollectionOrderInFieldRegex(FieldLocation fieldLocation) {
    String pathToUseInRules = fieldLocation.getPathToUseInRules();
    return ignoredCollectionOrderInFieldsMatchingRegexes.stream().anyMatch(regex -> regex.matcher(pathToUseInRules).matches());
  }

  private String describeComparedFields() {
    return join(comparedFields.stream().map(FieldLocation::shortDescription).collect(toList()));
  }

  private String describeComparedTypes() {
    List<String> typesDescription = comparedTypes.stream()
                                                 .map(Class::getName)
                                                 .collect(toList());
    return join(typesDescription);
  }

  private String describeIgnoredCollectionOrderInFields() {
    return join(ignoredCollectionOrderInFields);
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
      if (fieldComparators.hasFieldComparators()) {
        description.append(format("- these fields were compared with the following comparators:%n"));
        describeComparatorForFields(description);
      }
      if (fieldComparators.hasRegexFieldComparators()) {
        description.append(format("- the fields matching these regexes were compared with the following comparators:%n"));
        describeComparatorForRegexFields(description);
      }
      if (fieldComparators.hasFieldComparators() && fieldComparators.hasRegexFieldComparators()) {
        description.append(format("- field comparators take precedence over regex field matching comparators.%n"));
      }
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

  private void describeComparatorForRegexFields(StringBuilder description) {
    fieldComparators.comparatorByRegexFields()
                    .map(this::formatRegisteredComparatorForRegexFields)
                    .sorted()
                    .forEach(description::append);
  }

  private String formatRegisteredComparatorForField(Entry<String, Comparator<?>> comparatorForField) {
    return format("%s %s -> %s%n", INDENT_LEVEL_2, comparatorForField.getKey(), comparatorForField.getValue());
  }

  private String formatRegisteredComparatorForRegexFields(Entry<List<Pattern>, Comparator<?>> comparatorForRegexFields) {
    return format("%s %s -> %s%n", INDENT_LEVEL_2, comparatorForRegexFields.getKey(), comparatorForRegexFields.getValue());
  }

  private void describeTypeCheckingStrictness(StringBuilder description) {
    String str = strictTypeChecking
        ? "- actual and expected objects and their fields were considered different when of incompatible types (i.e. expected type does not extend actual's type) even if all their fields match, for example a Person instance will never match a PersonDto (call strictTypeChecking(false) to change that behavior).%n"
        : "- actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call strictTypeChecking(true) to change that behavior).%n";
    description.append(format(str));
  }

  private void describeRegisteredErrorMessagesForFields(StringBuilder description) {
    if (!fieldMessages.isEmpty()) {
      description.append(format("- these fields had overridden error messages:%n"));
      describeErrorMessagesForFields(description);
      if (!typeMessages.isEmpty()) {
        description.append(format("- field custom messages take precedence over type messages.%n"));
      }
    }
  }

  private void describeErrorMessagesForFields(StringBuilder description) {
    String fields = fieldMessages.messageByFields()
                                 .map(Entry::getKey)
                                 .collect(joining(DEFAULT_DELIMITER));
    description.append(format("%s %s%n", INDENT_LEVEL_2, fields));
  }

  private void describeRegisteredErrorMessagesForTypes(StringBuilder description) {
    if (!typeMessages.isEmpty()) {
      description.append("- these types had overridden error messages:%n");
      describeErrorMessagesForType(description);
    }
  }

  private void describeErrorMessagesForType(StringBuilder description) {
    String types = typeMessages.messageByTypes()
                               .map(it -> it.getKey().getName())
                               .collect(joining(DEFAULT_DELIMITER));
    description.append(format("%s %s%n", INDENT_LEVEL_2, types));
  }

  /**
   * Creates builder to build {@link RecursiveComparisonConfiguration}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  void checkComparedFieldsExist(Object actual) {
    Map<FieldLocation, String> unknownComparedFields = new TreeMap<>();
    for (FieldLocation comparedField : comparedFields) {
      checkComparedFieldExists(actual,
                               comparedField).ifPresent(entry -> unknownComparedFields.put(entry.getKey(), entry.getValue()));
    }
    if (!unknownComparedFields.isEmpty()) {
      StringBuilder errorMessageBuilder = new StringBuilder("The following fields don't exist: ");
      unknownComparedFields.forEach((fieldLocation,
                                     nodeName) -> errorMessageBuilder.append(formatUnknownComparedField(fieldLocation,
                                                                                                        nodeName)));
      throw new IllegalArgumentException(errorMessageBuilder.toString());
    }
  }

  private Optional<Entry<FieldLocation, String>> checkComparedFieldExists(Object actual, FieldLocation comparedFieldLocation) {
    Object node = actual;
    int nestingLevel = 0;
    while (nestingLevel < comparedFieldLocation.getDecomposedPath().size()) {
      if (node == null) {
        // won't be able to get children nodes, assume the field is known as we can't check it
        return Optional.empty();
      }
      if (isContainer(node)) {
        // TODO: supported with https://github.com/assertj/assertj/issues/3354
        return Optional.empty();
      }
      String comparedFieldNodeNameElement = comparedFieldLocation.getDecomposedPath().get(nestingLevel);
      Set<String> nodeNames = introspectionStrategy.getChildrenNodeNamesOf(node);
      if (!nodeNames.contains(comparedFieldNodeNameElement)) {
        return Optional.of(entry(comparedFieldLocation, comparedFieldNodeNameElement));
      }
      node = introspectionStrategy.getChildNodeValue(comparedFieldNodeNameElement, node);
      nestingLevel++;
    }
    return Optional.empty();
  }

  private static String formatUnknownComparedField(FieldLocation fieldLocation, String unknownNodeNameElement) {
    return fieldLocation.isTopLevelField()
        ? format("{%s}", unknownNodeNameElement)
        : format("{%s in %s}", unknownNodeNameElement, fieldLocation);
  }

  boolean hierarchyMatchesAnyComparedTypes(DualValue dualValue) {
    if (isFieldOfTypeToCompare(dualValue)) return true;
    // dualValue is not a type to compare but could be a child of one
    return fieldLocationsToCompareBecauseOfTypesToCompare.stream().anyMatch(dualValue.fieldLocation::hasParent);
  }

  boolean matchesOrIsChildOfFieldMatchingAnyComparedTypes(DualValue dualValue) {
    return fieldLocationsToCompareBecauseOfTypesToCompare.stream().anyMatch(dualValue.fieldLocation::exactlyMatches);
  }

  boolean hasComparedTypes() {
    return !comparedTypes.isEmpty();
  }

  private boolean isFieldOfTypeToCompare(DualValue dualValue) {
    Object valueToCheck = dualValue.actual != null ? dualValue.actual : dualValue.expected;
    return valueToCheck != null && comparedTypes.contains(valueToCheck.getClass());
  }

  boolean exactlyMatchesAnyComparedFields(DualValue dualValue) {
    return comparedFields.stream().anyMatch(comparedField -> comparedField.exactlyMatches(dualValue.fieldLocation));
  }

  /**
   * Builder to build {@link RecursiveComparisonConfiguration}.
   */
  public static final class Builder extends AbstractBuilder<Builder> {
    private boolean strictTypeChecking;
    private boolean ignoreAllActualNullFields;
    private boolean ignoreAllActualEmptyOptionalFields;
    private boolean ignoreAllExpectedNullFields;
    private FieldLocation[] comparedFields = {};
    private Class<?>[] comparedTypes = {};
    private Class<?>[] ignoredOverriddenEqualsForTypes = {};
    private String[] ignoredOverriddenEqualsForFields = {};
    private String[] ignoredOverriddenEqualsForFieldsMatchingRegexes = {};
    private boolean ignoreAllOverriddenEquals = DEFAULT_IGNORE_ALL_OVERRIDDEN_EQUALS;
    private boolean ignoreCollectionOrder;
    private String[] ignoredCollectionOrderInFields = {};
    private String[] ignoredCollectionOrderInFieldsMatchingRegexes = {};
    private final TypeComparators typeComparators = defaultTypeComparators();
    private final FieldComparators fieldComparators = new FieldComparators();
    private final FieldMessages fieldMessages = new FieldMessages();
    private final TypeMessages typeMessages = new TypeMessages();

    private RecursiveComparisonIntrospectionStrategy introspectionStrategy = DEFAULT_RECURSIVE_COMPARISON_INTROSPECTION_STRATEGY;

    private Builder() {
      super(Builder.class);
    }

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
     * Adds the given fields to the set of fields from the object under test to compare in the recursive comparison.
     * <p>
     * Nested fields can be specified like this: home.address.street.
     * <p>
     * See {@link RecursiveComparisonAssert#comparingOnlyFields(String...)} for examples.
     *
     * @param fieldsToCompare the fields of the object under test to compare.
     * @return this builder.
     */
    public Builder withComparedFields(String... fieldsToCompare) {
      this.comparedFields = Stream.of(fieldsToCompare).map(FieldLocation::new).toArray(FieldLocation[]::new);
      return this;
    }

    /**
     * Adds the given types to the set of fields from the object under test to compare in the recursive comparison.
     * See {@link RecursiveComparisonAssert#comparingOnlyFieldsOfTypes(Class[])} for examples.
     *
     * @param comparedTypes the types to compare in the recursive comparison.
     * @return this builder.
     */
    public Builder withComparedTypes(Class<?>... comparedTypes) {
      this.comparedTypes = comparedTypes;
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
     * Adds the given fields to the list fields from the object under test to ignore collection order in the recursive comparison.
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
     * @param regexes regexes used to find the object under test fields to ignore collection order in the comparison.
     * @return this builder.
     */
    public Builder withIgnoredCollectionOrderInFieldsMatchingRegexes(String... regexes) {
      this.ignoredCollectionOrderInFieldsMatchingRegexes = regexes;
      return this;
    }

    /**
     * Registers the given {@link Comparator} to compare the fields with the given type.
     * <p>
     * Comparators registered with this method have less precedence than comparators registered with {@link #withComparatorForFields(Comparator, String...)}
     * or BiPredicate registered with  {@link #withEqualsForFields(BiPredicate, String...)}.
     * <p>
     * Note that registering a {@link Comparator} for a given type will override the previously registered BiPredicate/Comparator (if any).
     * <p>
     * See {@link RecursiveComparisonAssert#withComparatorForType(Comparator, Class)} for examples.
     *
     * @param <T> the class type to register a comparator for
     * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given field
     * @param type the type to be compared with the given comparator.
     * @return this builder.
     * @throws NullPointerException if the given Comparator is null.
     */
    public <T> Builder withComparatorForType(Comparator<? super T> comparator, Class<T> type) {
      requireNonNull(comparator, "Expecting a non null Comparator");
      this.typeComparators.registerComparator(type, comparator);
      return this;
    }

    /**
     * Registers the given {@link BiPredicate} to compare the fields with the given type.
     * <p>
     * BiPredicates registered with this method have less precedence than the ones registered with {@link #withEqualsForFields(BiPredicate, String...)}
     * or the comparators registered with {@link #withComparatorForFields(Comparator, String...)}.
     * <p>
     * Note that registering a {@link BiPredicate} for a given type will override the previously registered BiPredicate/Comparator (if any).
     * <p>
     * See {@link RecursiveComparisonAssert#withEqualsForType(BiPredicate, Class)} for examples.
     *
     * @param <T> the class type to register a BiPredicate for
     * @param equals the {@link BiPredicate} to use to compare the given field
     * @param type the type to be compared with the given comparator.
     * @return this builder.
     * @throws NullPointerException if the given BiPredicate is null.
     * @since 3.17.0
     */
    @SuppressWarnings("unchecked")
    public <T> Builder withEqualsForType(BiPredicate<? super T, ? super T> equals, Class<T> type) {
      return withComparatorForType(toComparator(equals), type);
    }

    /**
     * Registers the given {@link Comparator} to compare the fields at the given locations.
     * <p>
     * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
     * one can register a comparator for Foo and Bar's {@code id} by calling:
     * <pre><code class='java'> registerComparatorForFields(idComparator, "foo.id", "foo.bar.id")</code></pre>
     * <p>
     * Comparators registered with this method have precedence over comparators registered with {@link #withComparatorForType(Comparator, Class)}
     * or BiPredicate registered with {@link #withEqualsForType(BiPredicate, Class)}.
     * <p>
     * Note that registering a {@link Comparator} for a given field will override the previously registered BiPredicate/Comparator (if any).
     * <p>
     * See {@link RecursiveComparisonAssert#withComparatorForFields(Comparator, String...) RecursiveComparisonAssert#withComparatorForFields(Comparator comparator, String...fields)} for examples.
     *
     * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given field
     * @param fields the fields the comparator should be used for
     * @return this builder.
     * @throws NullPointerException if the given Comparator is null.
     */
    public Builder withComparatorForFields(Comparator<?> comparator, String... fields) {
      requireNonNull(comparator, "Expecting a non null Comparator");
      Stream.of(fields).forEach(fieldLocation -> fieldComparators.registerComparator(fieldLocation, comparator));
      return this;
    }

    /**
     * Registers the given {@link BiPredicate} to compare the fields at the given locations.
     * <p>
     * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
     * one can register a BiPredicate for Foo and Bar's {@code id} by calling:
     * <pre><code class='java'> withEqualsForFields(idBiPredicate, "foo.id", "foo.bar.id")</code></pre>
     * <p>
     * BiPredicates registered with this method have precedence over the ones registered with {@link #withEqualsForType(BiPredicate, Class)}
     * or the comparators registered with {@link #withComparatorForType(Comparator, Class)}.
     * <p>
     * Note that registering a {@link BiPredicate} for a given field will override the previously registered BiPredicate/Comparator (if any).
     * <p>
     * See {@link RecursiveComparisonAssert#withEqualsForFields(BiPredicate, String...) RecursiveComparisonAssert#withEqualsForFields(BiPredicate equals, String...fields)} for examples.
     *
     * @param equals the {@link BiPredicate} to use to compare the given fields
     * @param fields the fields the BiPredicate should be used for
     * @return this builder.
     * @throws NullPointerException if the given BiPredicate is null.
     * @since 3.17.0
     */
    public Builder withEqualsForFields(BiPredicate<?, ?> equals, String... fields) {
      return withComparatorForFields(toComparator(equals), fields);
    }

    /**
     * Allows to register a {@link BiPredicate} to compare fields whose location matches the given regexes.
     * A typical usage is to compare double/float fields with a given precision.
     * <p>
     * The fields are evaluated from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
     * one can register a BiPredicate for Foo and Bar's {@code id} by calling:
     * <pre><code class='java'> withEqualsForFields(idBiPredicate, ".*id")</code></pre>
     * or
     * <pre><code class='java'> withEqualsForFields(idBiPredicate, "foo.*id")</code></pre>
     * <p>
     * BiPredicates registered with this method have precedence over the ones registered with {@link #registerEqualsForType(BiPredicate, Class)}
     * or the comparators registered with {@link #registerComparatorForType(Comparator, Class)} but don't have precedence over the
     * ones registered with exact location match: {@link #registerEqualsForFields(BiPredicate, String...)} or {@link #registerComparatorForFields(Comparator, String...)}
     * <p>
     * If registered regexes for different {@link BiPredicate} match a given field, the latest registered regexes {@link BiPredicate} wins.
     * <p>
     * See {@link RecursiveComparisonAssert#withEqualsForFieldsMatchingRegexes(BiPredicate, String...) RecursiveComparisonAssert#withEqualsForFieldsMatchingRegexes(BiPredicate equals, String...fields)} for examples.
     *
     * @param equals the {@link BiPredicate} to use to compare the fields matching the given regexes
     * @param regexes the regexes to match fields against
     * @return this builder.
     * @throws NullPointerException if the given BiPredicate is null.
     * @since 3.24.0
     */
    public Builder withEqualsForFieldsMatchingRegexes(BiPredicate<?, ?> equals, String... regexes) {
      fieldComparators.registerComparatorForFieldsMatchingRegexes(regexes, toComparator(equals));
      return this;
    }

    /**
     * Registers the giving message which would be shown when differences in the given fields while comparison occurred.
     * <p>
     * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both
     * have an {@code id} field, one can register a message for Foo and Bar's {@code id} by calling:
     * <pre><code class='java'> withErrorMessageForFields("some message", "foo.id", "foo.bar.id")</code></pre>
     * <p>
     * Messages registered with this method have precedence over the ones registered with {@link #withErrorMessageForType(String, Class)}.
     * <p>
     * In case of {@code null} as message the default error message will be used (See
     * {@link ComparisonDifference#DEFAULT_TEMPLATE}).
     *
     * @param message the error message that will be thrown when comparison error occurred.
     * @param fields the fields the error message should be used for.
     * @return this builder.
     * @throws NullPointerException if the giving list of arguments is null.
     */
    public Builder withErrorMessageForFields(String message, String... fields) {
      Stream.of(fields).forEach(fieldLocation -> fieldMessages.registerMessage(fieldLocation, message));
      return this;
    }

    /**
     * Registers the giving message which would be shown when differences for the giving type while comparison
     * occurred.
     * <p>
     * Message registered with this method have less precedence than the ones registered with {@link #withErrorMessageForFields(String, String...)}.
     * <p>
     * In case of {@code null} as message the default error message will be used (See
     * {@link ComparisonDifference#DEFAULT_TEMPLATE}).
     *
     * @param message the error message that will be thrown when comparison error occurred
     * @param type the type the error message should be used for
     * @return this builder
     */
    public Builder withErrorMessageForType(String message, Class<?> type) {
      this.typeMessages.registerMessage(type, message);
      return this;
    }

    @Override
    public Builder withIgnoredFields(String... fieldsToIgnore) {
      return super.withIgnoredFields(fieldsToIgnore);
    }

    @Override
    public Builder withIgnoredFieldsMatchingRegexes(String... regexes) {
      return super.withIgnoredFieldsMatchingRegexes(regexes);
    }

    @Override
    public Builder withIgnoredFieldsOfTypes(Class<?>... types) {
      return super.withIgnoredFieldsOfTypes(types);
    }

    /**
     * Defines how objects are introspected in the recursive comparison.
     * <p>
     * Default to {@link DefaultRecursiveComparisonIntrospectionStrategy}.
     *
     * @param introspectionStrategy the {@link RecursiveComparisonIntrospectionStrategy} to use
     * @return This builder.
     */
    public RecursiveComparisonConfiguration.Builder withIntrospectionStrategy(RecursiveComparisonIntrospectionStrategy introspectionStrategy) {
      this.introspectionStrategy = introspectionStrategy;
      return this;
    }

    public RecursiveComparisonConfiguration build() {
      return new RecursiveComparisonConfiguration(this);
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked", "ComparatorMethodParameterNotUsed" })
  private static Comparator toComparator(BiPredicate equals) {
    requireNonNull(equals, "Expecting a non null BiPredicate");
    return (o1, o2) -> equals.test(o1, o2) ? 0 : 1;
  }

}
