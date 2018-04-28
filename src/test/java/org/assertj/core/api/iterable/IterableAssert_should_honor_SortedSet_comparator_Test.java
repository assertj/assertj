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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.newTreeSet;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class IterableAssert_should_honor_SortedSet_comparator_Test {

  private Iterable<Set<String>> sets;

  @Before
  public void setup() {
    Set<String> treeSetWithComparator = new TreeSet<>(Comparator.comparing(String::toUpperCase));
    treeSetWithComparator.add("FOO");
    SortedSet<String> sortedSetWithComparator = new TreeSet<>(Comparator.comparing(String::toUpperCase));
    sortedSetWithComparator.add("FOO");
    Set<String> treeSet = newTreeSet("foo");
    SortedSet<String> sortedSet = newTreeSet("foo");
    sets = newArrayList(sortedSetWithComparator, treeSetWithComparator, sortedSet, treeSet);
  }

  @Test
  public void should_honor_sorted_set_comparator() {
    assertThat(sets).allSatisfy(set -> {
      assertThat(set).contains("foo");
      assertThat(set).containsAll(newLinkedHashSet("foo"));
      assertThat(set).containsAnyElementsOf(newLinkedHashSet("foo", "bar"));
      assertThat(set).containsAnyOf("foo", "bar");
      assertThat(set).containsExactly("foo");
      assertThat(set).containsExactlyElementsOf(newLinkedHashSet("foo"));
      assertThat(set).containsExactlyInAnyOrder("foo");
      assertThat(set).containsExactlyInAnyOrderElementsOf(newLinkedHashSet("foo"));
      assertThat(set).containsOnly("foo");
      assertThat(set).containsOnlyElementsOf(newLinkedHashSet("foo"));
      assertThat(set).containsOnlyOnce("foo");
      assertThat(set).containsSequence("foo");
      assertThat(set).containsSequence(newLinkedHashSet("foo"));
      assertThat(set).containsSubsequence("foo");
      assertThat(set).containsSubsequence(newLinkedHashSet("foo"));
    });
    assertThat(sets).noneSatisfy(set -> assertThat(set).doesNotContain("foo", "FOO"));
    assertThat(sets).noneSatisfy(set -> assertThat(set).doesNotContainAnyElementsOf(newLinkedHashSet("foo", "FOO")));
    assertThat(sets).noneSatisfy(set -> assertThat(set).doesNotContainSequence("foo"));
    assertThat(sets).noneSatisfy(set -> assertThat(set).doesNotContainSequence(newLinkedHashSet("foo")));
    assertThat(sets).noneSatisfy(set -> assertThat(set).doesNotContainSubsequence("foo"));
    assertThat(sets).noneSatisfy(set -> assertThat(set).doesNotContainSubsequence(newLinkedHashSet("foo")));
  }

}
