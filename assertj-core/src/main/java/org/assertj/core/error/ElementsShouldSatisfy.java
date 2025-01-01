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
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.util.Strings.escapePercent;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.assertj.core.api.AssertionInfo;

public class ElementsShouldSatisfy extends BasicErrorMessageFactory {

  public static ErrorMessageFactory elementsShouldSatisfyAny(Object actual,
                                                             List<UnsatisfiedRequirement> elementsNotSatisfyingRequirements,
                                                             AssertionInfo info) {
    return new ElementsShouldSatisfy("%n" +
                                     "Expecting any element of:%n" +
                                     "  %s%n" +
                                     "to satisfy the given assertions requirements but none did:%n%n",
                                     actual, elementsNotSatisfyingRequirements, info);
  }

  public static ErrorMessageFactory elementsShouldSatisfy(Object actual,
                                                          List<UnsatisfiedRequirement> elementsNotSatisfyingRestrictions,
                                                          AssertionInfo info) {
    return new ElementsShouldSatisfy("%n" +
                                     "Expecting all elements of:%n" +
                                     "  %s%n" +
                                     "to satisfy given requirements, but these elements did not:%n%n",
                                     actual, elementsNotSatisfyingRestrictions, info);
  }

  public static ErrorMessageFactory elementsShouldSatisfyExactly(Object actual,
                                                                 Map<Integer, UnsatisfiedRequirement> unsatisfiedRequirements,
                                                                 AssertionInfo info) {
    return new ElementsShouldSatisfy("%n" +
                                     "Expecting each element of:%n" +
                                     "  %s%n" +
                                     "to satisfy the requirements at its index, but these elements did not:%n%n",
                                     actual, unsatisfiedRequirements, info);
  }

  private ElementsShouldSatisfy(String message, Object actual, List<UnsatisfiedRequirement> elementsNotSatisfyingRequirements,
                                AssertionInfo info) {
    super(message + describeErrors(elementsNotSatisfyingRequirements, info), actual);
  }

  private ElementsShouldSatisfy(String message, Object actual, Map<Integer, UnsatisfiedRequirement> unsatisfiedRequirements,
                                AssertionInfo info) {
    super(message + describeErrors(unsatisfiedRequirements, info), actual);
  }

  private static String describeErrors(Map<Integer, UnsatisfiedRequirement> unsatisfiedRequirements, AssertionInfo info) {
    return escapePercent(unsatisfiedRequirements.entrySet().stream()
                                                .map(requirementAtIndex -> describe(requirementAtIndex, info))
                                                .collect(joining(format("%n%n"))));
  }

  private static String describe(Entry<Integer, UnsatisfiedRequirement> requirementsAtIndex, AssertionInfo info) {
    int index = requirementsAtIndex.getKey();
    UnsatisfiedRequirement unsatisfiedRequirement = requirementsAtIndex.getValue();
    return unsatisfiedRequirement.describe(index, info);
  }

  private static String describeErrors(List<UnsatisfiedRequirement> elementsNotSatisfyingRequirements, AssertionInfo info) {
    return escapePercent(elementsNotSatisfyingRequirements.stream()
                                                          .map(unsatisfiedRequirement -> unsatisfiedRequirement.describe(info))
                                                          .collect(joining(format("%n%n"))));
  }

  public static UnsatisfiedRequirement unsatisfiedRequirement(Object elementNotSatisfyingRequirements, String errorMessage) {
    return new UnsatisfiedRequirement(elementNotSatisfyingRequirements, errorMessage);
  }

}
