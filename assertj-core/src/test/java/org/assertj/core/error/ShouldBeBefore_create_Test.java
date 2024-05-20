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
import static org.assertj.core.error.ShouldBeBefore.shouldBeBefore;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.testkit.NeverEqualComparator.NEVER_EQUALS;
import static org.assertj.core.util.DateUtil.parse;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeBefore#create(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Joel Costigliola
 */
class ShouldBeBefore_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeBefore(parse("2019-01-01"), parse("2012-01-01"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // WHEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2019-01-01T00:00:00.000 (java.util.Date)%n" +
                                   "to be strictly before:%n" +
                                   "  2012-01-01T00:00:00.000 (java.util.Date)%n"));
  }

  @Test
  void should_create_error_message_with_comparison_strategy() {
    // GIVEN
    ComparatorBasedComparisonStrategy comparisonStrategy = new ComparatorBasedComparisonStrategy(NEVER_EQUALS);
    ErrorMessageFactory factory = shouldBeBefore(parse("2019-01-01"), parse("2012-01-01"), comparisonStrategy);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[Test] %n" +
                            "Expecting actual:%n" +
                            "  2019-01-01T00:00:00.000 (java.util.Date)%n" +
                            "to be strictly before:%n" +
                            "  2012-01-01T00:00:00.000 (java.util.Date)%n" +
                            "when comparing values using '%s'",
                            NEVER_EQUALS.description());
  }
}
