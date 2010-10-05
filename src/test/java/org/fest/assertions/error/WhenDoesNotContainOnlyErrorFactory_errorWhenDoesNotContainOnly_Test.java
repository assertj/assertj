/*
 * Created on Oct 3, 2010
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
package org.fest.assertions.error;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link WhenDoesNotContainOnlyErrorFactory#errorWhenDoesNotContainOnly(Object, Object, Object, Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class WhenDoesNotContainOnlyErrorFactory_errorWhenDoesNotContainOnly_Test {

  private static Object actual;
  private static Object expected;
  private static Object notFound;
  private static Object notExpected;

  @BeforeClass public static void setUpOnce() {
    actual = new Object();
    expected = new Object();
    notFound = new Object();
    notExpected = new Object();
  }

  @Test public void should_create_new_AssertionErrorFactory() {
    AssertionErrorFactory factory =
        WhenDoesNotContainOnlyErrorFactory.errorWhenDoesNotContainOnly(actual, expected, notFound, notExpected);
    assertEquals(WhenDoesNotContainOnlyErrorFactory.class, factory.getClass());
  }

  @Test public void should_pass_actual_expected_notFound_and_notExpected() {
    WhenDoesNotContainOnlyErrorFactory factory = (WhenDoesNotContainOnlyErrorFactory)
        WhenDoesNotContainOnlyErrorFactory.errorWhenDoesNotContainOnly(actual, expected, notFound, notExpected);
    assertSame(actual, factory.actual);
    assertSame(expected, factory.expected);
    assertSame(notFound, factory.notFound);
    assertSame(notExpected, factory.notExpected);
  }
}
