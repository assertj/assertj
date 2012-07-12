/*
 * Created on Jun 4, 2012
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fest.assertions.internal.Strings;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link StringAssert#hasSameSizeAs(Object[])}</code>.
 * 
 * @author Nicolas François
 */
public class StringAssert_hasSameSizeAs_with_Array_Test {

  private Strings strings;
  private StringAssert assertions;

  @Before
  public void setUp() {
    strings = mock(Strings.class);
    assertions = new StringAssert("Yoda");
    assertions.strings = strings;
  }

  @Test
  public void should_verify_that_actual_has_expected_size() {
    List<String> other = list("Luke");
    assertions.hasSameSizeAs(other);
    verify(strings).assertHasSameSizeAs(assertions.info, assertions.actual, other);
  }

  @Test
  public void should_return_this() {
    StringAssert returned = assertions.hasSameSizeAs(list("Luke"));
    assertSame(assertions, returned);
  }
}
