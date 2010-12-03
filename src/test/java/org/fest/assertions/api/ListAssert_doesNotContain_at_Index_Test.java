/*
 * Created on Dec 2, 2010
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

import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertSame;
import static org.fest.assertions.data.Index.atIndex;
import static org.mockito.Mockito.*;

import org.fest.assertions.data.Index;
import org.fest.assertions.internal.Lists;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ListAssert#doesNotContain(Object, Index)}</code>.
 *
 * @author Alex Ruiz
 */
public class ListAssert_doesNotContain_at_Index_Test {

  private Lists lists;
  private ListAssert assertions;

  @Before public void setUp() {
    lists = mock(Lists.class);
    assertions = new ListAssert(emptyList());
    assertions.lists = lists;
  }

  @Test public void should_verify_that_actual_doesNotContain_value_at_index() {
    assertions.doesNotContain("Yoda", atIndex(2));
    verify(lists).assertDoesNotContain(assertions.info, assertions.actual, "Yoda", atIndex(2));
  }

  @Test public void should_return_this() {
    ListAssert returned = assertions.doesNotContain("Luke", atIndex(6));
    assertSame(assertions, returned);
  }
}
