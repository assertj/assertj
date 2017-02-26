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
package org.assertj.core.internal.objects;

import static org.assertj.core.error.ShouldHaveToString.shouldHaveToString;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Person;
import org.junit.Before;
import org.junit.Test;

public class Objects_assertHasToString_Test extends ObjectsBaseTest {

  private Person actual;

  @Before
  @Override
  public void setUp() {
    super.setUp();
    actual = mock(Person.class);
    when(actual.toString()).thenReturn("foo");
  }

  @Test
  public void should_pass_if_actual_toString_is_the_expected_String() {
    objects.assertHasToString(someInfo(), actual, "foo");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    objects.assertHasToString(someInfo(), null, "foo");
  }

  @Test
  public void should_fail_if_actual_toString_is_not_the_expected_String() {
    AssertionInfo info = someInfo();
    try {
      objects.assertHasToString(info, actual, "bar");
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveToString(actual, "bar"));
    }
  }
}
