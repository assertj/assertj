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

/**
 * Creates an error message indicating that an assertion that verifies that a class have methods.
 */
public class ShouldHaveMethods extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link org.assertj.core.error.ShouldHaveMethods}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected expected methods for this class
   * @param missing missing methods for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveMethods(Class<?> actual, Set<String> expected, Set<String> missing) {
    return new ShouldHaveMethods(actual, expected, missing, false);
  }

  public static ErrorMessageFactory shouldHaveMethods(Class<?> actual, Set<String> expected, String modifier, Map<String,String> nonMatching) {
    return new ShouldHaveMethods(actual, expected, modifier, nonMatching, false);
  }

  /**
   * Creates a new </code>{@link org.assertj.core.error.ShouldHaveMethods}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected expected methods for this class
   * @param missing missing methods for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveDeclaredMethods(Class<?> actual, Set<String> expected, Set<String> missing) {
    return new ShouldHaveMethods(actual, expected, missing, true);
  }

  private ShouldHaveMethods(Class<?> actual, Set<String> expected, Set<String> missing, boolean declared) {
    super("%nExpecting%n  <%s>%nto have " + (declared ? "declared " : "")
      + "methods:%n  <%s>%nbut it doesn't have:%n  <%s>", actual, expected, missing);
  }

  private ShouldHaveMethods(Class<?> actual, Set<String> expected, String modifier, Map<String, String> nonMatching, boolean declared) {
    super("%nExpecting%n  <%s>%nto have " + (declared ? "declared " : "") +  modifier + " "
      + "methods:%n  <%s>%nbut the following are not " +modifier + ":%n  <%s>", actual, expected, nonMatching);
  }
}

