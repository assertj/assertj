/*
 * Copyright 2012-2025 the original author or authors.
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

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfy;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyExactly;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

class ElementsShouldSatisfy_create_Test {

  private static final AssertionInfo INFO = someInfo();

  @Test
  void should_create_error_message_all() {
    // GIVEN
    List<UnsatisfiedRequirement> unsatisfiedRequirements = list(new UnsatisfiedRequirement("Leia",
                                                                                           catchAssertionError("Leia mistake.")),
                                                                new UnsatisfiedRequirement("Luke",
                                                                                           catchAssertionError("Luke mistake.")));
    ErrorMessageFactory factory = elementsShouldSatisfy(list("Leia", "Luke", "Yoda"), unsatisfiedRequirements, INFO);
    // WHEN
    String message = factory.create(new TextDescription("Test"), INFO.representation());
    // THEN
    then(message).startsWith(format("[Test] %n" +
                                    "Expecting all elements of:%n" +
                                    "  [\"Leia\", \"Luke\", \"Yoda\"]%n" +
                                    "to satisfy given requirements, but these elements did not:%n%n"))
                 .contains("java.lang.AssertionError: Leia mistake.%n\tat".formatted(),
                           "java.lang.AssertionError: Luke mistake.%n\tat".formatted());
  }

  @Test
  void should_create_error_message_all_and_escape_percent_correctly() {
    // GIVEN
    var unsatisfiedRequirements = list(new UnsatisfiedRequirement("Leia%s", new AssertionError("Leia mistake.")),
                                       new UnsatisfiedRequirement("Luke", new AssertionError("Luke mistake.")));
    ErrorMessageFactory factory = elementsShouldSatisfy(list("Leia%s", "Luke", "Yoda"), unsatisfiedRequirements, INFO);
    // WHEN
    String message = factory.create(new TextDescription("Test"), INFO.representation());
    // THEN
    then(message).startsWith(format("[Test] %n" +
                                    "Expecting all elements of:%n" +
                                    "  [\"Leia%%s\", \"Luke\", \"Yoda\"]%n" +
                                    "to satisfy given requirements, but these elements did not:%n%n" +
                                    "\"Leia%%s\"%n"))
                 .contains("error: java.lang.AssertionError: Leia mistake.%n\tat".formatted())
                 .contains(format("%n" +
                                  "\"Luke\"%n" +
                                  "error: java.lang.AssertionError: Luke mistake.%n\tat"));

  }

  @Test
  void should_create_error_message_any() {
    // GIVEN
    List<UnsatisfiedRequirement> unsatisfiedRequirements = list(new UnsatisfiedRequirement("Leia",
                                                                                           catchAssertionError("Leia mistake.")),
                                                                new UnsatisfiedRequirement("Luke",
                                                                                           catchAssertionError("Luke mistake.")));
    ErrorMessageFactory factory = elementsShouldSatisfyAny(list("Luke", "Yoda"), unsatisfiedRequirements, INFO);
    // WHEN
    String message = factory.create(new TextDescription("Test"), INFO.representation());
    // THEN
    then(message).startsWith(format("[Test] %n" +
                                    "Expecting any element of:%n" +
                                    "  [\"Luke\", \"Yoda\"]%n" +
                                    "to satisfy the given assertions requirements but none did:%n%n"))
                 .contains("java.lang.AssertionError: Leia mistake.%n\tat".formatted(),
                           "java.lang.AssertionError: Luke mistake.%n\tat".formatted());
  }

  @Test
  void should_create_error_message_any_and_escape_percent_correctly() {
    // GIVEN
    var unsatisfiedRequirements = list(new UnsatisfiedRequirement("Leia", new AssertionError("Leia mistake.")),
                                       new UnsatisfiedRequirement("Luke", new AssertionError("Luke mistake.")));
    ErrorMessageFactory factory = elementsShouldSatisfyAny(list("Lu%dke", "Yoda"), unsatisfiedRequirements, INFO);
    // WHEN
    String message = factory.create(new TextDescription("Test"), INFO.representation());
    // THEN
    then(message).startsWith(format("[Test] %n" +
                                    "Expecting any element of:%n" +
                                    "  [\"Lu%%dke\", \"Yoda\"]%n" +
                                    "to satisfy the given assertions requirements but none did:%n%n" +
                                    "\"Leia\"%n"))
                 .contains("error: java.lang.AssertionError: Leia mistake.%n\tat".formatted())
                 .contains(format("%n" +
                                  "\"Luke\"%n" +
                                  "error: java.lang.AssertionError: Luke mistake.%n\tat"));
  }

  @Test
  void should_create_error_SatisfyExactly_message() {
    // GIVEN
    Map<Integer, UnsatisfiedRequirement> unsatisfiedRequirements = newHashMap();
    unsatisfiedRequirements.put(1, new UnsatisfiedRequirement("Leia%", catchAssertionError("Leia mistake.")));
    unsatisfiedRequirements.put(3, new UnsatisfiedRequirement("Luke", catchAssertionError("Luke mistake.")));
    ErrorMessageFactory factory = elementsShouldSatisfyExactly(list("Luke%", "Yoda"), unsatisfiedRequirements, INFO);
    // WHEN
    String message = factory.create(new TextDescription("Test"), INFO.representation());
    // THEN
    then(message).startsWith(format("[Test] %n" +
                                    "Expecting each element of:%n" +
                                    "  [\"Luke%%\", \"Yoda\"]%n" +
                                    "to satisfy the requirements at its index, but these elements did not:%n" +
                                    "%n" +
                                    "\"Leia%%\"%n" +
                                    "- element index: 1%n"))
                 .contains("- error: java.lang.AssertionError: Leia mistake.%n\tat".formatted())
                 .contains(format("%n" +
                                  "\"Luke\"%n" +
                                  "- element index: 3%n" +
                                  "- error: java.lang.AssertionError: Luke mistake.%n\tat"));
  }

  // just to get a stack trace
  private static AssertionError catchAssertionError(String message) {
    try {
      throw new AssertionError(message);
    } catch (AssertionError e) {
      return e;
    }
  }

}
