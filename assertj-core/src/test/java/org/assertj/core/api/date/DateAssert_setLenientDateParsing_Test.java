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
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Date;

import org.assertj.core.api.DateAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests the lenient mode of date parsing used in date assertions with date represented as {@link String}.
 *
 * @author Michal Kordas
 */
class DateAssert_setLenientDateParsing_Test extends DateAssertBaseTest {

  @Test
  void should_fail_if_given_date_string_representation_cant_be_parsed() {
    final String dateAsString = "2001/02/03";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new Date()).isEqualTo(dateAsString));
  }

  @Test
  void should_fail_if_date_can_be_parsed_leniently_but_lenient_mode_is_disabled() {
    final Date date = parse("2001-02-03");
    try {
      assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(date).isEqualTo("2001-01-34"))
                                                     .withMessageContaining("Failed to parse");
    } finally {}
  }

}
