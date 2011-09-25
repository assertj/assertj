/*
 * Created on Dec 26, 2010
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
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldEndWith.shouldEndWith;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.*;

import org.junit.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;

/**
 * Tests for <code>{@link Strings#assertEndsWith(AssertionInfo, String, String)}</code>.
 *
 * @author Alex Ruiz
 */
public class Strings_assertEndsWith_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Strings strings;

  @Before public void setUp() {
    failures = spy(new Failures());
    strings = new Strings();
    strings.failures = failures;
  }

  @Test public void should_fail_if_actual_does_not_end_with_suffix() {
    AssertionInfo info = someInfo();
    try {
      strings.assertEndsWith(info, "Yoda", "Luke");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith("Yoda", "Luke"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test public void should_throw_error_if_suffix_is_null() {
    thrown.expectNullPointerException("The given suffix should not be null");
    strings.assertEndsWith(someInfo(), "Yoda", null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertEndsWith(someInfo(), null, "Yoda");
  }

  @Test public void should_pass_if_actual_ends_with_suffix() {
    strings.assertEndsWith(someInfo(), "Yoda", "oda");
  }
}
