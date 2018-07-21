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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.util.DateUtil.*;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link DateUtil#parse(String)}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateUtil_parse_date_Test {

  @Test
  public void should_parse_string_with_date_time_format() {
    Date date = parse("1994-08-26");
    assertThat(formatAsDatetime(date)).isEqualTo("1994-08-26T00:00:00");
  }

  @Test
  public void should_return_null_if_string_to_parse_is_null() {
    assertThat(parse(null)).isNull();
  }

  @Test
  public void should_fail_if_string_does_not_respect_date_format() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(parse("invalid date format")).isNull());
  }

}
