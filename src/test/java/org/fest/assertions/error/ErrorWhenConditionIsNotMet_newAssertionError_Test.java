/*
 * Created on Sep 30, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;

import org.fest.assertions.core.TestCondition;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.junit.*;

/**
 * Tests for <code>{@link ErrorWhenConditionIsNotMet#newAssertionError(Description)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ErrorWhenConditionIsNotMet_newAssertionError_Test {

  private Description description;
  private ErrorWhenConditionIsNotMet factory;

  @Before public void setUp() {
    description = new TestDescription("Jedi");
    factory = new ErrorWhenConditionIsNotMet("Yoda", new TestCondition<String>("is tall"));
  }

  @Test public void should_create_AssertionError() {
    AssertionError error = factory.newAssertionError(description);
    assertEquals("[Jedi] actual value:<'Yoda'> should satisfy condition:<is tall>", error.getMessage());
  }
}
