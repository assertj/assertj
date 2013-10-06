/*
 * Created on Sep 26, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldBeInSameMinuteWindow.shouldBeInSameMinuteWindow;
import static org.assertj.core.util.Dates.parseDatetime;

import java.text.ParseException;

import org.junit.Test;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;


/**
 * Tests for <code>{@link ShouldBeInSameMinuteWindow#create(Description)}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldBeInSameMinuteWindow_create_Test {

  @Test
  public void should_create_error_message() throws ParseException {
    ErrorMessageFactory factory = shouldBeInSameMinuteWindow(parseDatetime("2011-01-01T05:00:00"),
                                                             parseDatetime("2011-01-01T05:02:01"));

    String message = factory.create(new TextDescription("Test"));
    assertThat(message).isEqualTo("[Test] \nExpecting:\n  <2011-01-01T05:00:00>\nto be close to:\n  " +
                                    "<2011-01-01T05:02:01>\n" +
                                    "by less than one minute (strictly) but difference was: 2m and 1s");
  }

}
