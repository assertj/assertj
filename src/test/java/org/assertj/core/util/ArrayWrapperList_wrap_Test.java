/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ArrayWrapperList#wrap(Object)}</code>.
 *
 * @author Alex Ruiz
 */
class ArrayWrapperList_wrap_Test {
  @Test
  void should_create_ArrayWrapperList_if_array_is_not_null() {
    int[] array = { 6, 8 };
    ArrayWrapperList list = ArrayWrapperList.wrap(array);
    assertThat(list).isNotNull();
    assertThat(list.array).isSameAs(array);
  }

  @Test
  void should_return_null_if_array_is_null() {
    assertThat(ArrayWrapperList.wrap(null)).isNull();
  }

  @Test
  void should_throw_error_if_parameter_is_not_array() {
    assertThatIllegalArgumentException().isThrownBy(() -> ArrayWrapperList.wrap("Yoda"))
                                        .withMessage("The object to wrap should be an array");
  }
}
