/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainSubsequence.actualDoesNotHaveEnoughElementsToContainSubsequence;
import static org.assertj.core.error.ShouldContainSubsequence.shouldContainSubsequence;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldContainSubsequence#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Marcin Mikosik
 */
class ShouldContainSubsequence_create_Test {

  @Test
  void should_create_error_message_when_actual_has_less_elements_then_subsequence() {
    // GIVEN
    ErrorMessageFactory factory = actualDoesNotHaveEnoughElementsToContainSubsequence(list("Yoda"), array("Yoda", "Leia"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual to contain the specified subsequence but actual does not have enough elements to contain it, actual size is 1 when subsequence size is 2%n"
                                   + "actual:%n"
                                   + "  [\"Yoda\"]%n"
                                   + "subsequence:%n"
                                   + "  [\"Yoda\", \"Leia\"]"));
  }

  @Test
  void should_create_error_message_with_first_subsequence_element_not_found_and_its_index() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainSubsequence(list("Yoda", "Luke"), array("Yoda", "Leia"), 1,
                                                           StandardComparisonStrategy.instance());
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual to contain the specified subsequence but failed to find the element at subsequence index 1 in actual:%n"
                                   + "subsequence element not found in actual:%n"
                                   + "  \"Leia\"%n"
                                   + "actual:%n"
                                   + "  [\"Yoda\", \"Luke\"]%n"
                                   + "subsequence:%n"
                                   + "  [\"Yoda\", \"Leia\"]"));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainSubsequence(list("Yoda", "Luke"), array("Yoda", "Leia"), 1,
                                                           new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual to contain the specified subsequence but failed to find the element at subsequence index 1 in actual when comparing elements using CaseInsensitiveStringComparator:%n"
                                   + "subsequence element not found in actual:%n"
                                   + "  \"Leia\"%n"
                                   + "actual:%n"
                                   + "  [\"Yoda\", \"Luke\"]%n"
                                   + "subsequence:%n"
                                   + "  [\"Yoda\", \"Leia\"]"));
  }
}
