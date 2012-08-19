/*
 * Created on Sep 17, 2010
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

import static org.fest.assertions.error.ShouldNotBeSame.shouldNotBeSame;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Objects;
import org.fest.assertions.internal.ObjectsBaseTest;
import org.fest.test.Person;

/**
 * Tests for <code>{@link Objects#assertNotSame(AssertionInfo, Object, Object)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Objects_assertNotSame_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_if_objects_are_not_same() {
    objects.assertNotSame(someInfo(), "Yoda", "Luke");
  }

  @Test
  public void should_fail_if_objects_are_same() {
    AssertionInfo info = someInfo();
    Object actual = new Person("Yoda");
    try {
      objects.assertNotSame(info, actual, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeSame(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
