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
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

class ShouldContainAnyOf_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainAnyOf(list("Yoda", "Han", "Han"), list("Vador", "Leia"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\", \"Han\", \"Han\"]%n" +
                                   "to contain at least one of the following elements:%n" +
                                   "  [\"Vador\", \"Leia\"]%n" +
                                   "but none were found"));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainAnyOf(list("Yoda", "Han", "Han"), list("Vador", "Leia"),
                                                     new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\", \"Han\", \"Han\"]%n" +
                                   "to contain at least one of the following elements:%n" +
                                   "  [\"Vador\", \"Leia\"]%n" +
                                   "but none were found " +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }

}
