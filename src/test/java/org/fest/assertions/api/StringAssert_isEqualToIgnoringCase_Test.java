/*
 * Created on Dec 24, 2010
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
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.Strings;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link StringAssert#isEqualToIgnoringCase(String)}</code>.
 *
 * @author Alex Ruiz
 */
public class StringAssert_isEqualToIgnoringCase_Test {

  private Strings strings;
  private StringAssert assertions;

  @Before public void setUp() {
    strings = mock(Strings.class);
    assertions = new StringAssert("Yoda");
    assertions.strings = strings;
  }

  @Test public void should_verify_that_actual_is_equal_to_given_String_ignoring_case() {
    assertions.isEqualToIgnoringCase("yoda");
    verify(strings).assertEqualsIgnoringCase(assertions.info, assertions.actual, "yoda");
  }

  @Test public void should_return_this() {
    StringAssert returned = assertions.isEqualToIgnoringCase("yoda");
    assertSame(assertions, returned);
  }
}
