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
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.newTreeSet;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class IterableAssert_should_honor_SortedSet_comparator_Test {

  @ParameterizedTest
  @MethodSource("sets")
  void should_honor_sorted_set_comparator(Iterable<String> actual) {
    // WHEN/THEN
    assertThat(actual).contains("foo");
    assertThat(actual).containsAll(newLinkedHashSet("foo"));
    assertThat(actual).containsAnyElementsOf(newLinkedHashSet("foo", "bar"));
    assertThat(actual).containsAnyOf("foo", "bar");
    assertThat(actual).containsExactly("foo");
    assertThat(actual).containsExactlyElementsOf(newLinkedHashSet("foo"));
    assertThat(actual).containsExactlyInAnyOrder("foo");
    assertThat(actual).containsExactlyInAnyOrderElementsOf(newLinkedHashSet("foo"));
    assertThat(actual).containsOnly("foo");
    assertThat(actual).isSubsetOf(newLinkedHashSet("foo"));
    assertThat(actual).containsOnlyOnce("foo");
    assertThat(actual).containsOnlyOnceElementsOf(newLinkedHashSet("foo"));
    assertThat(actual).containsSequence("foo");
    assertThat(actual).containsSequence(newLinkedHashSet("foo"));
    assertThat(actual).containsSubsequence("foo");
    assertThat(actual).containsSubsequence(newLinkedHashSet("foo"));
    assertThatAssertionErrorIsThrownBy(() -> assertThat(actual).doesNotContain("foo", "FOO"));
    assertThatAssertionErrorIsThrownBy(() -> assertThat(actual).doesNotContainAnyElementsOf(newLinkedHashSet("foo", "FOO")));
    assertThatAssertionErrorIsThrownBy(() -> assertThat(actual).doesNotContainSequence("foo"));
    assertThatAssertionErrorIsThrownBy(() -> assertThat(actual).doesNotContainSequence(newLinkedHashSet("foo")));
    assertThatAssertionErrorIsThrownBy(() -> assertThat(actual).doesNotContainSubsequence("foo"));
    assertThatAssertionErrorIsThrownBy(() -> assertThat(actual).doesNotContainSubsequence(newLinkedHashSet("foo")));
  }

  private static Stream<Set<String>> sets() {
    Set<String> treeSetWithComparator = new TreeSet<>(Comparator.comparing(String::toUpperCase));
    treeSetWithComparator.add("FOO");
    SortedSet<String> sortedSetWithComparator = new TreeSet<>(Comparator.comparing(String::toUpperCase));
    sortedSetWithComparator.add("FOO");
    Set<String> treeSet = newTreeSet("foo");
    SortedSet<String> sortedSet = newTreeSet("foo");

    return Stream.of(sortedSetWithComparator, treeSetWithComparator, sortedSet, treeSet);
  }

}
