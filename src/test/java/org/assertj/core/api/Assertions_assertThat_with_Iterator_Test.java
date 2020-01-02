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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(Iterator)}</code>.
 *
 * @author Julien Meddah
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class Assertions_assertThat_with_Iterator_Test {

  private StringIterator stringIterator = new StringIterator();

  private final class StringIterator implements Iterator<String> {
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
  public void should_create_Assert() {
    AbstractIterableAssert<?, Iterable<? extends Object>, Object, ObjectAssert<Object>> iteratorAssert = assertThat(newLinkedHashSet());
    assertThat(iteratorAssert).isNotNull();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_initialise_actual() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    Iterator<String> actual = (Iterator<String>) assertThat(names).actual;
    assertThat(actual).hasNext();
  }

  @Test
  public void should_allow_null() {
    assertThat(assertThat((Iterator<String>) null).actual).isNull();
  }

  @Test
  public void isEqualTo_should_honor_comparing_the_same_mocked_iterator() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isEqualTo(iterator);
  }
  
  @Test
  public void should_not_consume_iterator_when_asserting_non_null() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isNotNull();
    verifyZeroInteractions(iterator);
  }

  @Test
  public void isInstanceOf_should_check_the_original_iterator_without_consuming_it() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isInstanceOf(Iterator.class);
    verifyZeroInteractions(iterator);
  }

  @Test
  public void isInstanceOfAny_should_check_the_original_iterator_without_consuming_it() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isInstanceOfAny(Iterator.class, String.class);
    verifyZeroInteractions(iterator);
  }

  @Test
  public void isOfAnyClassIn_should_check_the_original_iterator_without_consuming_it() {
    assertThat(stringIterator).isOfAnyClassIn(Iterator.class, StringIterator.class);
  }

  @Test
  public void isExactlyInstanceOf_should_check_the_original_iterator() {
    assertThat(new StringIterator()).isExactlyInstanceOf(StringIterator.class);
  }

  @Test
  public void isNotExactlyInstanceOf_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotExactlyInstanceOf(Iterator.class);

    Throwable error = catchThrowable(() -> assertThat(stringIterator).isNotExactlyInstanceOf(StringIterator.class));

    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  public void isNotInstanceOf_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotInstanceOf(Long.class);
  }

  @Test
  public void isNotInstanceOfAny_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotInstanceOfAny(Long.class, String.class);
  }

  @Test
  public void isNotOfAnyClassIn_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotOfAnyClassIn(Long.class, String.class);
  }

  @Test
  public void isSameAs_should_check_the_original_iterator_without_consuming_it() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isSameAs(iterator);
    verifyZeroInteractions(iterator);
  }

  @Test
  public void isNotSameAs_should_check_the_original_iterator_without_consuming_it() {
    Iterator<?> iterator = mock(Iterator.class);
    try{
      assertThat(iterator).isNotSameAs(iterator);
    } catch(AssertionError e){
      verifyZeroInteractions(iterator);
      return;
    }
    Assertions.fail("Expected assertionError, because assert notSame on same iterator.");
  }


  @Test
  public void iterator_can_be_asserted_twice() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    assertThat(names).hasNext().hasNext();
  }

}
