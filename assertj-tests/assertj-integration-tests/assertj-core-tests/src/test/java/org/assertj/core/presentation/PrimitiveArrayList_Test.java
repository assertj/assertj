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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.presentation;

import static org.assertj.core.api.BDDAssertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.junit.jupiter.api.Test;

class PrimitiveArrayList_Test {
  @Test
  void should_not_be_able_to_create_for_non_array() {
    // WHEN
    IllegalArgumentException illegalArgumentException = catchIllegalArgumentException(() -> new PrimitiveArrayList("not an array"));
    // THEN
    then(illegalArgumentException).hasMessage("input must be an array");
  }

  @Test
  void should_not_be_able_to_be_created_from_a_null_input() {
    // WHEN
    IllegalArgumentException illegalArgumentException = catchIllegalArgumentException(() -> new PrimitiveArrayList(null));
    // THEN
    then(illegalArgumentException).hasMessage("input must be an array");
  }

  @Test
  void should_handle_empty() {
    // GIVEN
    int[] array = new int[0];
    // WHEN
    List<Object> view = new PrimitiveArrayList(array);
    // THEN
    then(view).isEmpty();
  }

  @Test
  void should_handle_non_empty_primitive() {
    // GIVEN
    int[] array = new int[] { 1, 2, 3 };
    // WHEN
    List<Object> view = new PrimitiveArrayList(array);
    // THEN
    then(view).isEqualTo(list(1, 2, 3));
  }

  @Test
  void should_handle_non_empty_objects() {
    // GIVEN
    Integer[] array = new Integer[] { 1, 2, 3 };
    // WHEN
    List<Object> view = new PrimitiveArrayList(array);
    // THEN
    then(view).isEqualTo(list(1, 2, 3));
  }
}
