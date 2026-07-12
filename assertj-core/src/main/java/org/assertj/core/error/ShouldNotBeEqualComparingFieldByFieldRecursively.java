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
package org.assertj.core.error;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.presentation.Representation;

/** Creates errors for objects expected not to be recursively equal. */
public class ShouldNotBeEqualComparingFieldByFieldRecursively extends BasicErrorMessageFactory {

  /**
   * Creates an error for recursively equal objects.
   *
   * @param actual the actual object
   * @param other the comparison object
   * @param recursiveComparisonConfiguration the comparison configuration
   * @param representation the value representation
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldNotBeEqualComparingFieldByFieldRecursively(Object actual, Object other,
                                                                                     RecursiveComparisonConfiguration recursiveComparisonConfiguration,
                                                                                     Representation representation) {
    String recursiveComparisonConfigurationDescription = recursiveComparisonConfiguration.multiLineDescription(representation);
    return new ShouldNotBeEqualComparingFieldByFieldRecursively("%n" +
                                                                "Expecting actual:%n" +
                                                                "  %s%n" +
                                                                "not to be equal to:%n" +
                                                                "  %s%n" +
                                                                "when recursively comparing field by field" +
                                                                "%n" +
                                                                "The recursive comparison was performed with this configuration:%n"
                                                                +
                                                                recursiveComparisonConfigurationDescription, // don't use %s
                                                                                                             // to avoid AssertJ
                                                                                                             // formatting String
                                                                                                             // with ""
                                                                actual, other);
  }

  /**
   * Creates an error for identical object references.
   *
   * @param actual the actual object
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldNotBeEqualComparingFieldByFieldRecursively(Object actual) {
    if (actual == null)
      return new ShouldNotBeEqualComparingFieldByFieldRecursively("%n" +
                                                                  "Expecting actual not to be equal to other but both are null.");
    return new ShouldNotBeEqualComparingFieldByFieldRecursively("%n" +
                                                                "Expecting actual not to be equal to other but both refer to the same object (actual == other):%n"
                                                                + "  %s%n", actual);
  }

  private ShouldNotBeEqualComparingFieldByFieldRecursively(String message, Object... arguments) {
    super(message, arguments);
  }

}
