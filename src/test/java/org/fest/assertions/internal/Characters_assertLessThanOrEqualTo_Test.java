/*
 * Created on Oct 24, 2010
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

import static org.fest.assertions.error.IsNotLessThanOrEqualTo.isNotLessThanOrEqualTo;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Characters#assertLessThanOrEqualTo(AssertionInfo, Character, char)}</code>.
 *
 * @author Alex Ruiz
 */
public class Characters_assertLessThanOrEqualTo_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Characters characters;

  @Before public void setUp() {
    failures = spy(Failures.instance());
    characters = new Characters();
    characters.failures = failures;
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    characters.assertLessThanOrEqualTo(someInfo(), null, 'a');
  }

  @Test public void should_pass_if_actual_is_less_than_other() {
    characters.assertLessThanOrEqualTo(someInfo(), 'b', 'a');
  }

  @Test public void should_pass_if_actual_is_equal_to_other() {
    characters.assertLessThanOrEqualTo(someInfo(), 'b', 'b');
  }

  @Test public void should_fail_if_actual_is_greater_than_other() {
    AssertionInfo info = someInfo();
    try {
      characters.assertLessThanOrEqualTo(info, 'b', 'a');
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isNotLessThanOrEqualTo('b', 'a'));
  }
}
