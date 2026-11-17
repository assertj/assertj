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
import static org.assertj.core.error.ShouldBeInSameYear.shouldBeInSameYear;

import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeInSameYear;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.tests.core.testkit.Dates;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldBeInSameYear#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Joel Costigliola
 */
class ShouldBeInSameYear_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeInSameYear(Dates.parseDate("2010-01-01"), Dates.parseDate("2011-01-01"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2010-01-01T00:00:00.000 (java.util.Date)%n" +
                                   "to be on same year as:%n" +
                                   "  2011-01-01T00:00:00.000 (java.util.Date)"));
  }

}
