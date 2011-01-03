/*
 * Created on Sep 17, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import static java.util.Collections.emptySet;
import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.error.DoesNotContainOnly.doesNotContainOnly;
import static org.fest.util.Collections.*;

import java.util.List;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link DoesNotContainOnly#create(Description)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class DoesNotContainOnly_create_Test {

  private Description description;
  private List<String> actual;
  private ErrorMessage errorMessage;

  @Before public void setUp() {
    description = new TestDescription("Test");
    actual = list("Yoda", "Han");
  }

  @Test public void should_create_default_error_message_if_notFound_and_notExpected_are_not_empty() {
    errorMessage = doesNotContainOnly(actual, list("Luke", "Yoda"), set("Han"), set("Luke"));
    String msg = "[Test] expected:<['Yoda', 'Han']> to contain only:<['Luke', 'Yoda']>; could not find:<['Luke']> and got unexpected:<['Han']>";
    assertEquals(msg, errorMessage.create(description));
  }

  @Test public void should_ignore_empty_notFound() {
    errorMessage = doesNotContainOnly(actual, list("Yoda"), set("Han"), emptySet());
    String msg = "[Test] expected:<['Yoda', 'Han']> to contain only:<['Yoda']>, but got unexpected:<['Han']>";
    assertEquals(msg, errorMessage.create(description));
  }

  @Test public void should_ignore_empty_notExpected() {
    errorMessage = doesNotContainOnly(actual, list("Luke", "Yoda", "Han"), emptySet(), set("Luke"));
    String msg = "[Test] expected:<['Yoda', 'Han']> to contain only:<['Luke', 'Yoda', 'Han']>, but could not find:<['Luke']>";
    assertEquals(msg, errorMessage.create(description));
  }
}
