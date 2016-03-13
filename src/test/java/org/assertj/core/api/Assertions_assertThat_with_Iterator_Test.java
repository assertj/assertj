/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Iterator;

import org.assertj.core.api.IterableAssert.LazyIterable;
import org.junit.Test;

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
      return false;
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
    AbstractIterableAssert<?, ? extends Iterable<? extends Object>, Object> assertions = Assertions.assertThat(newLinkedHashSet());
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_initialise_actual() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    AbstractIterableAssert<?, ? extends Iterable<? extends String>, String> assertions = assertThat(names);
    assertThat(assertions.actual).containsOnly("Leia", "Luke");
  }

  @Test
  public void should_allow_null() {
    AbstractIterableAssert<?, ? extends Iterable<? extends String>, String> assertions = assertThat((Iterator<String>) null);
    assertThat(assertions.actual).isNull();
  }

  @Test
  public void should_not_consume_iterator_when_asserting_non_null() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isNotNull();
    verifyZeroInteractions(iterator);
  }

  @Test
  public void isIstanceOf_should_check_the_original_iterator_without_consuming_it() {
    Iterator<?> iterator = mock(Iterator.class);
    assertThat(iterator).isInstanceOf(Iterator.class);
    verifyZeroInteractions(iterator);
  }

  @Test
  public void isIstanceOfAny_should_check_the_original_iterator_without_consuming_it() {
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
    try {
      assertThat(stringIterator).isNotExactlyInstanceOf(StringIterator.class);
    } catch (AssertionError e) {
      // ok
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
  @Test
  public void isNotInstanceOf_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotInstanceOf(LazyIterable.class);
  }
  
  @Test
  public void isNotInstanceOfAny_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotInstanceOfAny(LazyIterable.class, String.class);
  }
  
  @Test
  public void isNotOfAnyClassIn_should_check_the_original_iterator() {
    assertThat(stringIterator).isNotOfAnyClassIn(LazyIterable.class, String.class);
  }
  
  @Test
  public void iterator_can_be_asserted_twice_even_though_it_can_be_iterated_only_once() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    assertThat(names).containsExactly("Luke", "Leia").containsExactly("Luke", "Leia");
  }
}