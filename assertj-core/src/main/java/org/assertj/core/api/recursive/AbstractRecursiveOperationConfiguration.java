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
package org.assertj.core.api.recursive;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Lists.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.assertj.core.util.Strings;

public abstract class AbstractRecursiveOperationConfiguration {

  protected static final String DEFAULT_DELIMITER = ", ";

  private final Set<String> ignoredFields = new LinkedHashSet<>();
  private final List<Pattern> ignoredFieldsRegexes = new ArrayList<>();
  private final Set<Class<?>> ignoredTypes = new LinkedHashSet<>();
  private final List<Pattern> ignoredTypesRegexes = new ArrayList<>();

  protected AbstractRecursiveOperationConfiguration(AbstractBuilder<?> builder) {
    ignoreFields(builder.ignoredFields);
    ignoreFieldsMatchingRegexes(builder.ignoredFieldsMatchingRegexes);
    ignoreFieldsOfTypes(builder.ignoredTypes);
  }

  protected AbstractRecursiveOperationConfiguration() {}

  /**
   * Adds the given fields to the set of fields from the object under test to ignore in the recursive comparison.
   * <p>
   * The fields are ignored by name, not by value.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringFields(String...) RecursiveComparisonAssert#ignoringFields(String...)} for examples.
   *
   * @param fieldsToIgnore the fields of the object under test to ignore in the comparison.
   */
  public void ignoreFields(String... fieldsToIgnore) {
    List<String> fieldLocations = list(fieldsToIgnore);
    ignoredFields.addAll(fieldLocations);
  }

  /**
   * Returns the set of fields from the object under test to ignore in the recursive comparison.
   *
   * @return the set of fields from the object under test to ignore in the recursive comparison.
   */
  public Set<String> getIgnoredFields() {
    return ignoredFields;
  }

  /**
   * Allows to ignore in the recursive comparison the object under test fields matching the given regexes. The given regexes are added to the already registered ones.
   * <p>
   * See {@link RecursiveComparisonAssert#ignoringFieldsMatchingRegexes(String...) RecursiveComparisonAssert#ignoringFieldsMatchingRegexes(String...)} for examples.
   *
   * @param regexes regexes used to ignore fields in the comparison.
   */
  public void ignoreFieldsMatchingRegexes(String... regexes) {
    List<Pattern> patterns = toPatterns(regexes);
    ignoredFieldsRegexes.addAll(patterns);
  }

  public List<Pattern> getIgnoredFieldsRegexes() {
    return ignoredFieldsRegexes;
  }

  /**
   * Makes the recursive assertion to ignore the object under test fields of the given types.
   * The fields are ignored if their types <b>exactly match one of the ignored types</b>, for example if a field is a subtype of an ignored type it is not ignored.
   * <p>
   * If some object under test fields are null it is not possible to evaluate their types and thus these fields are not ignored.
   * <p>
   * Example: see {@link RecursiveComparisonAssert#ignoringFieldsOfTypes(Class[])}.
   *
   * @param types the types of the object under test to ignore in the comparison.
   */
  public void ignoreFieldsOfTypes(Class<?>... types) {
    stream(types).map(AbstractRecursiveOperationConfiguration::asWrapperIfPrimitiveType).forEach(ignoredTypes::add);
  }

  /**
   * Makes the recursive comparison to ignore the fields of the object under test having types matching one of the given regexes.
   * The fields are ignored if their types <b>exactly match one of the regexes</b>, if a field is a subtype of a matched type it is not ignored.
   * <p>
   * One use case of this method is to ignore types that can't be introspected.
   * <p>
   * If {@code strictTypeChecking} mode is enabled and a field of the object under test is null, the recursive
   * comparison evaluates the corresponding expected field's type (if not null), if it is disabled then the field is evaluated as
   * usual (i.e. it is not ignored).
   * <p>
   * <b>Warning</b>: primitive types are not directly supported because under the hood they are converted to their
   * corresponding wrapping types, for example {@code int} to {@code java.lang.Integer}. The preferred way to ignore
   * primitive types is to use {@link #ignoreFieldsOfTypes(Class[])}.
   * Another way is to ignore the wrapping type, for example ignoring {@code java.lang.Integer} ignores both
   * {@code java.lang.Integer} and {@code int} fields.
   * <p>
   * Example: see {@link RecursiveComparisonAssert#ignoringFieldsOfTypesMatchingRegexes(String...)}.
   *
   * @param regexes regexes specifying the types to ignore.
   */
  public void ignoreFieldsOfTypesMatchingRegexes(String... regexes) {
    List<Pattern> patterns = toPatterns(regexes);
    ignoredTypesRegexes.addAll(patterns);
  }

