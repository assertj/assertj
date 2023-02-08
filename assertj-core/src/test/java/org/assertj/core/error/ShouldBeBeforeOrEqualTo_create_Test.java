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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeBeforeOrEqualTo.shouldBeBeforeOrEqualTo;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.test.NeverEqualComparator.NEVER_EQUALS;
import static org.assertj.core.util.DateUtil.parse;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

class ShouldBeBeforeOrEqualTo_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeBeforeOrEqualTo(parse("2019-01-01"), parse("2012-01-01"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2019-01-01T00:00:00.000 (java.util.Date)%n" +
                                   "to be before or equal to:%n" +
                                   "  2012-01-01T00:00:00.000 (java.util.Date)%n"));
  }

  @Test
  void should_create_error_message_with_comparison_strategy() {
    // GIVEN
    ComparatorBasedComparisonStrategy comparisonStrategy = new ComparatorBasedComparisonStrategy(NEVER_EQUALS);
    ErrorMessageFactory factory = shouldBeBeforeOrEqualTo(parse("2019-01-01"), parse("2012-01-01"), comparisonStrategy);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[Test] %n" +
                            "Expecting actual:%n" +
                            "  2019-01-01T00:00:00.000 (java.util.Date)%n" +
                            "to be before or equal to:%n" +
                            "  2012-01-01T00:00:00.000 (java.util.Date)%n" +
                            "when comparing values using '%s'",
                            NEVER_EQUALS.description());
  }
}
