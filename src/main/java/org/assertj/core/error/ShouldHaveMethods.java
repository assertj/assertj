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

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Creates an error message indicating that an assertion that verifies that a class have methods.
 */
public class ShouldHaveMethods extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link org.assertj.core.error.ShouldHaveMethods}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param declared {@code true} if the expected methods are declared ones, {@code false} otherwise.
   * @param expected expected methods for this class
   * @param missing missing methods for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveMethods(Class<?> actual, boolean declared, SortedSet<String> expected,
                                                      SortedSet<String> missing) {
    return new ShouldHaveMethods(actual, expected, missing, declared);
  }

  public static ErrorMessageFactory shouldHaveMethods(Class<?> actual, boolean declared, SortedSet<String> expected,
                                                      String modifier, Map<String, String> nonMatching) {
    return new ShouldHaveMethods(actual, expected, modifier, nonMatching, declared);
  }

  public static ErrorMessageFactory shouldNotHaveMethods(Class<?> actual, String modifier, boolean declared,
                                                         SortedSet<String> actualMethodsHavingModifier) {
    return new ShouldHaveMethods(actual, modifier, declared, actualMethodsHavingModifier);
  }

  public static ErrorMessageFactory shouldNotHaveMethods(Class<?> actual, boolean declared,
                                                         SortedSet<String> actualMethodsHavingModifier) {
    return new ShouldHaveMethods(actual, null, declared, actualMethodsHavingModifier);
  }

  private ShouldHaveMethods(Class<?> actual, Set<String> expected, Set<String> missing, boolean declared) {
    super("%n" +
          "Expecting%n" +
          "  <%s>%n" +
          "to have " + (declared ? "declared " : "") + "methods:%n" +
          "  <%s>%n" +
          "but could not find:%n" +
          "  <%s>", actual, expected, missing);
  }

  private ShouldHaveMethods(Class<?> actual, Set<String> expected, String modifier, Map<String, String> nonMatching,
                            boolean declared) {
    super("%n" +
          "Expecting%n" +
          "  <%s>%n" +
          "to have " + (declared ? "declared " : "") + modifier + " " + "methods:%n" +
          "  <%s>%n" +
          "but the following are not " + modifier + ":%n" +
          "  <%s>", actual, expected, nonMatching);
  }

  private ShouldHaveMethods(Class<?> actual, String modifier, boolean declared,
                            Set<String> actualMethodsHavingModifier) {
    super("%n" +
          "Expecting%n" +
          "  <%s>%n" +
          "not to have any " + (declared ? "declared " : "")
          + (modifier != null && modifier.length() > 0 ? modifier + " " : "") + "methods but it has the following:%n" +
          "  <%s>", actual, actualMethodsHavingModifier);
  }
}
