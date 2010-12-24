/*
 * Created on Dec 22, 2010
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
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.TestData.matchAnything;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.Strings;
import org.junit.*;

/**
 * Tests for <code>{@link StringAssert#matches(String)}</code>.
 *
 * @author Alex Ruiz
 */
public class StringAssert_matches_String_Test {

  private static String regex;

  private Strings strings;
  private StringAssert assertions;

  @BeforeClass public static void setUpOnce() {
    regex = matchAnything().pattern();
  }

  @Before public void setUp() {
    strings = mock(Strings.class);
    assertions = new StringAssert("Yoda");
    assertions.strings = strings;
  }

  @Test public void should_verify_that_actual_matches_regular_expression() {
    assertions.matches(regex);
    verify(strings).assertMatches(assertions.info, assertions.actual, regex);
  }

  @Test public void should_return_this() {
    StringAssert returned = assertions.matches(regex);
    assertSame(assertions, returned);
  }
}
