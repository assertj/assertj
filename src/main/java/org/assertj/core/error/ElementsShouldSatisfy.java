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

import static java.util.stream.Collectors.joining;
import static org.assertj.core.util.Strings.escapePercent;

import java.util.List;

public class ElementsShouldSatisfy extends BasicErrorMessageFactory {

  public static ErrorMessageFactory elementsShouldSatisfyAny(Object actual) {
    return new ElementsShouldSatisfy(actual);
  }

  public static ErrorMessageFactory elementsShouldSatisfy(Object actual,
                                                          List<UnsatisfiedRequirement> elementsNotSatisfyingRestrictions) {
    return new ElementsShouldSatisfy(actual, elementsNotSatisfyingRestrictions);
  }

  private ElementsShouldSatisfy(Object actual) {
    super("%n" +
          "Expecting any element of:%n" +
          "  <%s>%n" +
          "to satisfy the given assertions requirements but none did.",
          actual);
  }

  private ElementsShouldSatisfy(Object actual, List<UnsatisfiedRequirement> elementsNotSatisfyingRequirements) {
    super("%n" +
          "Expecting all elements of:%n" +
          "  <%s>%n" +
          "to satisfy given requirements, but these elements did not:%n%n" +
          describeErrors(elementsNotSatisfyingRequirements),
          actual);
  }

  private static String describeErrors(List<UnsatisfiedRequirement> elementsNotSatisfyingRequirements) {
    return escapePercent(elementsNotSatisfyingRequirements.stream()
                                                          .map(UnsatisfiedRequirement::toString)
                                                          .collect(joining(String.format("%n%n"))));
  }

  public static class UnsatisfiedRequirement {

    private final Object elementNotSatisfyingRequirements;
    private final String errorMessage;

    public UnsatisfiedRequirement(Object elementNotSatisfyingRequirements, String errorMessage) {
      this.elementNotSatisfyingRequirements = elementNotSatisfyingRequirements;
      this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
      return String.format("  <%s> %s", elementNotSatisfyingRequirements, errorMessage);
    }
  }

}
