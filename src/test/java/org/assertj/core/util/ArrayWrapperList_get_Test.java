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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.api.Assertions.assertThat;


import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.ArrayWrapperList;
import org.junit.*;

/**
 * Tests for <code>{@link ArrayWrapperList#get(int)}</code>.
 * 
 * @author Yvonne Wang
 */
public class ArrayWrapperList_get_Test {

  @Rule
  public ExpectedException thrown = none();

  private ArrayWrapperList list;

  @Before
  public void setUp() {
    int[] array = { 6, 8 };
    list = ArrayWrapperList.wrap(array);
  }

  @Test
  public void should_return_value_at_index() {
    assertThat(list.get(1)).isEqualTo(8);
  }

  @Test
  public void should_throw_error_if_index_is_negative() {
    thrown.expectIndexOutOfBoundsException("Index should be between 0 and 1 (inclusive,) but was -1");
    list.get(-1);
  }

  @Test
  public void should_throw_error_if_index_is_equal_to_size() {
    thrown.expectIndexOutOfBoundsException("Index should be between 0 and 1 (inclusive,) but was 2");
    list.get(2);
  }

  @Test
  public void should_throw_error_if_index_is_greater_than_size() {
    thrown.expectIndexOutOfBoundsException("Index should be between 0 and 1 (inclusive,) but was 6");
    list.get(6);
  }
}
