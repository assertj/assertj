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
package org.fest.assertions.error;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link ErrorWhenGroupDoesNotContainValues#errorWhenDoesNotContain(Object, Object, Object)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ErrorWhenGroupDoesNotContainValues_errorWhenDoesNotContain_Test {

  private static Object actual;
  private static Object expected;
  private static Object notFound;

  @BeforeClass public static void setUpOnce() {
    actual = new Object();
    expected = new Object();
    notFound = new Object();
  }

  @Test public void should_create_new_AssertionErrorFactory() {
    AssertionErrorFactory factory = ErrorWhenGroupDoesNotContainValues.errorWhenDoesNotContain(actual, expected, notFound);
    assertEquals(ErrorWhenGroupDoesNotContainValues.class, factory.getClass());
  }

  @Test public void should_pass_actual_expected_and_notFound() {
    ErrorWhenGroupDoesNotContainValues factory = (ErrorWhenGroupDoesNotContainValues)
        ErrorWhenGroupDoesNotContainValues.errorWhenDoesNotContain(actual, expected, notFound);
    assertSame(actual, factory.actual);
    assertSame(expected, factory.expected);
    assertSame(notFound, factory.notFound);
  }
}
