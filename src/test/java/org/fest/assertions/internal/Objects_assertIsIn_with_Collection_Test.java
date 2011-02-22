/*
 * Created on Jan 3, 2010
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

import static java.util.Collections.emptyList;
import static org.fest.assertions.error.ShouldBeIn.shouldBeIn;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Objects#assertIsIn(AssertionInfo, Object, Collection)}</code>.
 *
 * @author Joel Costigliola
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Objects_assertIsIn_with_Collection_Test {

  private static Collection<?> values;

  @BeforeClass public static void setUpOnce() {
    values = list("Yoda", "Leia");
  }

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Objects objects;

  @Before public void setUp() {
    failures = spy(new Failures());
    objects = new Objects();
    objects.failures = failures;
  }

  @Test public void should_throw_error_if_Collection_is_null() {
    thrown.expectNullPointerException(collectionIsNull());
    Collection<?> c = null;
    objects.assertIsIn(someInfo(), "Yoda", c);
  }

  @Test public void should_throw_error_if_Collection_is_empty() {
    thrown.expectIllegalArgumentException(collectionIsEmpty());
    objects.assertIsIn(someInfo(), "Yoda", emptyList());
  }

  @Test public void should_pass_if_actual_is_in_Collection() {
    objects.assertIsIn(someInfo(), "Yoda", values);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    objects.assertIsIn(someInfo(), null, values);
  }

  @Test public void should_fail_if_actual_is_not_in_Collection() {
    AssertionInfo info = someInfo();
    try {
      objects.assertIsIn(info, "Luke", values);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeIn("Luke", values));
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }
}
