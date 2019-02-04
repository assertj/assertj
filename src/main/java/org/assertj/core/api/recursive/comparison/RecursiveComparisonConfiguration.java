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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Strings.join;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.VisibleForTesting;

// TODO registered comparators vs ignored overridden equals test
@Beta
public class RecursiveComparisonConfiguration {

  public static final String INDENT_LEVEL_2 = "  -";
  private boolean strictTypeChecking = false;

  // fields to ignore section
  private boolean ignoreAllActualNullFields = false;
  private Set<FieldLocation> ignoredFields = new LinkedHashSet<>();
  private List<Pattern> ignoredFieldsRegexes = new ArrayList<>();

  // overridden equals method to ignore section
  private List<Class<?>> ignoredOverriddenEqualsForTypes = new ArrayList<>();
  private List<FieldLocation> ignoredOverriddenEqualsForFields = new ArrayList<>();
  private List<Pattern> ignoredOverriddenEqualsRegexes = new ArrayList<>();
  private boolean ignoreAllOverriddenEquals = false;

  // registered comparators section
  private TypeComparators typeComparators = defaultTypeComparators();
  private FieldComparators fieldComparators = new FieldComparators();

  public boolean hasComparatorForField(String fieldName) {
    return fieldComparators.hasComparatorForField(new FieldLocation(fieldName));
  }

  public Comparator<?> getComparatorForField(String fieldName) {
    return fieldComparators.getComparatorForField(new FieldLocation(fieldName));
  }

  public boolean hasComparatorForType(Class<?> keyType) {
    return typeComparators.hasComparatorForType(keyType);
  }

  public boolean hasNoCustomComparators() {
    return false; // TODO fail one test
  }

  public Comparator<?> getComparatorForType(Class<?> fieldType) {
    return typeComparators.get(fieldType);
  }

  @VisibleForTesting
  TypeComparators getTypeComparators() {
    return typeComparators;
  }

  Stream<Entry<Class<?>, Comparator<?>>> comparatorByTypes() {
    return typeComparators.comparatorByTypes();
  }

  @VisibleForTesting
  boolean getIgnoreAllActualNullFields() {
    return ignoreAllActualNullFields;
  }

