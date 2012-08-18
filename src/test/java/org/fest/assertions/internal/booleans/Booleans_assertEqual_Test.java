/*
 * Created on Oct 22, 2010
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
package org.fest.assertions.internal.booleans;

import static java.lang.Boolean.TRUE;

import static org.fest.assertions.error.ShouldBeEqual.shouldBeEqual;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Booleans;
import org.fest.assertions.internal.BooleansBaseTest;

/**
 * Tests for <code>{@link Booleans#assertEqual(AssertionInfo, Boolean, boolean)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Booleans_assertEqual_Test extends BooleansBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    booleans.assertEqual(someInfo(), null, true);
  }

  @Test
  public void should_pass_if_booleans_are_equal() {
    booleans.assertEqual(someInfo(), TRUE, true);
  }

  @Test
  public void should_fail_if_booleans_are_not_equal() {
    AssertionInfo info = someInfo();
    boolean expected = false;
    try {
      booleans.assertEqual(info, TRUE, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(TRUE, expected));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
