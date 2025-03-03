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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.DateUtil.parse;

import java.time.LocalDate;
import java.util.Date;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveDateField_create_Test {

  @Test
  void should_create_error_message_for_date() {
    // GIVEN
    Date date = parse("2015-12-31");
    ErrorMessageFactory factory = shouldHaveDateField(date, "month", 10);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2015-12-31T00:00:00.000 (java.util.Date)%n" +
                                   "to be on \"month\" 10"));
  }

  @Test
  void should_create_error_message_for_local_date() {
    // GIVEN
    LocalDate date = LocalDate.of(2015, 12, 31);
    ErrorMessageFactory factory = shouldHaveDateField(date, "year", 2000);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2015-12-31 (java.time.LocalDate)%n" +
                                   "to be on \"year\" 2000"));
  }

}
