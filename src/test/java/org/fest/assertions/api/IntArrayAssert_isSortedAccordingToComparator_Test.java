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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;

import static org.fest.assertions.test.IntArrayFactory.emptyArray;

import static org.mockito.Mockito.*;

import java.util.Comparator;

import org.junit.Test;

import org.fest.assertions.internal.IntArrays;

/**
 * Tests for <code>{@link IntArrayAssert#isSortedAccordingTo(Comparator)}</code>.
 * 
 * @author Joel Costigliola
 */
public class IntArrayAssert_isSortedAccordingToComparator_Test {

  @SuppressWarnings("unchecked")
  private Comparator<? extends Integer> comparator = mock(Comparator.class);

  @Test
  public void should_verify_that_assertIsSorted_is_called() {
    IntArrays arrays = mock(IntArrays.class);
    IntArrayAssert assertions = new IntArrayAssert(emptyArray());
    assertions.arrays = arrays;
    assertions.isSortedAccordingTo(comparator);
    verify(arrays).assertIsSortedAccordingToComparator(assertions.info, assertions.actual, comparator);
  }

  @Test
  public void should_return_this() {
    IntArrayAssert objectArrayAssert = new IntArrayAssert(new int[] {1, 2});
    assertSame(objectArrayAssert, objectArrayAssert.isSortedAccordingTo(comparator));
  }

}
