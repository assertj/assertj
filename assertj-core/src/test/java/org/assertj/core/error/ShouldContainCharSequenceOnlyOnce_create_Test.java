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
import static org.assertj.core.error.ShouldContainCharSequenceOnlyOnce.shouldContainOnlyOnce;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShouldContainCharSequenceOnlyOnce_create_Test {

  private ErrorMessageFactory factoryWithSeveralOccurrences;
  private ErrorMessageFactory factoryWithNoOccurrence;

  @BeforeEach
  public void setUp() {
    factoryWithSeveralOccurrences = shouldContainOnlyOnce("aaamotifmotifaabbbmotifaaa", "motif", 3);
    factoryWithNoOccurrence = shouldContainOnlyOnce("aaamodifmoifaabbbmotfaaa", "motif", 0);
  }

  @Test
  void should_create_error_message_when_string_to_search_appears_several_times() {
    // WHEN
    String message = factoryWithSeveralOccurrences.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n  \"motif\"%nto appear only once in:%n  \"aaamotifmotifaabbbmotifaaa\"%nbut it appeared 3 times "));
  }

  @Test
  void should_create_error_message_when_string_to_search_does_not_appear() {
    // WHEN
    String message = factoryWithNoOccurrence.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n  \"motif\"%nto appear only once in:%n  \"aaamodifmoifaabbbmotfaaa\"%nbut it did not appear "));
  }

  @Test
  void should_create_error_message_when_string_to_search_does_not_appear_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnlyOnce("aaamoDifmoifaabbbmotfaaa", "MOtif", 0,
                                                        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n  \"MOtif\"%nto appear only once in:%n  \"aaamoDifmoifaabbbmotfaaa\"%nbut it did not appear when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_create_error_message_when_string_to_search_appears_several_times_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnlyOnce("aaamotIFmoTifaabbbmotifaaa", "MOtif", 3,
                                                        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n  \"MOtif\"%nto appear only once in:%n  \"aaamotIFmoTifaabbbmotifaaa\"%nbut it appeared 3 times when comparing values using CaseInsensitiveStringComparator"));
  }

}
