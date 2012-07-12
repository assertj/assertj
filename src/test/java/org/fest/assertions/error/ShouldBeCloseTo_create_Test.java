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

import static org.fest.assertions.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.fest.util.Dates.ISO_DATE_TIME_FORMAT_WITH_MS;

import java.text.ParseException;

import org.junit.Test;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;

/**
 * Tests for <code>{@link ShouldBeCloseTo#create(Description)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeCloseTo_create_Test {

  @Test
  public void should_create_error_message_with_period_boundaries_included() throws ParseException {
    ErrorMessageFactory factory = shouldBeCloseTo(ISO_DATE_TIME_FORMAT_WITH_MS.parse("2011-01-01T00:00:00.000"),
        ISO_DATE_TIME_FORMAT_WITH_MS.parse("2011-01-01T00:00:00.101"), 100, 101);
    String message = factory.create(new TextDescription("Test"));
    assertEquals(
        "[Test] expected '2011-01-01T00:00:00.000' to be close to '2011-01-01T00:00:00.101' by less than 100ms but difference was of 101ms",
        message);
  }

}
