/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.util.DateUtil.parse;

import static org.assertj.core.api.Assertions.*;

import java.text.*;

import org.junit.Test;

/**
 * Tests for <code>{@link DateUtil#parse(String)}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateUtil_parse_Test {

  @Test
  public void should_parse_string() throws ParseException {
    String dateAsString = "26/08/1994";
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    assertThat(parse("1994-08-26")).isEqualTo(formatter.parse(dateAsString));
  }

  @Test
  public void should_return_null_if_string_to_parse_is_null() {
    assertThat(parse(null)).isNull();
  }

}
