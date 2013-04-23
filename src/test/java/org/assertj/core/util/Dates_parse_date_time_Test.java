/*
 * Created on Sep 23, 2006
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2006-2011 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.util.Dates.*;

import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.none;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for <code>{@link Dates#parseDatetime(String)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_parse_date_time_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_parse_string_with_date_time_format() {
    Date date = parseDatetime("1994-08-26T00:00:00");
    assertEquals("1994-08-26T00:00:00", formatAsDatetime(date));
  }

  @Test
  public void should_return_null_if_string_to_parse_is_null() {
    assertNull(parseDatetime(null));
  }

  @Test
  public void should_fail_if_string_does_not_respect_date_format() {
    thrown.expect(RuntimeException.class);
    assertNull(parseDatetime("invalid date format"));
  }

}
