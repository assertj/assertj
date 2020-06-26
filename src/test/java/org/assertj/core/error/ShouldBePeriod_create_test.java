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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBePeriod.shouldBeNegative;
import static org.assertj.core.error.ShouldBePeriod.shouldBePositive;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.time.Period;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

/**
 * @author Hayden Meloche
 */
class ShouldBePeriod_create_test {

  @Test
  void should_create_error_message_for_positive() {
    // GIVEN
    ErrorMessageFactory factory = shouldBePositive(Period.ofMonths(-1));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting Period:%n  <P-1M>%nto be positive"));
  }

  @Test
  void should_create_error_message_for_negative() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeNegative(Period.ofMonths(1));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting Period:%n  <P1M>%nto be negative"));
  }
}
