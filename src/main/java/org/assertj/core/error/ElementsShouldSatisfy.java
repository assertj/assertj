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
import static java.util.stream.Collectors.joining;
import static org.assertj.core.util.Strings.escapePercent;

import java.util.List;

import org.assertj.core.api.AssertionInfo;

public class ElementsShouldSatisfy extends BasicErrorMessageFactory {

  public static ErrorMessageFactory elementsShouldSatisfyAny(Object actual,
                                                             List<UnsatisfiedRequirement> elementsNotSatisfyingRequirements,
                                                             AssertionInfo info) {
    return new ElementsShouldSatisfy("%n" +
                                     "Expecting any element of:%n" +
                                     "  <%s>%n" +
                                     "to satisfy the given assertions requirements but none did:%n%n",
                                     actual, elementsNotSatisfyingRequirements, info);
  }

  public static ErrorMessageFactory elementsShouldSatisfy(Object actual,
                                                          List<UnsatisfiedRequirement> elementsNotSatisfyingRestrictions,
                                                          AssertionInfo info) {
    return new ElementsShouldSatisfy("%n" +
                                     "Expecting all elements of:%n" +
                                     "  <%s>%n" +
                                     "to satisfy given requirements, but these elements did not:%n%n",
                                     actual, elementsNotSatisfyingRestrictions, info);
  }

  private ElementsShouldSatisfy(String message, Object actual, List<UnsatisfiedRequirement> elementsNotSatisfyingRequirements,
                                AssertionInfo info) {
    super(message + describeErrors(elementsNotSatisfyingRequirements, info), actual);
  }

  private static String describeErrors(List<UnsatisfiedRequirement> elementsNotSatisfyingRequirements, AssertionInfo info) {
    return escapePercent(elementsNotSatisfyingRequirements.stream()
                                                          .map(unsatisfiedRequirement -> unsatisfiedRequirement.describe(info))
                                                          .collect(joining(format("%n%n"))));
  }

  public static UnsatisfiedRequirement unsatisfiedRequirement(Object elementNotSatisfyingRequirements, String errorMessage) {
    return new ElementsShouldSatisfy.UnsatisfiedRequirement(elementNotSatisfyingRequirements, errorMessage);
  }

  public static class UnsatisfiedRequirement {

    private final Object elementNotSatisfyingRequirements;
    private final String errorMessage;

    public UnsatisfiedRequirement(Object elementNotSatisfyingRequirements, String errorMessage) {
      this.elementNotSatisfyingRequirements = elementNotSatisfyingRequirements;
      this.errorMessage = errorMessage;
    }

    public String describe(AssertionInfo info) {
      return format("  <%s> error: %s", info.representation().toStringOf(elementNotSatisfyingRequirements), errorMessage);
    }

    @Override
    public String toString() {
      return format("  <%s> %s", elementNotSatisfyingRequirements, errorMessage);
    }
  }

}
