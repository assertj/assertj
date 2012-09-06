/*
 * Created on Nov 26, 2010
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.util;

import static org.fest.assertions.test.ExpectedException.none;
import static org.junit.Assert.*;

import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link ArrayWrapperList#wrap(Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class ArrayWrapperList_wrap_Test {
  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_create_ArrayWrapperList_if_array_is_not_null() {
    int[] array = { 6, 8 };
    ArrayWrapperList list = ArrayWrapperList.wrap(array);
    assertNotNull(list);
    assertSame(array, list.array);
  }

  @Test
  public void should_return_null_if_array_is_null() {
    assertNull(ArrayWrapperList.wrap(null));
  }

  @Test
  public void should_throw_error_if_parameter_is_not_array() {
    thrown.expectIllegalArgumentException("The object to wrap should be an array");
    ArrayWrapperList.wrap("Yoda");
  }
}
