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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

  /**
   * Creates an error for methods with incorrect modifiers.
   *
   * @param actual the actual class
   * @param declared whether declared methods are checked
   * @param expected the expected methods
   * @param modifier the expected modifier
   * @param nonMatching the methods with incorrect modifiers
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldHaveMethods(Class<?> actual, boolean declared, SortedSet<String> expected,
                                                      String modifier, Map<String, String> nonMatching) {
    return shouldHaveMethods(actual, declared, expected, modifier, Collections.emptySortedSet(), nonMatching);
  }

  /// Creates an error for expected methods that are either missing or that do not have the expected modifier.
  ///
  /// The "could not find" and "not <modifier>" sections are only included when they are not empty.
  ///
  /// @param actual the actual class
  /// @param declared whether declared methods are checked
  /// @param expected the expected methods
  /// @param modifier the expected modifier
  /// @param missing the expected methods that could not be found
  /// @param nonMatching the expected methods found without the expected modifier, mapped to their actual modifiers
  /// @return the error message factory
  public static ErrorMessageFactory shouldHaveMethods(Class<?> actual, boolean declared, SortedSet<String> expected,
                                                      String modifier, SortedSet<String> missing,
                                                      Map<String, String> nonMatching) {
    return new ShouldHaveMethods(actual, expected, modifier, missing, nonMatching, declared);
  }

  /**
   * Creates an error for methods unexpectedly having a modifier.
   *
   * @param actual the actual class
   * @param modifier the prohibited modifier
   * @param declared whether declared methods are checked
   * @param actualMethodsHavingModifier the offending methods
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldNotHaveMethods(Class<?> actual, String modifier, boolean declared,
                                                         SortedSet<String> actualMethodsHavingModifier) {
    return new ShouldHaveMethods(actual, modifier, declared, actualMethodsHavingModifier);
  }

  /**
   * Creates an error for unexpectedly present methods.
   *
   * @param actual the actual class
   * @param declared whether declared methods are checked
   * @param actualMethodsHavingModifier the offending methods
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldNotHaveMethods(Class<?> actual, boolean declared,
                                                         SortedSet<String> actualMethodsHavingModifier) {
    return new ShouldHaveMethods(actual, null, declared, actualMethodsHavingModifier);
  }

  private ShouldHaveMethods(Class<?> actual, Set<String> expected, Set<String> missing, boolean declared) {
    super("%n" +
          "Expecting%n" +
          "  %s%n" +
          "to have " + (declared ? "declared " : "") + "methods:%n" +
          "  %s%n" +
          "but could not find:%n" +
          "  %s", actual, expected, missing);
  }

  private ShouldHaveMethods(Class<?> actual, Set<String> expected, String modifier, Set<String> missing,
                            Map<String, String> nonMatching, boolean declared) {
    super(modifierMessageFormat(declared, modifier, missing, nonMatching),
          modifierMessageArguments(actual, expected, missing, nonMatching));
  }

  private static String modifierMessageFormat(boolean declared, String modifier, Set<String> missing,
                                              Map<String, String> nonMatching) {
    StringBuilder format = new StringBuilder("%n" +
                                             "Expecting%n" +
                                             "  %s%n" +
                                             "to have " + (declared ? "declared " : "") + modifier + " methods:%n" +
                                             "  %s");
    if (!missing.isEmpty()) format.append("%n" +
                                          "but could not find:%n" +
                                          "  %s");
    if (!nonMatching.isEmpty()) format.append("%n" +
                                              (missing.isEmpty() ? "but" : "and") + " the following are not " + modifier + ":%n" +
                                              "  %s");
    return format.toString();
  }

  private static Object[] modifierMessageArguments(Class<?> actual, Set<String> expected, Set<String> missing,
                                                   Map<String, String> nonMatching) {
    List<Object> arguments = new ArrayList<>(List.of(actual, expected));
    if (!missing.isEmpty()) arguments.add(missing);
    if (!nonMatching.isEmpty()) arguments.add(nonMatching);
    return arguments.toArray();
  }

  private ShouldHaveMethods(Class<?> actual, String modifier, boolean declared,
                            Set<String> actualMethodsHavingModifier) {
    super("%n" +
          "Expecting%n" +
          "  %s%n" +
          "not to have any " + (declared ? "declared " : "")
          + (modifier != null && !modifier.isEmpty() ? modifier + " " : "") + "methods but it has the following:%n" +
          "  %s", actual, actualMethodsHavingModifier);
  }
}
