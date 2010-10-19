/*
 * Created on Sep 18, 2010
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

import static java.util.Collections.emptyList;
import static org.fest.assertions.error.IsNotNullOrEmpty.isNotNullOrEmpty;
import static org.fest.util.Collections.list;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertNullOrEmpty(AssertionInfo, Collection)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Collections_assertNullOrEmpty_Test {

  private static WritableAssertionInfo info;

  private Failures failures;
  private Collections collections;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    collections = new Collections(failures);
  }

  @Test public void should_pass_if_actual_is_null() {
    collections.assertNullOrEmpty(info, null);
  }

  @Test public void should_pass_if_actual_is_empty() {
    collections.assertNullOrEmpty(info, emptyList());
  }

  @Test public void should_fail_if_actual_has_elements() {
    Collection<String> actual = list("Yoda");
    try {
      collections.assertNullOrEmpty(info, actual);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isNotNullOrEmpty(actual));
  }
}
