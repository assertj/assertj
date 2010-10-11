/*
 * Created on Sep 16, 2010
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

import static org.fest.assertions.error.ErrorWhenObjectsAreNotEqual.errorWhenNotEqual;
import static org.fest.assertions.test.Exceptions.assertionFailingOnPurpose;
import static org.fest.assertions.test.ExpectedException.none;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Objects#assertNull(AssertionInfo, Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class Objects_assertNull_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Objects objects;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = mock(Failures.class);
    objects = new Objects(failures);
  }

  @Test public void should_pass_if_object_is_null() {
    objects.assertNull(info, null);
  }

  @Test public void should_fail_if_object_is_not_null() {
    AssertionError expectedError = assertionFailingOnPurpose();
    Object o = new Object();
    when(failures.failure(info, errorWhenNotEqual(o, null))).thenReturn(expectedError);
    thrown.expect(expectedError);
    objects.assertNull(info, o);
  }
}
