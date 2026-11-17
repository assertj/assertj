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
import static org.assertj.core.error.ShouldBeAfterOrEqualTo.shouldBeAfterOrEqualTo;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.sql.Timestamp;
import java.time.Instant;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeAfterOrEqualTo;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.tests.core.testkit.Dates;
import org.assertj.tests.core.testkit.NeverEqualComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldBeAfterOrEqualTo#create(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Joel Costigliola
 */
class ShouldBeAfterOrEqualTo_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeAfterOrEqualTo(Dates.parseDate("2011-01-01"), Dates.parseDate("2012-01-01"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2011-01-01T00:00:00.000 (java.util.Date)%n" +
                                   "to be after or equal to:%n" +
                                   "  2012-01-01T00:00:00.000 (java.util.Date)%n"));
  }

  @Test
  void should_create_error_message_with_comparison_strategy() {
    // GIVEN
    ComparatorBasedComparisonStrategy comparisonStrategy = new ComparatorBasedComparisonStrategy(NeverEqualComparator.NEVER_EQUALS);
    ErrorMessageFactory factory = shouldBeAfterOrEqualTo(Dates.parseDate("2011-01-01"), Dates.parseDate("2012-01-01"),
                                                         comparisonStrategy);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[Test] %n" +
                            "Expecting actual:%n" +
                            "  2011-01-01T00:00:00.000 (java.util.Date)%n" +
                            "to be after or equal to:%n" +
                            "  2012-01-01T00:00:00.000 (java.util.Date)%n" +
                            "when comparing values using '%s'",
                            NeverEqualComparator.NEVER_EQUALS.description());
  }

  @Test
  void should_create_error_message_showing_Timestamp_nanos_so_values_are_distinguishable() {
    // GIVEN — same millisecond, different nanos (typical Instant.now() vs currentTimeMillis case)
    Instant withMicros = Instant.parse("2011-01-01T00:00:00.123456789Z");
    Timestamp actualMillisOnly = new Timestamp(withMicros.toEpochMilli());
    Timestamp otherWithNanos = Timestamp.from(withMicros);
    ErrorMessageFactory factory = shouldBeAfterOrEqualTo(actualMillisOnly, otherWithNanos);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN — both would look identical at ms precision; nanos must appear so the assertionError is understandable
    then(STANDARD_REPRESENTATION.toStringOf(actualMillisOnly)).doesNotContain("123456");
    then(STANDARD_REPRESENTATION.toStringOf(otherWithNanos)).contains("123456");
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  %s%n" +
                                   "to be after or equal to:%n" +
                                   "  %s%n",
                                   STANDARD_REPRESENTATION.toStringOf(actualMillisOnly),
                                   STANDARD_REPRESENTATION.toStringOf(otherWithNanos)));
  }
}
