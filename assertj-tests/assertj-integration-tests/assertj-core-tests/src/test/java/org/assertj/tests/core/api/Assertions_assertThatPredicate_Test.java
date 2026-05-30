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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThatPredicate;

import java.util.Iterator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

class Assertions_assertThatPredicate_Test {

  @Test
  void should_provide_Predicate_assertions() {
    // GIVEN
    IteratorPredicate<String> actual = new IteratorPredicate<>();
    // WHEN/THEN
    assertThatPredicate(actual).rejects("foo");
  }

  private static class IteratorPredicate<T> implements Iterator<T>, Predicate<T> {

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
