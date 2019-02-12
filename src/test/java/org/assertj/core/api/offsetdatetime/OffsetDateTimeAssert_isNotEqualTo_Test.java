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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.offsetdatetime;

import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link java.time.OffsetDateTime} are already defined in assertj-core)
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class OffsetDateTimeAssert_isNotEqualTo_Test extends OffsetDateTimeAssertBaseTest {

  @Test
  public void test_isNotEqualTo_assertion() {
    // WHEN
    assertThat(REFERENCE).isNotEqualTo(REFERENCE.plusDays(1));
    assertThat(REFERENCE).isNotEqualTo(REFERENCE.plusDays(1).toString());
    // THEN
    assertThatThrownBy(() -> assertThat(REFERENCE).isNotEqualTo(REFERENCE.toString())).isInstanceOf(AssertionError.class);
  }

  @Test
  public void test_isNotEqualTo_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      String offsetDateTimeAsString = OffsetDateTime.of(2000, 1, 5, 3, 0, 5, 0, UTC).toString();
      assertThat(OffsetDateTime.of(2000, 1, 5, 3, 0, 5, 0, UTC)).isNotEqualTo(offsetDateTimeAsString);
    }).withMessage(format("%nExpecting:%n" +
                          " <2000-01-05T03:00:05Z>%n" +
                          "not to be equal to:%n" +
                          " <2000-01-05T03:00:05Z>%n"));
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(OffsetDateTime.now()).isNotEqualTo((String) null))
                                        .withMessage("The String representing the OffsetDateTime to compare actual with should not be null");
  }

}
