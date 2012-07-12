/*
 * Created on Sep 26, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;

import static org.fest.assertions.error.ShouldBeInSameMillisecond.shouldBeInSameMillisecond;

import java.text.*;
import java.util.Date;

import org.junit.Test;

import org.fest.assertions.description.*;

/**
 * Tests for <code>{@link ShouldBeInSameMillisecond#create(Description)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeInSameMillisecond_create_Test {

  @Test
  public void should_create_error_message() throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss:SS");
    Date date1 = dateFormat.parse("1994-08-26T22:35:17:29");
    Date date2 = dateFormat.parse("1994-08-26T22:35:17:30");
    String message = shouldBeInSameMillisecond(date1, date2).create(new TextDescription("Test"));
    assertEquals(
        "[Test] expected <1994-08-26T22:35:17:29> to be on same year, month, day, hour, minute, second and millisecond as <1994-08-26T22:35:17:30>",
        message);
  }

}
