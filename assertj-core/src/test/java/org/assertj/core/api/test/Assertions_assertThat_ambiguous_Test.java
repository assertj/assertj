/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.test;

import static org.assertj.core.api.Assertions.assertThatIterator;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIterator;
import static org.assertj.core.api.BDDAssertions.thenPredicate;
import static org.assertj.core.api.PredicateAssert.assertThatPredicate;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

// not in org.assertj.core.api package to avoid resolving classes from it
class Assertions_solving_assertThat_ambiguous_Test {

  @Test
  void should_resolve_ambiguous_assertThat_for_interfaces_and_use_assertThat_or_then_for_classes() {
    // GIVEN
    IteratorPredicate<String> iteratorPredicate = new IteratorPredicate<>();
    // WHEN/THEN
    assertThatPredicate(iteratorPredicate).rejects("foo");
    assertThatIterator(iteratorPredicate).isExhausted();
    thenPredicate(iteratorPredicate).rejects("foo");
    thenIterator(iteratorPredicate).isExhausted();
    // assertions from AssertionsForClassTypes.assertThat
    assertThat("").isEmpty();
    assertThat(2L).isPositive();
    then("").isEmpty();
    then(2L).isPositive();
  }

  @Test
  void should_resolve_ambiguous_assertThat_for_SqlException() {
    // GIVEN
    var sqlException = new SQLException("test");
    // WHEN/THEN
    assertThat(sqlException).hasMessage("test");
    then(sqlException).hasMessage("test");
  }

  static class IteratorPredicate<T> implements Iterator<T>, Predicate<T> {

    @Override
    public boolean test(T t) {
      return false;
    }

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public T next() {
      return null;
    }

  }
}
