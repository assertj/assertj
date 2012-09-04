/*
 * Created on Nov 26, 2010
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
package org.fest.assertions.util;

import static org.fest.assertions.test.IntArrays.emptyArray;
import static org.fest.util.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for <code>{@link ArrayWrapperList#size()}</code>.
 * 
 * @author Alex Ruiz
 */
@RunWith(Parameterized.class)
public class ArrayWrapperList_size_Test {

  @Parameters
  public static Collection<Object[]> parameters() {
    return newArrayList(new Object[][] { { new int[] { 0, 1, 2 } }, { new int[] { 0 } }, { emptyArray() } });
  }

  private final int[] array;

  public ArrayWrapperList_size_Test(int[] array) {
    this.array = array;
  }

  @Test
  public void should_return_size_of_array() {
    ArrayWrapperList list = new ArrayWrapperList(array);
    assertEquals(array.length, list.size());
  }
}
