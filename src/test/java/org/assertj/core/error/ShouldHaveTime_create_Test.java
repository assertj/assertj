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
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.error.ShouldHaveTime.shouldHaveTime;

import java.text.ParseException;
import java.util.Date;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ShouldHaveTime;
import org.assertj.core.util.Dates;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldHaveTime#create(Description)}</code>.
 * 
 * @author Guillaume Girou
 * @author Nicolas François
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldHaveTime_create_Test {

  @Test
  public void should_create_error_message() throws ParseException {
    Date date = Dates.parseDatetime("2011-01-01T05:01:00");
    String message = shouldHaveTime(date, 123).create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting\n <2011-01-01T05:01:00>\nto have time:\n <123L>\nbut was:\n <" + date.getTime() + "L>", message);
  }
}
