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

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePrecision.shouldHavePrecision;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

class ShouldHavePrecision_create_Test {

  @Test
  void should_create_error_message_for_big_decimal() {
    // GIVEN
    BigDecimal actual = BigDecimal.TEN;
    // WHEN
    String message = shouldHavePrecision(actual, 5).create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting %s to have a precision of:%n" +
                                   "  5%n" +
                                   "but had a precision of:%n" +
                                   "  %s", actual, actual.precision()));
  }
}
