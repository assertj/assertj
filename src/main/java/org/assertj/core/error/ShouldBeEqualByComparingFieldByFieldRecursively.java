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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.util.Strings.join;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.internal.DeepDifference.Difference;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.Objects;

public class ShouldBeEqualByComparingFieldByFieldRecursively extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldBeEqualByComparingFieldByFieldRecursive(Object actual, Object other,
                                                                                  List<Difference> differences,
                                                                                  Representation representation) {
    List<String> descriptionOfDifferences = new ArrayList<>(differences.size());
    for (Difference difference : differences) {
      descriptionOfDifferences.add(describeDifference(difference, representation));
    }
    return new ShouldBeEqualByComparingFieldByFieldRecursively("%n" +
                                                               "Expecting:%n" +
                                                               "  <%s>%n" +
                                                               "to be equal to:%n" +
                                                               "  <%s>%n" +
                                                               "when recursively comparing field by field, but found the following difference(s):%n"
                                                               + join(descriptionOfDifferences).with(format("%n")),
                                                               actual, other);
  }

  private ShouldBeEqualByComparingFieldByFieldRecursively(String message, Object actual, Object other) {
    super(message, actual, other);
  }

  private static String describeDifference(Difference difference, Representation representation) {

    String actualFieldValue = representation.toStringOf(difference.getActual());
    String otherFieldValue = representation.toStringOf(difference.getOther());

    boolean sameRepresentation = Objects.areEqual(actualFieldValue, otherFieldValue);

    String actualFieldValueRepresentation = sameRepresentation
        ? representation.unambiguousToStringOf(difference.getActual())
        : actualFieldValue;

    String otherFieldValueRepresentation = sameRepresentation
        ? representation.unambiguousToStringOf(difference.getOther())
        : otherFieldValue;

    return format("%nPath to difference: <%s>%n" +
                  "- expected: <%s>%n" +
                  "- actual  : <%s>",
                  join(difference.getPath()).with("."),
                  otherFieldValueRepresentation.replace("%", "%%"),
                  actualFieldValueRepresentation.replace("%", "%%"));
  }

}
