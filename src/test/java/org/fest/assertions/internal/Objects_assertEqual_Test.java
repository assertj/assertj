/*
 * Created on Sep 9, 2010
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

import static org.fest.assertions.error.IsNotEqual.isNotEqual;
import static org.fest.assertions.test.ExpectedException.none;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Objects#assertEqual(AssertionInfo, Object, Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class Objects_assertEqual_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Objects objects;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    objects = new Objects(failures);
  }

  @Test public void should_pass_if_objects_are_equal() {
    objects.assertEqual(info, "Yoda", "Yoda");
  }

  @Test public void should_fail_if_objects_are_not_equal() {
    try {
      objects.assertEqual(info, "Luke", "Yoda");
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isNotEqual("Luke", "Yoda"));
  }
}
