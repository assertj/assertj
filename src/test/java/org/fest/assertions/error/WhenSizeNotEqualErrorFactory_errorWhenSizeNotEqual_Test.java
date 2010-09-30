/*
 * Created on Sep 26, 2010
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

import java.util.List;

import org.fest.util.Collections;
import org.junit.*;

/**
 * Tests for <code>{@link WhenSizeNotEqualErrorFactory#errorWhenSizeNotEqual(Object, int, int)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class WhenSizeNotEqualErrorFactory_errorWhenSizeNotEqual_Test {

  private static List<String> a;
  private static int as;
  private static int es;

  @BeforeClass public static void setUpOnce() {
    a = Collections.list("Luke", "Yoda");
    as = 2;
    es = 8;
  }

  @Test public void should_create_new_AssertionErrorFactory() {
    AssertionErrorFactory factory = WhenSizeNotEqualErrorFactory.errorWhenSizeNotEqual(a, as, es);
    assertEquals(WhenSizeNotEqualErrorFactory.class, factory.getClass());
  }

  @Test public void should_pass_actual_and_sizes() {
    WhenSizeNotEqualErrorFactory factory =
      (WhenSizeNotEqualErrorFactory) WhenSizeNotEqualErrorFactory.errorWhenSizeNotEqual(a, as, es);
    assertSame(a, factory.actual);
    assertEquals(as, factory.actualSize);
    assertEquals(es, factory.expectedSize);
  }
}
