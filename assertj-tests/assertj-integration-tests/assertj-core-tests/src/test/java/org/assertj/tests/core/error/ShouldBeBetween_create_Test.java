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
package org.assertj.tests.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeBetween;
import org.assertj.tests.core.testkit.Dates;
import org.assertj.tests.core.testkit.NeverEqualComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeBetween#create(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Joel Costigliola
 */
class ShouldBeBetween_create_Test {

  @Test
  void should_create_error_message_with_period_boundaries_included() {
    // GIVEN
    ErrorMessageFactory factory = ShouldBeBetween.shouldBeBetween(Dates.parseDate("2010-01-01"), Dates.parseDate("2011-01-01"),
                                                                  Dates.parseDate("2012-01-01"), true,
                                                                  true);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2010-01-01T00:00:00.000 (java.util.Date)%n" +
                                   "to be in period:%n" +
                                   "  [2011-01-01T00:00:00.000 (java.util.Date), 2012-01-01T00:00:00.000 (java.util.Date)]%n"));
  }

  @Test
  void should_create_error_message_with_period_lower_boundary_included() {
    // GIVEN
    ErrorMessageFactory factory = ShouldBeBetween.shouldBeBetween(Dates.parseDate("2010-01-01"), Dates.parseDate("2011-01-01"),
                                                                  Dates.parseDate("2012-01-01"), true,
                                                                  false);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2010-01-01T00:00:00.000 (java.util.Date)%n" +
                                   "to be in period:%n" +
                                   "  [2011-01-01T00:00:00.000 (java.util.Date), 2012-01-01T00:00:00.000 (java.util.Date)[%n"));
  }

  @Test
  void should_create_error_message_with_period_upper_boundary_included() {
    // GIVEN
    ErrorMessageFactory factory = ShouldBeBetween.shouldBeBetween(Dates.parseDate("2010-01-01"), Dates.parseDate("2011-01-01"),
                                                                  Dates.parseDate("2012-01-01"),
                                                                  false, true);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2010-01-01T00:00:00.000 (java.util.Date)%n" +
                                   "to be in period:%n" +
                                   "  ]2011-01-01T00:00:00.000 (java.util.Date), 2012-01-01T00:00:00.000 (java.util.Date)]%n"));
  }

  @Test
  void should_create_error_message_with_period_boundaries_excluded() {
    // GIVEN
    ErrorMessageFactory factory = ShouldBeBetween.shouldBeBetween(Dates.parseDate("2010-01-01"), Dates.parseDate("2011-01-01"),
                                                                  Dates.parseDate("2012-01-01"),
                                                                  false, false);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2010-01-01T00:00:00.000 (java.util.Date)%n" +
                                   "to be in period:%n" +
                                   "  ]2011-01-01T00:00:00.000 (java.util.Date), 2012-01-01T00:00:00.000 (java.util.Date)[%n"));
  }

  @Test
  void should_create_error_message_with_comparison_strategy() {
    // GIVEN
    ComparatorBasedComparisonStrategy comparisonStrategy = new ComparatorBasedComparisonStrategy(NeverEqualComparator.NEVER_EQUALS);
    ErrorMessageFactory factory = ShouldBeBetween.shouldBeBetween(Dates.parseDate("2010-01-01"), Dates.parseDate("2011-01-01"),
                                                                  Dates.parseDate("2012-01-01"),
                                                                  false, false,
                                                                  comparisonStrategy);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo("[Test] %n" +
                            "Expecting actual:%n" +
                            "  2010-01-01T00:00:00.000 (java.util.Date)%n" +
                            "to be in period:%n" +
                            "  ]2011-01-01T00:00:00.000 (java.util.Date), 2012-01-01T00:00:00.000 (java.util.Date)[%n" +
                            "when comparing values using '%s'",
                            NeverEqualComparator.NEVER_EQUALS.description());
  }

}
