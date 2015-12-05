/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.IntArrays.arrayOf;
import static org.assertj.core.util.Arrays.array;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Tests for <code>{@link ArrayWrapperList#size()}</code>.
 * 
 * @author Alex Ruiz
 * @author Dan Corder
 */
@RunWith(JUnitParamsRunner.class)
public class ArrayWrapperList_size_Test {

  @Test
  @Parameters(method = "arrayGenerator")
  public void should_return_size_of_array(int[] array) {
    ArrayWrapperList list = new ArrayWrapperList(array);
    assertThat(list).hasSameSizeAs(array);
  }

  @SuppressWarnings("unused")
  private Object arrayGenerator() {
    return array(arrayOf(0, 1, 2),
                 arrayOf(0),
                 arrayOf());
  }
}
