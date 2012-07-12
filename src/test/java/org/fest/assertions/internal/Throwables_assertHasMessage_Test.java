/*
 * Created on Dec 26, 2010
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
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldHaveMessage.shouldHaveMessage;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;

/**
 * Tests for <code>{@link Throwables#assertHasMessage(AssertionInfo, Throwable, String)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Throwables_assertHasMessage_Test {

  private static Throwable actual;

  @BeforeClass
  public static void setUpOnce() {
    actual = new NullPointerException("Throwable message");
  }

  @Rule
  public ExpectedException thrown = none();

  private Failures failures;
  private Throwables throwables;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    throwables = new Throwables();
    throwables.failures = failures;
  }

  @Test
  public void should_pass_if_actual_has_expected_message() {
    throwables.assertHasMessage(someInfo(), actual, "Throwable message");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    throwables.assertHasMessage(someInfo(), null, "Throwable message");
  }

  @Test
  public void should_fail_if_actual_has_not_expected_message() {
    AssertionInfo info = someInfo();
    try {
      throwables.assertHasMessage(info, actual, "expected message");
      fail("AssertionError expected");
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveMessage(actual, "expected message"));
    }
  }
}
