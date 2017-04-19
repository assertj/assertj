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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Iterator;

import org.assertj.core.api.IterableAssert.LazyIterable;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(Iterator)}</code>.
 *
 * @author Julien Meddah
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class Assertions_assertThat_with_Iterator_Test {

  @Rule
  public ExpectedException thrown = none();

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
    Iterable<String> actual = (Iterable<String>) assertThat(names).actual;
    assertThat(actual).containsOnly("Leia", "Luke");
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
  public void iterator_can_be_asserted_twice_even_though_it_can_be_iterated_only_once() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    assertThat(names).containsExactly("Luke", "Leia").containsExactly("Luke", "Leia");
  }

  @Test
  public void startsWith_should_work_with_infinite_iterators() {
    assertThat(stringIterator).startsWith("", "");
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    assertThat(stringIterator).startsWith((String[])null);
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_empty() {
    Iterator<Object> empty = asList().iterator();
    assertThat(empty).startsWith(emptyArray());
  }

  @Test
  public void should_fail_if_sequence_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    Iterator<String> names = asList("Luke", "Leia").iterator();
    assertThat(names).startsWith(new String[0]);
  }

  // startsWith tests

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    Iterator<Object> names = null;
    assertThat(names).startsWith(emptyArray());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    thrown.expectAssertionError();
    String[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    Iterator<String> names = asList("Luke", "Leia").iterator();
    assertThat(names).startsWith(sequence);
  }

  @Test
  public void should_fail_if_actual_does_not_start_with_sequence() {
    thrown.expectAssertionError();
    String[] sequence = { "Han", "C-3PO" };
    Iterator<String> names = asList("Luke", "Leia").iterator();
    assertThat(names).startsWith(sequence);
  }

  @Test
  public void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    thrown.expectAssertionError();
    String[] sequence = { "Luke", "Yoda" };
    Iterator<String> names = asList("Luke", "Leia").iterator();
    assertThat(names).startsWith(sequence);
  }

  @Test
  public void should_pass_if_actual_starts_with_sequence() {
    Iterator<String> names = asList("Luke", "Leia", "Yoda").iterator();
    assertThat(names).startsWith(array("Luke", "Leia"));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    assertThat(names).startsWith(array("Luke", "Leia"));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_fail_if_actual_does_not_start_with_sequence_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError();
    Iterator<String> names = asList("Luke", "Leia").iterator();
    String[] sequence = { "Han", "C-3PO" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

  @Test
  public void should_fail_if_actual_starts_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError();
    Iterator<String> names = asList("Luke", "Leia").iterator();
    String[] sequence = { "Luke", "Obi-Wan", "Han" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

  @Test
  public void should_pass_if_actual_starts_with_sequence_according_to_custom_comparison_strategy() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    String[] sequence = { "LUKE" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    String[] sequence = { "LUKE", "lEIA" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

}
