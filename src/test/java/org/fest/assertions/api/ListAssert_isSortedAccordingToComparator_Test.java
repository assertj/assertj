/*
 * Created on Sep 30, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.util.Comparator;

import org.junit.Test;

import org.fest.assertions.internal.Lists;

/**
 * Tests for <code>{@link AbstractCollectionAssert#isSortedAccordingTo(Comparator)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ListAssert_isSortedAccordingToComparator_Test {

  private Comparator<?> comparator = mock(Comparator.class);

  @Test
  public void should_verify_that_actual_does_not_contain_null() {
    Lists lists = mock(Lists.class);
    ListAssert assertions = new ListAssert(emptyList());
    assertions.lists = lists;
    assertions.isSortedAccordingTo(comparator);
    verify(lists).assertIsSortedAccordingToComparator(assertions.info, assertions.actual, comparator);
  }

  @Test
  public void should_return_this() {
    ListAssert assertions = new ListAssert(emptyList());
    assertSame(assertions, assertions.isSortedAccordingTo(comparator));
  }
}
