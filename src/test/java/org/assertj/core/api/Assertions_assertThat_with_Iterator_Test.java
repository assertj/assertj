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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

/**
 * @author Julien Meddah
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class Assertions_assertThat_with_Iterator_Test {

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
    Iterable<Object> actual = newLinkedHashSet();
    IteratorAssert<Object> iteratorAssert = assertThat(actual.iterator());
    assertThat(iteratorAssert).isNotNull();
  }

  @SuppressWarnings("unchecked")
  @Test
  void should_initialise_actual() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    Iterator<String> actual = (Iterator<String>) assertThat(names).actual;
    assertThat(actual).hasNext();
  }

  @Test
  void should_allow_null() {
    assertThat(assertThat((Iterator<String>) null).actual).isNull();
  }

  @Test
  void isEqualTo_should_honor_comparing_the_same_mocked_iterator() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isEqualTo(iterator);
  }

  @Test
  void should_not_consume_iterator_when_asserting_non_null() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isNotNull();
    verifyNoInteractions(iterator);
  }

  @Test
  void isInstanceOf_should_check_the_original_iterator_without_consuming_it() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isInstanceOf(Iterator.class);
    verifyNoInteractions(iterator);
  }

  @Test
  void isInstanceOfAny_should_check_the_original_iterator_without_consuming_it() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isInstanceOfAny(Iterator.class, String.class);
    verifyNoInteractions(iterator);
  }

  @Test
  void isOfAnyClassIn_should_check_the_original_iterator_without_consuming_it() {
    assertThat(stringIterator).isOfAnyClassIn(Iterator.class, StringIterator.class);
  }

  @Test
  void isExactlyInstanceOf_should_check_the_original_iterator() {
    assertThat(new StringIterator()).isExactlyInstanceOf(StringIterator.class);
  }

  @Test
  void isNotExactlyInstanceOf_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotExactlyInstanceOf(Iterator.class);

    Throwable error = catchThrowable(() -> assertThat(stringIterator).isNotExactlyInstanceOf(StringIterator.class));

    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  void isNotInstanceOf_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotInstanceOf(Long.class);
  }

  @Test
  void isNotInstanceOfAny_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotInstanceOfAny(Long.class, String.class);
  }

  @Test
  void isNotOfAnyClassIn_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotOfAnyClassIn(Long.class, String.class);
  }

  @Test
  void isSameAs_should_check_the_original_iterator_without_consuming_it() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isSameAs(iterator);
    verifyNoInteractions(iterator);
  }

  @Test
  void isNotSameAs_should_check_the_original_iterator_without_consuming_it() {
    Iterator<?> iterator = mock(Iterator.class);
    try {
      assertThat(iterator).isNotSameAs(iterator);
    } catch (AssertionError e) {
      verifyNoInteractions(iterator);
      return;
    }
    fail("Expected assertionError, because assert notSame on same iterator.");
  }

  @Test
  void iterator_can_be_asserted_twice() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    assertThat(names).hasNext().hasNext();
  }

}
