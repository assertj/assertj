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
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldNotBeEqual#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ShouldNotBeEqual_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotBeEqual("Yoda", "Luke");
    // WHEN
    String message = factory.create(new TestDescription("Jedi"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Jedi] %nExpecting actual:%n  \"Yoda\"%nnot to be equal to:%n  \"Luke\"%n"));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotBeEqual("Yoda", "Luke",
                                                   new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TestDescription("Jedi"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Jedi] %nExpecting actual:%n  \"Yoda\"%nnot to be equal to:%n  \"Luke\"%nwhen comparing values using CaseInsensitiveStringComparator"));
  }
}
