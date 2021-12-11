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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.containsIgnoringNewLines;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringWhitespaces;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.internal.util.collections.Sets.newSet;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldContainCharSequence#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class ShouldContainCharSequence_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain("Yoda", "Luke");
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to contain:%n" +
                                   "  \"Luke\" "));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain("Yoda", "Luke",
                                                new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to contain:%n" +
                                   "  \"Luke\" when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_create_error_message_when_ignoring_whitespaces() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainIgnoringWhitespaces("Yoda", "Luke", StandardComparisonStrategy.instance());
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to contain (ignoring whitespaces):%n" +
                                   "  \"Luke\" "));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy_when_ignoring_whitespaces() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainIgnoringWhitespaces("Yoda", "Luke",
                                                                   new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to contain (ignoring whitespaces):%n" +
                                   "  \"Luke\" when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_create_error_message_when_ignoring_case() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainIgnoringCase("Yoda", "Luke");
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to contain:%n" +
                                   "  \"Luke\"%n" +
                                   " (ignoring case)"));
  }

  @Test
  void should_create_error_message_with_several_CharSequence_values() {
    // GIVEN
    CharSequence[] charSequences = array("Luke", "Vador", "Solo");
    ErrorMessageFactory factory = shouldContain("Yoda, Luke", charSequences, newSet("Vador", "Solo"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda, Luke\"%n" +
                                   "to contain:%n" +
                                   "  [\"Luke\", \"Vador\", \"Solo\"]%n" +
                                   "but could not find:%n" +
                                   "  [\"Vador\", \"Solo\"]%n "));
  }

  @Test
  void should_create_error_message_with_several_CharSequence_values_when_ignoring_whitespaces() {
    // GIVEN
    CharSequence[] charSequences = array("Luke", "Vador", "Solo");
    ErrorMessageFactory factory = shouldContainIgnoringWhitespaces("Yoda, Luke", charSequences, newSet("Vador", "Solo"),
                                                                   StandardComparisonStrategy.instance());
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda, Luke\"%n" +
                                   "to contain (ignoring whitespaces):%n" +
                                   "  [\"Luke\", \"Vador\", \"Solo\"]%n" +
                                   "but could not find:%n" +
                                   "  [\"Vador\", \"Solo\"]%n "));
  }

  // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2060
  @Test
  void should_create_error_message_with_custom_comparison_strategy_when_ignoring_new_lines() {
    // GIVEN
    final ErrorMessageFactory factory = containsIgnoringNewLines("Yoda", "Luke", null, null,
                                                                 new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    // WHEN
    final String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to contain (ignoring new lines):%n" +
                                   "  \"Luke\" when comparing values using CaseInsensitiveStringComparator"));
  }

  // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2060
  @Test
  void should_create_error_message_with_several_CharSequence_values_when_ignoring_new_lines() {
    // GIVEN
    final CharSequence[] charSequences = array("Vador", "Luke", "Solo");
    final ErrorMessageFactory factory = containsIgnoringNewLines("Yoda" + System.lineSeparator() + "Luke", null, charSequences,
                                                                 newSet("Vador", "Solo"),
                                                                 StandardComparisonStrategy.instance());
    // WHEN
    final String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda%nLuke\"%n" +
                                   "to contain (ignoring new lines):%n" +
                                   "  [\"Vador\", \"Luke\", \"Solo\"]%n" +
                                   "but could not find:%n" +
                                   "  [\"Vador\", \"Solo\"]%n "));
  }
}
