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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldNotBeNull.shouldNotBeNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.junit.*;

/**
 * Tests for <code>{@link Objects#assertNotNull(AssertionInfo, Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class Objects_assertNotNull_Test {

  private Failures failures;
  private Objects objects;

  @Before public void setUp() {
    failures = spy(new Failures());
    objects = new Objects();
    objects.failures = failures;
  }

  @Test public void should_pass_if_object_is_not_null() {
    objects.assertNotNull(someInfo(), "Luke");
  }

  @Test public void should_fail_if_object_is_null() {
    AssertionInfo info = someInfo();
    try {
      objects.assertNotNull(info, null);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeNull());
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }
}
