/*
 * Created on Dec 24, 2010
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

import static org.fest.assertions.error.DoesNotContainString.doesNotContain;
import static org.fest.assertions.test.ErrorMessages.sequenceToLookForIsNull;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Strings#assertContains(AssertionInfo, String, String)}</code>.
 *
 * @author Alex Ruiz
 */
public class Strings_assertContains_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Strings strings;

  @Before public void setUp() {
    failures = spy(Failures.instance());
    strings = new Strings(failures);
  }

  @Test public void should_fail_if_actual_does_not_contain_sequence() {
    AssertionInfo info = someInfo();
    try {
      strings.assertContains(info, "Yoda", "Luke");
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenSequenceNotFound(info, "Yoda", "Luke");
  }

  @Test public void should_fail_if_actual_contains_sequence_but_in_different_case() {
    AssertionInfo info = someInfo();
    try {
      strings.assertContains(info, "Yoda", "yo");
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenSequenceNotFound(info, "Yoda", "yo");
  }

  private void assertThatFailureWasThrownWhenSequenceNotFound(AssertionInfo info, String actual, String sequence) {
    verify(failures).failure(info, doesNotContain(actual, sequence));
  }

  @Test public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(sequenceToLookForIsNull());
    strings.assertContains(someInfo(), "Yoda", null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    strings.assertContains(someInfo(), null, "Yoda");
  }

  @Test public void should_pass_if_actual_contains_sequence() {
    strings.assertContains(someInfo(), "Yoda", "Yo");
  }
}
