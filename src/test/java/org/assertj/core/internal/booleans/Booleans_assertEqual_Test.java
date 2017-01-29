/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.booleans;

import static java.lang.Boolean.TRUE;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Booleans;
import org.assertj.core.internal.BooleansBaseTest;
import org.junit.Test;


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
      verify(failures).failure(info, shouldBeEqual(TRUE, expected, info.representation()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
