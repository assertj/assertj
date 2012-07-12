/*
 * Created on Dec 24, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.TestData.matchAnything;
import static org.mockito.Mockito.*;

import java.util.regex.Pattern;

import org.fest.assertions.internal.Strings;
import org.junit.*;

/**
 * Tests for <code>{@link StringAssert#doesNotMatch(String)}</code>.
 * 
 * @author Alex Ruiz
 */
public class StringAssert_doesNotMatch_String_Test {

  private static Pattern pattern;

  private Strings strings;
  private StringAssert assertions;

  @BeforeClass
  public static void setUpOnce() {
    pattern = matchAnything();
  }

  @Before
  public void setUp() {
    strings = mock(Strings.class);
    assertions = new StringAssert("Yoda");
    assertions.strings = strings;
  }

  @Test
  public void should_verify_that_actual_does_not_match_regular_expression() {
    assertions.doesNotMatch(pattern);
    verify(strings).assertDoesNotMatch(assertions.info, assertions.actual, pattern);
  }

  @Test
  public void should_return_this() {
    StringAssert returned = assertions.doesNotMatch(pattern);
    assertSame(assertions, returned);
  }
}
