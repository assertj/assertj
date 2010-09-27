/*
 * Created on Sep 21, 2010
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

import static org.fest.util.Collections.list;
import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.*;

/**
 * Tests for <code>{@link WhenNotEmptyErrorFactory#errorWhenNotEmpty(Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class WhenNotEmptyErrorFactory_errorWhenNotEmpty_Test {

  private static Collection<String> actual;

  @BeforeClass public static void setUpOnce() {
    actual = list("Yoda");
  }

  @Test public void should_create_new_AssertionErrorFactory() {
    AssertionErrorFactory factory = WhenNotEmptyErrorFactory.errorWhenNotEmpty(actual);
    assertEquals(WhenNotEmptyErrorFactory.class, factory.getClass());
  }

  @Test public void should_pass_actual() {
    WhenNotEmptyErrorFactory factory = (WhenNotEmptyErrorFactory) WhenNotEmptyErrorFactory.errorWhenNotEmpty(actual);
    assertSame(actual, factory.actual);
  }
}
