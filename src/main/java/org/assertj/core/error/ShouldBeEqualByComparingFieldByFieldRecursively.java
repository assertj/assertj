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
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Strings.escapePercent;
import static org.assertj.core.util.Strings.join;

import java.util.List;
import java.util.Objects;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.DeepDifference.Difference;
import org.assertj.core.presentation.Representation;

public class ShouldBeEqualByComparingFieldByFieldRecursively extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldBeEqualByComparingFieldByFieldRecursive(Object actual, Object other,
                                                                                  List<Difference> differences,
                                                                                  Representation representation) {
    List<String> descriptionOfDifferences = differences.stream()
                                                       .map(difference -> describeDifference(difference, representation))
                                                       .collect(toList());
    return new ShouldBeEqualByComparingFieldByFieldRecursively("%n" +
                                                               "Expecting:%n" +
                                                               "  <%s>%n" +
                                                               "to be equal to:%n" +
                                                               "  <%s>%n" +
                                                               "when recursively comparing field by field, but found the following difference(s):%n"
                                                               + join(descriptionOfDifferences).with(format("%n")),
                                                               actual, other);
  }

  public static ErrorMessageFactory shouldBeEqualByComparingFieldByFieldRecursively(Object actual, Object other,
                                                                                    List<ComparisonDifference> differences,
                                                                                    RecursiveComparisonConfiguration recursiveComparisonConfiguration,
                                                                                    Representation representation) {
    String differencesDescription = join(differences.stream()
                                                    .map(difference -> difference.multiLineDescription(representation))
                                                    .collect(toList())).with(format("%n%n"));
    String recursiveComparisonConfigurationDescription = recursiveComparisonConfiguration.multiLineDescription(representation);
    String differencesCount = differences.size() == 1 ? "difference:%n" : "%s differences:%n";
    // @format:off
    return new ShouldBeEqualByComparingFieldByFieldRecursively("%n" +
                                                               "Expecting:%n" +
                                                               "  <%s>%n" +
                                                               "to be equal to:%n" +
                                                               "  <%s>%n" +
                                                               "when recursively comparing field by field, but found the following " + differencesCount +
                                                               "%n" +
                                                               escapePercent(differencesDescription) + "%n" +
                                                               "%n"+
                                                               "The recursive comparison was performed with this configuration:%n" +
                                                               recursiveComparisonConfigurationDescription, // don't use %s to avoid AssertJ formatting String with ""
                                                               actual, other, differences.size());
    // @format:on
  }

  private ShouldBeEqualByComparingFieldByFieldRecursively(String message, Object... arguments) {
    super(message, arguments);
  }

  private static String describeDifference(Difference difference, Representation representation) {

    String actualFieldValue = representation.toStringOf(difference.getActual());
    String otherFieldValue = representation.toStringOf(difference.getOther());

    boolean sameRepresentation = Objects.equals(actualFieldValue, otherFieldValue);

    String actualFieldValueRepresentation = sameRepresentation
        ? representation.unambiguousToStringOf(difference.getActual())
        : actualFieldValue;

    String otherFieldValueRepresentation = sameRepresentation
        ? representation.unambiguousToStringOf(difference.getOther())
        : otherFieldValue;

    String additionalInfo = difference.getDescription()
                                      .map(desc -> format("%n- reason  : %s", escapePercent(desc)))
                                      .orElse("");
    return format("%nPath to difference: <%s>%n" +
                  "- actual  : <%s>%n" +
                  "- expected: <%s>" + additionalInfo,
                  join(difference.getPath()).with("."),
                  escapePercent(actualFieldValueRepresentation),
                  escapePercent(otherFieldValueRepresentation));
  }

}
