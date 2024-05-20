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
package org.assertj.tests.core.api;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatIterator;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Iterator;
import org.assertj.core.api.IteratorAssert;
import org.junit.jupiter.api.Test;

class Assertions_assertThatIterator_Test {

  private final StringIterator stringIterator = new StringIterator();

  private static final class StringIterator implements Iterator<String> {
    @Override
    public boolean hasNext() {
      return true;
    }

    @Override
    public String next() {
      return "";
    }

    @Override
    public void remove() {}
  }

  @Test
  void should_create_Assert() {
    // GIVEN
    Iterable<Object> actual = newLinkedHashSet();
    // WHEN
    IteratorAssert<Object> iteratorAssert = assertThatIterator(actual.iterator());
    // THEN
    then(iteratorAssert).isNotNull();
  }

  @Test
  void isEqualTo_should_honor_comparing_the_same_mocked_iterator() {
    // GIVEN
    Iterator<?> iterator = mock(Iterator.class);
    // WHEN/THEN
    assertThatIterator(iterator).isEqualTo(iterator);
  }

  @Test
  void should_not_consume_iterator_when_asserting_non_null() {
    // GIVEN
    Iterator<?> iterator = mock(Iterator.class);
    // WHEN
    assertThatIterator(iterator).isNotNull();
    // THEN
    verifyNoInteractions(iterator);
  }

  @Test
  void isInstanceOf_should_check_the_original_iterator_without_consuming_it() {
    // GIVEN
    Iterator<?> iterator = mock(Iterator.class);
    // WHEN
    assertThatIterator(iterator).isInstanceOf(Iterator.class);
    // THEN
    verifyNoInteractions(iterator);
  }

  @Test
  void isInstanceOfAny_should_check_the_original_iterator_without_consuming_it() {
    // GIVEN
    Iterator<?> iterator = mock(Iterator.class);
    // WHEN
    then(iterator).isInstanceOfAny(Iterator.class, String.class);
    // THEN
    verifyNoInteractions(iterator);
  }

  @Test
  void isOfAnyClassIn_should_check_the_original_iterator_without_consuming_it() {
    assertThatIterator(stringIterator).isOfAnyClassIn(Iterator.class, StringIterator.class);
  }

  @Test
  void isExactlyInstanceOf_should_check_the_original_iterator() {
    assertThatIterator(new StringIterator()).isExactlyInstanceOf(StringIterator.class);
  }

  @Test
  void isNotExactlyInstanceOf_should_check_the_original_iterator() {
    // WHEN
    assertThatIterator(stringIterator).isNotExactlyInstanceOf(Iterator.class);
    // THEN
    expectAssertionError(() -> then(stringIterator).isNotExactlyInstanceOf(StringIterator.class));
  }

  @Test
  void isNotInstanceOf_should_check_the_original_iterator() {
    assertThatIterator(stringIterator).isNotInstanceOf(Long.class);
  }

  @Test
  void isNotInstanceOfAny_should_check_the_original_iterator() {
    assertThatIterator(stringIterator).isNotInstanceOfAny(Long.class, String.class);
  }

  @Test
  void isNotOfAnyClassIn_should_check_the_original_iterator() {
    assertThatIterator(stringIterator).isNotOfAnyClassIn(Long.class, String.class);
  }

  @Test
  void isSameAs_should_check_the_original_iterator_without_consuming_it() {
    // GIVEN
    Iterator<?> iterator = mock(Iterator.class);
    // WHEN
    assertThatIterator(iterator).isSameAs(iterator);
    // THEN
    verifyNoInteractions(iterator);
  }

  @Test
  void isNotSameAs_should_check_the_original_iterator_without_consuming_it() {
    // GIVEN
    Iterator<?> iterator = mock(Iterator.class);
    // WHEN
    expectAssertionError(() -> assertThatIterator(iterator).isNotSameAs(iterator));
    // THEN
    verifyNoInteractions(iterator);
  }

  @Test
  void iterator_can_be_asserted_twice() {
    // GIVEN
    Iterator<String> names = asList("Luke", "Leia").iterator();
    // WHEN/THEN
    assertThatIterator(names).hasNext().hasNext();
  }

}
