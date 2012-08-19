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
package org.fest.assertions.internal.objects;

import static org.fest.assertions.error.ShouldBeExactlyInstanceOf.shouldBeExactlyInstance;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Objects;
import org.fest.assertions.internal.ObjectsBaseTest;

import org.junit.Test;

/**
 * Tests for <code>{@link Objects#assertIsExactlyInstanceOf(AssertionInfo, Object, Class)}</code>.
 * 
 * @author Joel Costigliola
 * @author Nicolas François
 */
public class Objects_assertIsExactlyInstanceOf_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_if_actual_is_exactly_instance_of_type() {
    objects.assertIsExactlyInstanceOf(someInfo(), "Yoda", String.class);
  }

  @Test
  public void should_throw_error_if_type_is_null() {
    thrown.expectNullPointerException("The given type should not be null");
    objects.assertIsExactlyInstanceOf(someInfo(), "Yoda", null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    objects.assertIsExactlyInstanceOf(someInfo(), null, String.class);
  }

  @Test
  public void should_fail_if_actual_is_not_exactly_instance_of_type() {
    AssertionInfo info = someInfo();
    try {
      objects.assertIsExactlyInstanceOf(info, "Yoda", Object.class);
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeExactlyInstance("Yoda", Object.class));
    }
  }
}