  protected static Class<?> asWrapperIfPrimitiveType(Class<?> type) {
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
   * Returns the set of fields from the object under test types to ignore in the recursive comparison.
   *
   * @return the set of fields from the object under test types to ignore in the recursive comparison.
   */
  public Set<Class<?>> getIgnoredTypes() {
    return ignoredTypes;
  }

  /**
   * Returns the regexes that will be used to ignore fields with types matching these regexes in the recursive comparison.
   *
   * @return the regexes that will be used to ignore fields with types matching these regexes in the recursive comparison.
   */
  public List<Pattern> getIgnoredTypesRegexes() {
    return ignoredTypesRegexes;
  }

  protected void describeIgnoredFields(StringBuilder description) {
    if (!getIgnoredFields().isEmpty())
      description.append(format("- the following fields were ignored in the comparison: %s%n", describeIgnoredFields()));
  }

  protected void describeIgnoredFieldsRegexes(StringBuilder description) {
    if (!getIgnoredFieldsRegexes().isEmpty())
      description.append(format("- the fields matching the following regexes were ignored in the comparison: %s%n",
                                describeRegexes(getIgnoredFieldsRegexes())));
  }

  protected String describeIgnoredTypes() {
    List<String> typesDescription = getIgnoredTypes().stream()
                                                     .map(Class::getName)
                                                     .collect(toList());
    return join(typesDescription);
  }

  protected String describeRegexes(List<Pattern> regexes) {
    List<String> fieldsDescription = regexes.stream()
                                            .map(Pattern::pattern)
                                            .collect(toList());
    return join(fieldsDescription);
  }

  protected static String join(Collection<String> typesDescription) {
    return Strings.join(typesDescription).with(DEFAULT_DELIMITER);
  }

  public boolean matchesAnIgnoredFieldRegex(FieldLocation fieldLocation) {
    // checks parent fields as if a parent field is ignored all subfields (including this field location) should be too.
    return getIgnoredFieldsRegexes().stream().anyMatch(fieldLocation::hierarchyMatchesRegex);
  }

  public boolean matchesAnIgnoredField(FieldLocation fieldLocation) {
    // checks parent fields as if a parent field is ignored all subfields (including this field location) should be too.
    return getIgnoredFields().stream().anyMatch(fieldLocation::hierarchyMatches);
  }

  private String describeIgnoredFields() {
    return join(getIgnoredFields());
  }

  protected static class AbstractBuilder<BUILDER_TYPE extends AbstractBuilder<BUILDER_TYPE>> {
    private final BUILDER_TYPE thisBuilder;

    private String[] ignoredFields = {};
    private String[] ignoredFieldsMatchingRegexes = {};
    private Class<?>[] ignoredTypes = {};

    @SuppressWarnings("unchecked")
    protected AbstractBuilder(Class<? extends AbstractBuilder<BUILDER_TYPE>> selfType) {
      thisBuilder = (BUILDER_TYPE) selfType.cast(this);
    }

    /**
     * Adds the given fields to the set of fields from the object under test to ignore in the recursive comparison. Nested fields can be specified like this: home.address.street.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringFields(String...) RecursiveComparisonAssert#ignoringFields(String...)} for examples.
     *
     * @param fieldsToIgnore the fields of the object under test to ignore in the comparison.
     * @return this builder.
     */
    public BUILDER_TYPE withIgnoredFields(String... fieldsToIgnore) {
      this.ignoredFields = fieldsToIgnore;
      return thisBuilder;
    }

    /**
     * Allows to ignore in the recursive comparison the object under test fields matching the given regexes. The given regexes are added to the already registered ones.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringFieldsMatchingRegexes(String...) RecursiveComparisonAssert#ignoringFieldsMatchingRegexes(String...)} for examples.
     *
     * @param regexes regexes used to ignore fields in the comparison.
     * @return this builder.
     */
    public BUILDER_TYPE withIgnoredFieldsMatchingRegexes(String... regexes) {
      this.ignoredFieldsMatchingRegexes = regexes;
      return thisBuilder;
    }

    /**
     * Adds the given types to the list fields from the object under test types to ignore in the recursive comparison.
     * The fields are ignored if their types exactly match one of the ignored types, if a field is a subtype of an ignored type it won't be ignored.
     * <p>
     * Note that if some object under test fields are null, they are not ignored by this method as their type can't be evaluated.
     * <p>
     * See {@link RecursiveComparisonAssert#ignoringFields(String...) RecursiveComparisonAssert#ignoringFieldsOfTypes(Class...)} for examples.
     *
     * @param types the types of the object under test to ignore in the comparison.
     * @return this builder.
     */
    public BUILDER_TYPE withIgnoredFieldsOfTypes(Class<?>... types) {
      this.ignoredTypes = types;
      return thisBuilder;
    }
  }

  private static List<Pattern> toPatterns(String[] regexes) {
    return Stream.of(regexes)
                 .map(Pattern::compile)
                 .collect(toList());
  }

}
