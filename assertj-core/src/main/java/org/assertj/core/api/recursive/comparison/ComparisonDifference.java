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
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.join;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.recursive.comparison.DualValue.rootDualValue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.internal.UnambiguousRepresentation;
import org.assertj.core.presentation.Representation;

/**
 * Describes a difference found during recursive comparison.
 */
public class ComparisonDifference implements Comparable<ComparisonDifference> {

  // ELEMENT_WITH_INDEX_PATTERN should match [0] or [123] but not name[0] or [0].name, explanation:
  // - ^ represents the start of the string in a regex
  // - \[ represents [ in a regex, need another \ to escape it in a java string
  // - d+ any number
  // - \] represents ] in a regex
  // - $ represents the end of the string in a regex
  private static final String TOP_LEVEL_ELEMENT_PATTERN = "^\\[\\d+]$";
  private static final String FIELD = "field/property '%s'";
  private static final String TOP_LEVEL_OBJECTS = "Top level actual and expected objects";
  private static final String TOP_LEVEL_ELEMENTS = "Top level actual and expected objects element at index %s";

  /** Default template used to describe a comparison difference. */
  public static final String DEFAULT_TEMPLATE = "%s differ:%n" +
                                                "- actual value  : %s%n" +
                                                "- expected value: %s%s";

  final List<String> decomposedPath;
  final String concatenatedPath;
  final Object actual;
  final Object expected;
  final Optional<String> additionalInformation;
  final String template;

  /**
   * Creates a difference from the given dual value.
   *
   * @param dualValue the compared values and their path
   */
  public ComparisonDifference(DualValue dualValue) {
    this(dualValue, null);
  }

  /**
   * Creates a difference with additional information.
   *
   * @param dualValue the compared values and their path
   * @param additionalInformation additional difference information
   */
  public ComparisonDifference(DualValue dualValue, String additionalInformation) {
    this(dualValue, additionalInformation, DEFAULT_TEMPLATE);
  }

  /**
   * Creates a difference with additional information and a custom template.
   *
   * @param dualValue the compared values and their path
   * @param additionalInformation additional difference information
   * @param template the description template
   */
  public ComparisonDifference(DualValue dualValue, String additionalInformation, String template) {
    this(dualValue.getDecomposedPath(), dualValue.actual, dualValue.expected, additionalInformation, template);
  }

  private ComparisonDifference(List<String> decomposedPath, Object actual, Object other, String additionalInformation,
                               String template) {
    this.decomposedPath = unmodifiableList(requireNonNull(decomposedPath, "a path can't be null"));
    this.concatenatedPath = toConcatenatedPath(decomposedPath);
    this.actual = actual;
    this.expected = other;
    this.additionalInformation = Optional.ofNullable(additionalInformation);
    this.template = template != null ? template : DEFAULT_TEMPLATE;
  }

  /**
   * Creates a difference for the root compared values.
   *
   * @param actual the actual value
   * @param other the expected value
   * @param additionalInformation additional difference information
   * @return the root comparison difference
   */
  public static ComparisonDifference rootComparisonDifference(Object actual, Object other, String additionalInformation) {
    return new ComparisonDifference(rootDualValue(actual, other), additionalInformation);
  }

  /**
   * Returns the actual value.
   *
   * @return the actual value
   */
  public Object getActual() {
    return actual;
  }

  /**
   * Returns the expected value.
   *
   * @return the expected value
   */
  public Object getExpected() {
    return expected;
  }

  /**
   * Returns the description template.
   *
   * @return the description template
   */
  public String getTemplate() {
    return template;
  }

  /**
   * Returns additional difference information.
   *
   * @return the additional information
   */
  public Optional<String> getAdditionalInformation() {
    return additionalInformation;
  }

  /**
   * Returns the decomposed field path.
   *
   * @return the decomposed path
   */
  public List<String> getDecomposedPath() {
    return decomposedPath;
  }

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  /**
   * Returns the concatenated field path.
   *
   * @return the concatenated path
   */
  public String getConcatenatedPath() {
    return concatenatedPath;
  }

  @Override
  public String toString() {
    return additionalInformation.map(s -> "ComparisonDifference [path=%s, actual=%s, expected=%s, template=%s, additionalInformation=%s]".formatted(
                                                                                                                                                    concatenatedPath,
                                                                                                                                                    actual,
                                                                                                                                                    expected,
                                                                                                                                                    template,
                                                                                                                                                    s))
                                .orElseGet(() -> "ComparisonDifference [path=%s, actual=%s, template=%s, expected=%s]".formatted(
                                                                                                                                 concatenatedPath,
                                                                                                                                 actual,
                                                                                                                                 template,
                                                                                                                                 expected));
  }

  /**
   * Returns a multi-line description using the configured representation.
   *
   * @return the difference description
   */
  public String multiLineDescription() {
    // use the default configured representation
    return multiLineDescription(ConfigurationProvider.CONFIGURATION_PROVIDER.representation());
  }

  /**
   * Returns a multi-line description using the given representation.
   *
   * @param representation the value representation
   * @return the difference description
   */
  public String multiLineDescription(Representation representation) {
    UnambiguousRepresentation unambiguousRepresentation = new UnambiguousRepresentation(representation, actual, expected);
    String additionalInfo = additionalInformation.map(ComparisonDifference::formatOnNewline).orElse("");
    return getTemplate().formatted(fieldPathDescription(),
                                   unambiguousRepresentation.getActual(),
                                   unambiguousRepresentation.getExpected(),
                                   additionalInfo);
  }

  // returns a user-friendly path description
  /**
   * Returns a user-friendly field path description.
   *
   * @return the field path description
   */
  protected String fieldPathDescription() {
    if (concatenatedPath.isEmpty()) return TOP_LEVEL_OBJECTS;
    return concatenatedPath.matches(TOP_LEVEL_ELEMENT_PATTERN)
        ? TOP_LEVEL_ELEMENTS.formatted(extractIndex(concatenatedPath))
        : FIELD.formatted(concatenatedPath);
  }

  private static String extractIndex(String path) {
    // path looks like [12]
    String index = path.substring(1);
    return index.replaceFirst("]", "");
  }

  private static String formatOnNewline(String info) {
    return "%n%s".formatted(info);
  }

  private static String toConcatenatedPath(List<String> decomposedPath) {
    String concatenatedPath = join(".", decomposedPath);
    // remove the . from array/list index, so person.children.[2].name -> person.children[2].name
    return concatenatedPath.replaceAll("\\.\\[", "[");
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof ComparisonDifference)) {
      return false;
    }
    ComparisonDifference castOther = (ComparisonDifference) other;
    return Objects.equals(concatenatedPath, castOther.concatenatedPath)
           && Objects.equals(actual, castOther.actual)
           && Objects.equals(expected, castOther.expected)
           && Objects.equals(template, castOther.template)
           && Objects.equals(additionalInformation, castOther.additionalInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(concatenatedPath, actual, expected, template, additionalInformation);
  }

  @Override
  public int compareTo(final ComparisonDifference other) {
    // we don't use '.' to join path before comparing them as it would make a.b < aa
    return concat(decomposedPath).compareTo(concat(other.decomposedPath));
  }

  private static String concat(List<String> decomposedPath) {
    return join("", decomposedPath);
  }

}
