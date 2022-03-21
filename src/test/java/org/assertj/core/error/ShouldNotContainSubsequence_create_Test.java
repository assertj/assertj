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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainSubsequence.shouldNotContainSubsequence;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

class ShouldNotContainSubsequence_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContainSubsequence(list("Yoda", "Luke", "Leia"), list("Luke", "Leia"), 1);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n  [\"Yoda\", \"Luke\", \"Leia\"]%nto not contain subsequence:%n  [\"Luke\", \"Leia\"]%nbut was found starting at index 1%n"));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContainSubsequence(list("Yoda", "LUke", "LeiA"), list("Luke", "Leia"),
                                                              new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance),
                                                              1);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n  [\"Yoda\", \"LUke\", \"LeiA\"]%nto not contain subsequence:%n  [\"Luke\", \"Leia\"]%n"
                                   + "but was found starting at index 1%n"
                                   + "when comparing values using CaseInsensitiveStringComparator"));
  }

}