  /**
   * Sets whether actual null fields are ignored in the recursive comparison.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringActualNullFields()} for code examples.
   *
   * @param ignoreAllActualNullFields whether to ingore actual null fields in the recursive comparison
   */
  public void setIgnoreAllActualNullFields(boolean ignoreAllActualNullFields) {
    this.ignoreAllActualNullFields = ignoreAllActualNullFields;
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
   * Returns the list of the object under test fields to ignore in the recursive comparison.
   *
   * @return the list of the object under test fields to ignore in the recursive comparison.
   */
  public Set<FieldLocation> getIgnoredFields() {
    return ignoredFields;
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
    ignoredOverriddenEqualsRegexes.addAll(Stream.of(regexes)
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

  /**
   * Registers the given {@link Comparator} to compare the fields with the given type.
   * <p>
   * Comparators specified by this method have less precedence than comparators added with {@link #registerComparatorForField(Comparator, FieldLocation)}.
   * <p>
   * See {@link RecursiveComparisonAssert#withComparatorForType(Comparator, Class)} for examples.
   *
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given field
   * @param type the type to be compared with the given comparator.
   */
  public <T> void registerComparatorForType(Comparator<? super T> comparator, Class<T> type) {
    typeComparators.put(type, comparator);
  }

  /**
   * Registers the given {@link Comparator} to compare the fields with the given locations.
   * <p>
   * The field locations must be specified from the root object,
   * for example if {@code Foo} has a {@code Bar} field which has an {@code id}, one can register to a comparator for Bar's {@code id} by calling:
   * <pre><code class='java'> registerComparatorForField(new FieldLocation("bar.id"), idComparator)</code></pre>
   * <p>
   * Comparators specified by this method have precedence over comparators added with {@link #registerComparatorForType(Comparator, Class)}.
   * <p>
   * See {@link RecursiveComparisonAssert#withComparatorForFields(Comparator, String...) RecursiveComparisonAssert#withComparatorForFields(Comparator, String...)} for examples.
   *
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given field
   * @param fieldLocation the location from the root object of the field the comparator should be used for
   */
  public void registerComparatorForField(Comparator<?> comparator, FieldLocation fieldLocation) {
    fieldComparators.registerComparator(fieldLocation, comparator);
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

  public List<Pattern> getIgnoredOverriddenEqualsRegexes() {
    return ignoredOverriddenEqualsRegexes;
  }

  public Stream<Entry<FieldLocation, Comparator<?>>> comparatorByFields() {
    return fieldComparators.comparatorByFields();
  }

  @Override
  public String toString() {
    return multiLineDescription(CONFIGURATION_PROVIDER.representation());
  }

  public String multiLineDescription(Representation representation) {
    StringBuilder description = new StringBuilder();
    describeIgnoreAllActualNullFields(description);
    describeIgnoredFields(description);
    describeIgnoredFieldsRegexes(description);
    describeOverriddenEqualsMethodsUsage(description, representation);
    describeRegisteredComparatorByTypes(description);
    describeRegisteredComparatorForFields(description);
    describeTypeCheckingStrictness(description);
    return description.toString();
  }

  // non public stuff

  boolean shouldIgnore(DualKey dualKey) {
    return matchesAnIgnoredNullField(dualKey)
           || matchesAnIgnoredField(dualKey)
           || matchesAnIgnoredFieldRegex(dualKey);
  }

  Predicate<String> shouldKeepField(String parentConcatenatedPath) {
    return fieldName -> shouldKeepField(parentConcatenatedPath, fieldName);
  }

  private boolean shouldKeepField(String parentPath, String fieldName) {
    String fieldConcatenatedPath = concatenatedPath(parentPath, fieldName);
    return !matchesAnIgnoredField(fieldConcatenatedPath) && !matchesAnIgnoredFieldRegex(fieldConcatenatedPath);
  }

  // TODO move somewhere else ?
  private static String concatenatedPath(String parentPath, String name) {
    return parentPath.isEmpty() ? name : format("%s.%s", parentPath, name);
  }

  boolean shouldIgnoreOverriddenEqualsOf(DualKey dualKey) {
    if (dualKey.isJavaType()) return false; // we must compare basic types otherwise the recursive comparison loops infinitely!
    return ignoreAllOverriddenEquals
           || matchesAnIgnoredOverriddenEqualsField(dualKey)
           || shouldIgnoreOverriddenEqualsOf(dualKey.key1.getClass());
  }

  @VisibleForTesting
  boolean shouldIgnoreOverriddenEqualsOf(Class<? extends Object> clazz) {
    return matchesAnIgnoredOverriddenEqualsRegex(clazz) || matchesAnIgnoredOverriddenEqualsType(clazz);
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

  private void describeIgnoreAllActualNullFields(StringBuilder description) {
    if (ignoreAllActualNullFields) description.append(format("- all actual null fields were ignored in the comparison%n"));
  }

  private void describeOverriddenEqualsMethodsUsage(StringBuilder description, Representation representation) {
    boolean isConfiguredToIgnoreSomeOverriddenEqualsMethods = isConfiguredToIgnoreSomeOverriddenEqualsMethods();
    String header = ignoreAllOverriddenEquals
        ? "- no overridden equals methods were used in the comparison except for java types"
        : "- overridden equals methods were used in the comparison";
    description.append(header);
    if (isConfiguredToIgnoreSomeOverriddenEqualsMethods) {
      description.append(format(ignoreAllOverriddenEquals ? " and:%n" : ", except for:%n"));
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
    if (!ignoredOverriddenEqualsRegexes.isEmpty())
      description.append(format("%s the types matching the following regexes: %s%n", INDENT_LEVEL_2,
                                describeRegexes(ignoredOverriddenEqualsRegexes)));
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

  private boolean matchesAnIgnoredOverriddenEqualsRegex(Class<?> clazz) {
    if (ignoredOverriddenEqualsRegexes.isEmpty()) return false; // shortcut
    String canonicalName = clazz.getCanonicalName();
    return ignoredOverriddenEqualsRegexes.stream()
                                         .anyMatch(regex -> regex.matcher(canonicalName).matches());
  }

  private boolean matchesAnIgnoredOverriddenEqualsType(Class<?> clazz) {
    return ignoredOverriddenEqualsForTypes.contains(clazz);
  }

  private boolean matchesAnIgnoredOverriddenEqualsField(DualKey dualKey) {
    return ignoredOverriddenEqualsForFields.stream()
                                           .anyMatch(fieldLocation -> fieldLocation.matches(dualKey.concatenatedPath));
  }

  private boolean matchesAnIgnoredNullField(DualKey dualKey) {
    return ignoreAllActualNullFields && dualKey.key1 == null;
  }

  private boolean matchesAnIgnoredFieldRegex(String fieldConcatenatedPath) {
    return ignoredFieldsRegexes.stream()
                               .anyMatch(regex -> regex.matcher(fieldConcatenatedPath).matches());
  }

  private boolean matchesAnIgnoredFieldRegex(DualKey dualKey) {
    return matchesAnIgnoredFieldRegex(dualKey.concatenatedPath);
  }

  private boolean matchesAnIgnoredField(DualKey dualKey) {
    return matchesAnIgnoredField(dualKey.concatenatedPath);
  }

  private boolean matchesAnIgnoredField(String fieldConcatenatedPath) {
    return ignoredFields.stream()
                        .anyMatch(fieldLocation -> fieldLocation.matches(fieldConcatenatedPath));
  }

  private String describeIgnoredFields() {
    List<String> fieldsDescription = ignoredFields.stream()
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

  private boolean isConfiguredToIgnoreSomeOverriddenEqualsMethods() {
    return !ignoredOverriddenEqualsRegexes.isEmpty()
           || !ignoredOverriddenEqualsForTypes.isEmpty()
           || !ignoredOverriddenEqualsForFields.isEmpty();
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

}
