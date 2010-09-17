/*
 * Created on Sep 17, 2010
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

import static org.fest.assertions.test.Exceptions.assertionFailingOnPurpose;
import static org.fest.assertions.test.ExpectedException.none;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.*;
import org.fest.assertions.error.WhenNotSameErrorFactory;
import org.fest.assertions.test.*;
import org.junit.*;

/**
 * Tests for <code>{@link Objects#assertSame(AssertionInfo, Object, Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class Objects_assertSame_Test {

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

  @Test public void should_pass_if_objects_are_same() {
    Object o = new Object();
    objects.assertSame(info, o, o);
  }

  @Test public void should_fail_if_objects_are_not_same() {
    AssertionError expectedError = assertionFailingOnPurpose();
    Object a = new Person("Yoda");
    Object e = new Person("Yoda");
    when(failures.failure(info, WhenNotSameErrorFactory.errorWhenNotSame(a, e))).thenReturn(expectedError);
    thrown.expect(expectedError);
    objects.assertSame(info, a, e);
  }
}
