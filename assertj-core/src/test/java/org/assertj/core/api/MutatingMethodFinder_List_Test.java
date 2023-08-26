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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Tests finding mutating methods in lists. */
class MutatingMethodFinder_List_Test extends MutatingMethodFinder_Collection_Test {

  @ParameterizedTest
  @MethodSource("one_mutating_method_source")
  void one_mutating_method(final String method, final int argumentCount) {
    testOneMutatingMethodInCollection(List.class, new ArrayList<>(), method, argumentCount);
  }

  /** Mutating list and collection methods. */
  static Stream<Arguments> one_mutating_method_source() {
    final Stream<Arguments> methods = Stream.of(Arguments.of("add", 2),
                                                Arguments.of("addAll", 2),
                                                Arguments.of("remove", 1),
                                                Arguments.of("replaceAll", 1),
                                                Arguments.of("set", 2),
                                                Arguments.of("sort", 1));

    return Streams.concat(collectionMethods(), methods);
  }

  @Nested
  class Iterator_Test {
    @Test
    void successful_remove_is_detected() {
      testIterator(List.class, "iterator", mock(Iterator.class), "remove");
    }

    @Test
    void successful_set_is_detected() {
      ListIterator<?> iterator = mock(ListIterator.class);
      doThrow(UnsupportedOperationException.class).when(iterator).remove();
      doThrow(UnsupportedOperationException.class).when(iterator).add(any());
      testIterator(List.class, "listIterator", iterator, "set");
    }

    @Test
    void successful_add_is_detected() {
      ListIterator<?> iterator = mock(ListIterator.class);
      doThrow(UnsupportedOperationException.class).when(iterator).remove();
      doThrow(UnsupportedOperationException.class).when(iterator).set(any());
      testIterator(List.class, "listIterator", iterator, "add");
    }

    @Test
    void successful_list_iterator_remove_is_detected() {
      ListIterator<?> iterator = mock(ListIterator.class);
      doThrow(UnsupportedOperationException.class).when(iterator).set(any());
      doThrow(UnsupportedOperationException.class).when(iterator).add(any());
      testIterator(List.class, "listIterator", iterator, "remove");
    }
  }

  @ParameterizedTest(name = "{1}")
  @MethodSource("an_immutable_list_is_identified_source")
  void an_immutable_list_is_identified(final List<String> list, final Class<?> listClass) {
    assertThat(finder.visitList(list)).isEmpty();
  }

  static Stream<Arguments> an_immutable_list_is_identified_source() {
    return Stream.of(Collections.emptyList(), Collections.singletonList(""), ImmutableList.of())
                 .map(list -> Arguments.of(list, list.getClass()));
  }

  @ParameterizedTest(name = "{1}")
  @MethodSource("a_mutable_list_is_identified_source")
  void a_mutable_list_is_identified(final List<String> list, final Class<?> listClass) {
    assertThat(finder.visitList(list)).isNotEmpty();
  }

  static Stream<Arguments> a_mutable_list_is_identified_source() {
    return Stream.of(new ArrayList<>(), new LinkedList<>(), new Vector<>())
                 .map(list -> Arguments.of(list, list.getClass()));
  }
}
