/*
 * Created on Sep 30, 2010
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.Comparator;

import org.fest.assertions.internal.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Tests for <code>{@link AbstractIterableAssert#isSortedAccordingTo(Comparator)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ListAssert_isSortedAccordingToComparator_Test {

  @Mock
  private Comparator<String> comparator;

  @Before
  public void before() {
    initMocks(this);
  }

  @Test
  public void should_verify_that_actual_does_not_contain_null() {
    Lists lists = mock(Lists.class);
    ListAssert<String> assertions = new ListAssert<String>(Collections.<String> emptyList());
    assertions.lists = lists;
    assertions.isSortedAccordingTo(comparator);
    verify(lists).assertIsSortedAccordingToComparator(assertions.info, assertions.actual, comparator);
  }

  @Test
  public void should_return_this() {
    ListAssert<String> assertions = new ListAssert<String>(Collections.<String> emptyList());
    assertSame(assertions, assertions.isSortedAccordingTo(comparator));
  }
}
