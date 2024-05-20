/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.presentation;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

class TransformingList_Test {
  @Test
  void should_handle_empty() {
    // GIVEN
    List<Integer> source = emptyList();
    // WHEN
    List<String> transformed = new TransformingList<>(source, Object::toString);
    // THEN
    then(transformed).isEmpty();
  }

  @Test
  void should_handle_non_empty() {
    // GIVEN
    List<Integer> source = list(1, 2, 3);
    // WHEN
    List<String> transformed = new TransformingList<>(source, Object::toString);
    // THEN
    then(transformed).isEqualTo(ImmutableList.of("1", "2", "3"));
  }

  @Test
  void should_not_be_able_to_be_created_from_a_null_list() {
    // WHEN
    NullPointerException nullPointerException = catchNullPointerException(() -> new TransformingList<>(null, Object::toString));
    // THEN
    then(nullPointerException).hasMessage("source list");
  }

  @Test
  void should_not_be_able_to_be_created_with_a_null_function() {
    // WHEN
    NullPointerException nullPointerException = catchNullPointerException(() -> new TransformingList<>(emptyList(), null));
    // THEN
    then(nullPointerException).hasMessage("transform function");
  }

}
