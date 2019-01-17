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
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.annotations.Beta;
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

  // registered comparators section
  private TypeComparators typeComparators = defaultTypeComparators();
  private FieldComparators fieldComparators = new FieldComparators();

  // TODO use FieldLocation instead of String ?
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

  /**
   * Sets whether actual null fields are ignored in the recursive comparison.
   * <p>
   * TODO add a code example.
   *
   * @param ignoreAllActualNullFields
   */
  public void setIgnoreAllActualNullFields(boolean ignoreAllActualNullFields) {
    this.ignoreAllActualNullFields = ignoreAllActualNullFields;
  }

  /**
   * Register the given field paths as to be ignored in the comparison.
   * <p>
   * TODO add a code example.
   *
   * @param fieldPaths the field paths to be ignored in the comparison
   */
  public void ignoreFields(String... fieldPaths) {
    List<FieldLocation> fieldLocations = FieldLocation.from(fieldPaths);
    ignoredFields.addAll(fieldLocations);
  }

  public Set<FieldLocation> getIgnoredFields() {
    return ignoredFields;
  }

  boolean shouldIgnore(DualKey dualKey) {
    return matchesAnIgnoredNullField(dualKey)
           || matchesAnIgnoredField(dualKey)
           || matchesAnIgnoredRegex(dualKey);
  }

  public void ignoreFieldsByRegexes(String... regexes) {
    this.ignoredFieldsRegexes = Stream.of(regexes)
                                      .map(Pattern::compile)
                                      .collect(toList());
  }

  public void ignoreOverriddenEqualsByRegexes(String... regexes) {
    this.ignoredOverriddenEqualsRegexes = Stream.of(regexes)
                                                .map(Pattern::compile)
                                                .collect(toList());
  }

  @VisibleForTesting
  public void ignoreOverriddenEqualsForTypes(Class<?>... types) {
    this.ignoredOverriddenEqualsForTypes = list(types);
  }

  public void ignoreOverriddenEqualsForFields(String... fieldPaths) {
    List<FieldLocation> fieldLocations = FieldLocation.from(fieldPaths);
    this.ignoredOverriddenEqualsForFields.addAll(fieldLocations); // TODO or reset ?
  }

  public <T> void registerComparatorForType(Class<T> type, Comparator<? super T> comparator) {
    this.typeComparators.put(type, comparator);
  }

  public void registerComparatorForField(FieldLocation fieldLocation, Comparator<?> comparator) {
    this.fieldComparators.registerComparator(fieldLocation, comparator);
  }

  public boolean enforceStrictTypeChecking() {
    return strictTypeChecking;
  }

  public void strictTypeChecking(boolean strictTypeChecking) {
    this.strictTypeChecking = strictTypeChecking;
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

  boolean shouldIgnoreOverriddenEqualsOf(DualKey dualKey) {
    return matchesAnIgnoredOverriddenEqualsField(dualKey) || shouldIgnoreOverriddenEqualsOf(dualKey.key1.getClass());
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
    description.append(format("- overridden equals methods were used in the comparison"));
    if (isConfiguredToIgnoreSomeOverriddenEqualsMethods()) {
      description.append(format(", except for:%n"));
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
    if (this.ignoredOverriddenEqualsRegexes.isEmpty()) return false; // shortcut
    String canonicalName = clazz.getCanonicalName();
    return this.ignoredOverriddenEqualsRegexes.stream()
                                              .anyMatch(regex -> regex.matcher(canonicalName).matches());
  }

  private boolean matchesAnIgnoredOverriddenEqualsType(Class<?> clazz) {
    return this.ignoredOverriddenEqualsForTypes.contains(clazz);
  }

  private boolean matchesAnIgnoredOverriddenEqualsField(DualKey dualKey) {
    return ignoredOverriddenEqualsForFields.stream()
                                           .anyMatch(fieldLocation -> fieldLocation.matches(dualKey.concatenatedPath));
  }

  private boolean matchesAnIgnoredNullField(DualKey dualKey) {
    return ignoreAllActualNullFields && dualKey.key1 == null;
  }

  private boolean matchesAnIgnoredRegex(DualKey dualKey) {
    return this.ignoredFieldsRegexes.stream()
                                    .anyMatch(regex -> regex.matcher(dualKey.concatenatedPath).matches());
  }

  private boolean matchesAnIgnoredField(DualKey dualKey) {
    return ignoredFields.stream()
                        .anyMatch(fieldLocation -> fieldLocation.matches(dualKey.concatenatedPath));
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
    typeComparators.registeredComparatorByTypes()
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
    fieldComparators.getFieldComparators()
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
