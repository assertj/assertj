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
package org.assertj.core.api.iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeUnmodifiable.shouldBeUnmodifiable;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.newTreeSet;
import static org.assertj.core.util.Sets.set;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.stream.Stream;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.apache.commons.collections4.set.UnmodifiableSortedSet;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.testkit.jdk11.Jdk11;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IteratorAssert_isUnmodifiable_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Iterator<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @MethodSource({ "modifiableIterators", "startedModifiableIterators" })
  void should_fail_if_actual_can_be_modified(Iterator<?> actual, ErrorMessageFactory errorMessageFactory) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).as(actual.getClass().getName())
                        .hasMessage(errorMessageFactory.create());
  }

  // Same as CollectionAssert_isUnmodifiable_Test#modifiableCollections
  private static Stream<Arguments> modifiableIterators() {
    return Stream.of(arguments(new ArrayList<>().iterator(),
                               shouldBeUnmodifiable("Iterator.remove()", new IllegalStateException())),
                     arguments(new LinkedHashSet<>().iterator(),
                               shouldBeUnmodifiable("Iterator.remove()", new IllegalStateException())),
                     arguments(new LinkedList<>().iterator(),
                               shouldBeUnmodifiable("Iterator.remove()", new IllegalStateException())),
                     arguments(new HashSet<>().iterator(),
                               shouldBeUnmodifiable("Iterator.remove()", new IllegalStateException())),
                     arguments(newArrayList(new Object()).iterator(),
                               shouldBeUnmodifiable("Iterator.remove()", new IllegalStateException())),
                     arguments(newLinkedHashSet(new Object()).iterator(),
                               shouldBeUnmodifiable("Iterator.remove()", new IllegalStateException())),
                     arguments(newTreeSet("element").iterator(),
                               shouldBeUnmodifiable("Iterator.remove()", new IllegalStateException())));
  }

  // No exception thrown if Iterator.remove() is called after Iterator.next()
  private static Stream<Arguments> startedModifiableIterators() {
    Iterator<?> startedIterator = new ArrayList<>(list(1, 2, 3)).iterator();
    startedIterator.next();
    Iterator<?> endedIterator = new ArrayList<>(list(1)).iterator();
    endedIterator.next();
    return Stream.of(arguments(startedIterator, shouldBeUnmodifiable("Iterator.remove()")),
                     arguments(endedIterator, shouldBeUnmodifiable("Iterator.remove()")));
  }

  @ParameterizedTest
  @MethodSource("unmodifiableIterators")
  void should_pass(Iterator<?> actual) {
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> assertThat(actual).isUnmodifiable());
  }

  // Same as CollectionAssert_isUnmodifiable_Test#unmodifiableCollections
  private static Stream<Iterator<?>> unmodifiableIterators() {
    return Stream.of(Collections.emptyList(),
                     Collections.emptyNavigableSet(),
                     Collections.emptySet(),
                     Collections.emptySortedSet(),
                     Collections.singleton("element"),
                     Collections.singletonList("element"),
                     Collections.unmodifiableCollection(list(new Object())),
                     Collections.unmodifiableList(list(new Object())),
                     Collections.unmodifiableNavigableSet(newTreeSet("element")),
                     Collections.unmodifiableSet(set(new Object())),
                     Collections.unmodifiableSortedSet(newTreeSet("element")),
                     ImmutableList.of(new Object()),
                     ImmutableSet.of(new Object()),
                     ImmutableSortedSet.of("element"),
                     Jdk11.List.of(),
                     Jdk11.List.of("element"), // same implementation for 1 or 2 parameters
                     Jdk11.List.of("element", "element", "element"), // same implementation for 3+ parameters
                     Jdk11.Set.of(),
                     Jdk11.Set.of("element"), // same implementation for 1 or 2 parameters
                     Jdk11.Set.of("element1", "element2", "element3"), // same implementation for 3+ parameters
                     Sets.unmodifiableNavigableSet(newTreeSet("element")),
                     UnmodifiableCollection.unmodifiableCollection(list(new Object())),
                     UnmodifiableList.unmodifiableList(list(new Object())),
                     UnmodifiableSet.unmodifiableSet(set(new Object())),
                     UnmodifiableSortedSet.unmodifiableSortedSet(newTreeSet("element")))
                 .map(c -> c.iterator());
  }

}
