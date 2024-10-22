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
package org.assertj.core.api.iterable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.commons.collections4.set.UnmodifiableNavigableSet;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.apache.commons.collections4.set.UnmodifiableSortedSet;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.test.jdk11.Jdk11;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.stream.Stream;

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
import static org.mockito.Mockito.mock;

/**
 * See also {@code org.assertj.core.api.collection.CollectionAssert_isUnmodifiable_Test}.
 */
class IterableAssert_isUnmodifiable_Test {
  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Iterable<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_not_a_collection() {
    // GIVEN
    Iterable<?> actual = mock(Iterable.class);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).hasMessageContaining("instance of", "java.util.Collection");
  }

  @ParameterizedTest
  @MethodSource("modifiableCollections")
  void should_fail_if_actual_can_be_modified(Iterable<?> actual, ErrorMessageFactory errorMessageFactory) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).as(actual.getClass().getName())
                        .hasMessage(errorMessageFactory.create());
  }

  private static Stream<Arguments> modifiableCollections() {
    return Stream.of(arguments(new ArrayList<>(), shouldBeUnmodifiable("Collection.add(null)")),
                     arguments(new LinkedList<>(), shouldBeUnmodifiable("Collection.add(null)")),
                     arguments(new HashSet<>(), shouldBeUnmodifiable("Collection.add(null)")));
  }

  @ParameterizedTest(name = "{1}")
  @MethodSource("unmodifiableCollections")
  void should_pass(Iterable<?> actual, final Class<?> actualClass) {
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> assertThat(actual).isUnmodifiable());
  }

  private static Stream<Arguments> unmodifiableCollections() {
    return Stream.of(Collections.emptyList(),
                     Collections.emptyNavigableSet(),
                     Collections.emptySet(),
                     Collections.unmodifiableCollection(list(new Object())),
                     Collections.unmodifiableList(list(new Object())),
                     Collections.unmodifiableNavigableSet(newTreeSet("element")),
                     Collections.unmodifiableSet(set(new Object())),
                     Collections.unmodifiableSortedSet(newTreeSet("element")))
                 .map(collection -> Arguments.of(collection, collection.getClass()));
  }

}
