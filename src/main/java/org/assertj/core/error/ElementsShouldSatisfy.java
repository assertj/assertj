/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

public class ElementsShouldSatisfy extends BasicErrorMessageFactory {

  public static <T> ErrorMessageFactory elementsShouldSatisfy(Object actual, T elementNotSatisfyingRestrictions,
                                                              String assertionErrorDetails) {
    return new ElementsShouldSatisfy(actual, elementNotSatisfyingRestrictions, assertionErrorDetails);
  }

  public static <T> ErrorMessageFactory elementsShouldSatisfyAny(Object actual) {
    return new ElementsShouldSatisfy(actual);
  }

  private ElementsShouldSatisfy(Object actual, Object notSatisfies, String assertionErrorDetails) {
    super("%n" +
          "Expecting all elements of:%n" +
          "  <%s>%n" +
          "to satisfy given requirements, but this element did not:%n" +
          "  <%s> %n" +
          "Details: %s",
          actual, notSatisfies, assertionErrorDetails);
  }

  private ElementsShouldSatisfy(Object actual) {
    super("%n" +
          "Expecting any element of:%n" +
          "  <%s>%n" +
          "to satisfy the given assertions requirements but none did.",
          actual);
  }

}
