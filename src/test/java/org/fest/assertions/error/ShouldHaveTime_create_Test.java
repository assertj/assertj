/*
 * Created on Jul 20, 2012
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
import static org.fest.assertions.error.ShouldHaveTime.shouldHaveTime;
import static org.fest.util.Dates.ISO_DATE_TIME_FORMAT;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldHaveTime#create(Description)}</code>.
 * 
 * @author Guillaume Girou
 * @author Nicolas François
 */
public class ShouldHaveTime_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() throws Exception {
    factory = shouldHaveTime(ISO_DATE_TIME_FORMAT.parse("2011-01-01T05:01:00"), Long.MIN_VALUE, Long.MAX_VALUE);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expected <2011-01-01T05:01:00> to have time:<9223372036854775807L> but was:<-9223372036854775808L>", message);
  }
}
