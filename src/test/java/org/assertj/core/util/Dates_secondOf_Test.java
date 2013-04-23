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

import static org.assertj.core.util.Dates.secondOf;

import static org.junit.Assert.assertEquals;
import static org.junit.rules.ExpectedException.none;

import java.text.*;
import java.util.Date;

import org.junit.*;
import org.junit.rules.ExpectedException;

/**
 * Tests for <code>{@link Dates#secondOf(Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_secondOf_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_return_second_of_date() throws ParseException {
    String dateAsString = "26/08/1994T22:35:17";
    Date date = new SimpleDateFormat("dd/MM/yyyy'T'hh:mm:ss").parse(dateAsString);
    assertEquals(17, secondOf(date));
  }

  @Test
  public void should_throws_NullPointerException_if_date_parameter_is_null() {
    thrown.expect(NullPointerException.class);
    secondOf(null);
  }

}
