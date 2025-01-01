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
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
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
import org.assertj.core.util.VisibleForTesting;

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

  public static final String DEFAULT_TEMPLATE = "%s differ:%n" +
                                                "- actual value  : %s%n" +
                                                "- expected value: %s%s";

  final List<String> decomposedPath;
  final String concatenatedPath;
  final Object actual;
  final Object expected;
  final Optional<String> additionalInformation;
  final String template;

  public ComparisonDifference(DualValue dualValue) {
    this(dualValue.getDecomposedPath(), dualValue.actual, dualValue.expected, null, DEFAULT_TEMPLATE);
  }

  public ComparisonDifference(DualValue dualValue, String additionalInformation) {
    this(dualValue.getDecomposedPath(), dualValue.actual, dualValue.expected, additionalInformation, DEFAULT_TEMPLATE);
  }

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

  public static ComparisonDifference rootComparisonDifference(Object actual, Object other, String additionalInformation) {
    return new ComparisonDifference(rootDualValue(actual, other), additionalInformation);
  }

  public Object getActual() {
    return actual;
  }

  public Object getExpected() {
    return expected;
  }

  public String getTemplate() {
    return template;
  }

  public Optional<String> getAdditionalInformation() {
    return additionalInformation;
  }

  public List<String> getDecomposedPath() {
    return decomposedPath;
  }

  @VisibleForTesting
  public String getConcatenatedPath() {
    return concatenatedPath;
  }

  @Override
  public String toString() {
    return additionalInformation.map(s -> format("ComparisonDifference [path=%s, actual=%s, expected=%s, template=%s, additionalInformation=%s]",
                                                 concatenatedPath, actual, expected, template, s))
                                .orElseGet(() -> format("ComparisonDifference [path=%s, actual=%s, template=%s, expected=%s]",
                                                        concatenatedPath, actual, template, expected));
  }

  public String multiLineDescription() {
    // use the default configured representation
    return multiLineDescription(ConfigurationProvider.CONFIGURATION_PROVIDER.representation());
  }

  public String multiLineDescription(Representation representation) {
    UnambiguousRepresentation unambiguousRepresentation = new UnambiguousRepresentation(representation, actual, expected);
    String additionalInfo = additionalInformation.map(ComparisonDifference::formatOnNewline).orElse("");
    return format(getTemplate(),
                  fieldPathDescription(),
                  unambiguousRepresentation.getActual(),
                  unambiguousRepresentation.getExpected(),
                  additionalInfo);
  }

  // returns a user-friendly path description
  protected String fieldPathDescription() {
    if (concatenatedPath.isEmpty()) return TOP_LEVEL_OBJECTS;
    if (concatenatedPath.matches(TOP_LEVEL_ELEMENT_PATTERN)) return format(TOP_LEVEL_ELEMENTS, extractIndex(concatenatedPath));
    return format(FIELD, concatenatedPath);
  }

  private static String extractIndex(String path) {
    // path looks like [12]
    String index = path.substring(1);
    return index.replaceFirst("]", "");
  }

  private static String formatOnNewline(String info) {
    return format("%n%s", info);
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
