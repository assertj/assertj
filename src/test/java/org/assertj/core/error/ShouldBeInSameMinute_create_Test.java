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
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.error.ShouldBeInSameMinute.shouldBeInSameMinute;

import java.text.ParseException;

import org.assertj.core.description.*;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeInSameMinute;
import org.assertj.core.util.Dates;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldBeInSameMinute#create(Description)}</code>.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldBeInSameMinute_create_Test {

  @Test
  public void should_create_error_message() throws ParseException {
    ErrorMessageFactory factory = shouldBeInSameMinute(Dates.parseDatetime("2011-01-01T05:01:00"),
        Dates.parseDatetime("2011-01-01T05:02:00"));

    String message = factory.create(new TextDescription("Test"));
    assertEquals(
        "[Test] \nExpecting:\n <2011-01-01T05:01:00>\nto be on same year, month, day, hour and minute as:\n <2011-01-01T05:02:00>", message);
  }

}
