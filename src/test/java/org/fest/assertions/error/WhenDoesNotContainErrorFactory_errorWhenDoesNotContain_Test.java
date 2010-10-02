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

import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.list;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link WhenDoesNotContainErrorFactory#errorWhenDoesNotContain(Object, Object, Object)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class WhenDoesNotContainErrorFactory_errorWhenDoesNotContain_Test {

  private static List<String> actual;
  private static Object[] expected;
  private static Object[] notFound;

  @BeforeClass public static void setUpOnce() {
    actual = list("Yoda");
    expected = array("Luke", "Yoda");
    notFound = array("Luke");
  }

  @Test public void should_create_new_AssertionErrorFactory() {
    AssertionErrorFactory factory = WhenDoesNotContainErrorFactory.errorWhenDoesNotContain(actual, expected, notFound);
    assertEquals(WhenDoesNotContainErrorFactory.class, factory.getClass());
  }

  @Test public void should_pass_actual_expected_and_notFound() {
    WhenDoesNotContainErrorFactory factory = (WhenDoesNotContainErrorFactory)
        WhenDoesNotContainErrorFactory.errorWhenDoesNotContain(actual, expected, notFound);
    assertSame(actual, factory.actual);
    assertSame(expected, factory.expected);
    assertSame(notFound, factory.notFound);
  }
}
