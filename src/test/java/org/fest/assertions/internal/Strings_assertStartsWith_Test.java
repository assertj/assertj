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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.DoesNotStartWith.doesNotStartWith;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Strings#assertStartsWith(AssertionInfo, String, String)}</code>.
 *
 * @author Alex Ruiz
 */
public class Strings_assertStartsWith_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Strings strings;

  @Before public void setUp() {
    failures = spy(Failures.instance());
    strings = new Strings(failures);
  }

  @Test public void should_fail_if_actual_does_not_start_with_prefix() {
    AssertionInfo info = someInfo();
    try {
      strings.assertStartsWith(info, "Yoda", "Luke");
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, doesNotStartWith("Yoda", "Luke"));
  }

  @Test public void should_throw_error_if_prefix_is_null() {
    thrown.expectNullPointerException("The given prefix should not be null");
    strings.assertStartsWith(someInfo(), "Yoda", null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    strings.assertStartsWith(someInfo(), null, "Yoda");
  }

  @Test public void should_pass_if_actual_starts_with_prefix() {
    strings.assertStartsWith(someInfo(), "Yoda", "Yo");
  }
}
