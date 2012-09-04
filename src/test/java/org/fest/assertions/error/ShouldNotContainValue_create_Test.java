/*
 * Created on Jun 3, 2012
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.data.MapEntry.entry;
import static org.fest.assertions.error.ShouldNotContainValue.shouldNotContainValue;
import static org.fest.assertions.test.Maps.mapOf;

import java.util.Map;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldNotContainKey#create(Description)}</code>.
 * 
 * @author Nicolas François
 */
public class ShouldNotContainValue_create_Test {

  @Test
  public void should_create_error_message() {
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldNotContainValue(map, "green");
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:\n" + "<{'name'='Yoda', 'color'='green'}>\n" + " not to contain value:\n" + "<'green'>",
        message);
  }

}
