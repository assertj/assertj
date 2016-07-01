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
import static org.assertj.core.api.Assertions.atIndex;
import static org.assertj.core.test.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.IterableAssert.LazyIterable;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.StringStream;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Rule;
import org.junit.Test;

public class Assertions_assertThat_with_Stream_Test {

  @Rule
  public ExpectedException thrown = none();

  private StringStream stringStream = new StringStream();

  Stream<String> infiniteStream = Stream.generate(() -> "");

  @Test
  public void should_create_Assert() {
    Object assertions = Assertions.assertThat(Stream.of("Luke", "Leia"));
    assertThat(assertions).isNotNull();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_initialise_actual() {
    Stream<String> iterator = Stream.of("Luke", "Leia");
    List<? extends String> actual = assertThat(iterator).actual;
    assertThat((List<String>) actual).contains("Luke", atIndex(0))
                                     .contains("Leia", atIndex(1));
  }

  @Test
  public void should_allow_null() {
    assertThat(assertThat((Stream<String>) null).actual).isNull();
  }

  @Test
  public void isEqualTo_should_honor_comparing_the_same_mocked_stream() {
    Stream<?> stream = mock(Stream.class);
    assertThat(stream).isEqualTo(stream);
  }

  @Test
  public void stream_can_be_asserted_twice() {
    Stream<String> names = Stream.of("Luke", "Leia");
    assertThat(names).containsExactly("Luke", "Leia")
                     .containsExactly("Luke", "Leia");
  }

  @Test
  public void should_not_consume_stream_when_asserting_non_null() {
    Stream<?> stream = mock(Stream.class);
    assertThat(stream).isNotNull();
    verifyZeroInteractions(stream);
  }

  @Test
  public void isIstanceOf_should_check_the_original_stream_without_consuming_it() {
    Stream<?> stream = mock(Stream.class);
    assertThat(stream).isInstanceOf(Stream.class);
    verifyZeroInteractions(stream);
  }

  @Test
  public void isIstanceOfAny_should_check_the_original_stream_without_consuming_it() {
    Stream<?> stream = mock(Stream.class);
    assertThat(stream).isInstanceOfAny(Stream.class, String.class);
    verifyZeroInteractions(stream);
  }

  @Test
  public void isOfAnyClassIn_should_check_the_original_stream_without_consuming_it() {
    assertThat(stringStream).isOfAnyClassIn(Double.class, StringStream.class);
  }

  @Test
  public void isExactlyInstanceOf_should_check_the_original_stream() {
    assertThat(new StringStream()).isExactlyInstanceOf(StringStream.class);
  }

  @Test
  public void isNotExactlyInstanceOf_should_check_the_original_stream() {
    assertThat(stringStream).isNotExactlyInstanceOf(Stream.class);
    try {
      assertThat(stringStream).isNotExactlyInstanceOf(StringStream.class);
    } catch (AssertionError e) {
      // ok
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void isNotInstanceOf_should_check_the_original_stream() {
    assertThat(stringStream).isNotInstanceOf(LazyIterable.class);
  }

  @Test
  public void isNotInstanceOfAny_should_check_the_original_stream() {
    assertThat(stringStream).isNotInstanceOfAny(LazyIterable.class, String.class);
  }

  @Test
  public void isNotOfAnyClassIn_should_check_the_original_stream() {
    assertThat(stringStream).isNotOfAnyClassIn(LazyIterable.class, String.class);
  }

  @Test
  public void isSameAs_should_check_the_original_stream_without_consuming_it() {
    Stream<?> stream = mock(Stream.class);
    assertThat(stream).isSameAs(stream);
    verifyZeroInteractions(stream);
  }

  @Test
  public void isNotSameAs_should_check_the_original_stream_without_consuming_it() {
    Stream<?> stream = mock(Stream.class);
    try {
      assertThat(stream).isNotSameAs(stream);
    } catch (AssertionError e) {
      verifyZeroInteractions(stream);
      return;
    }
    Assertions.fail("Expected assertionError, because assert notSame on same stream.");
  }

  // startsWith tests

  @Test
  public void startsWith_should_work_with_infinite_streams() {
    assertThat(infiniteStream).startsWith("", "");
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    assertThat(infiniteStream).startsWith((String[]) null);
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_empty() {
    Stream<Object> empty = asList().stream();
    assertThat(empty).startsWith(emptyArray());
  }

  @Test
  public void should_fail_if_sequence_to_look_for_is_empty_and_actual_is_not() {
    thrown.expect(AssertionError.class);
    Stream<String> names = asList("Luke", "Leia").stream();
    assertThat(names).startsWith(new String[0]);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    Stream<Object> names = null;
    assertThat(names).startsWith(emptyArray());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    thrown.expect(AssertionError.class);
    String[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    Stream<String> names = asList("Luke", "Leia").stream();
    assertThat(names).startsWith(sequence);
  }

  @Test
  public void should_fail_if_actual_does_not_start_with_sequence() {
    thrown.expect(AssertionError.class);
    String[] sequence = { "Han", "C-3PO" };
    Stream<String> names = asList("Luke", "Leia").stream();
    assertThat(names).startsWith(sequence);
  }

  @Test
  public void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    thrown.expect(AssertionError.class);
    String[] sequence = { "Luke", "Yoda" };
    Stream<String> names = asList("Luke", "Leia").stream();
    assertThat(names).startsWith(sequence);
  }

  @Test
  public void should_pass_if_actual_starts_with_sequence() {
    Stream<String> names = asList("Luke", "Leia", "Yoda").stream();
    assertThat(names).startsWith(array("Luke", "Leia"));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    Stream<String> names = asList("Luke", "Leia").stream();
    assertThat(names).startsWith(array("Luke", "Leia"));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_fail_if_actual_does_not_start_with_sequence_according_to_custom_comparison_strategy() {
    thrown.expect(AssertionError.class);
    Stream<String> names = asList("Luke", "Leia").stream();
    String[] sequence = { "Han", "C-3PO" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

  @Test
  public void should_fail_if_actual_starts_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    thrown.expect(AssertionError.class);
    Stream<String> names = asList("Luke", "Leia").stream();
    String[] sequence = { "Luke", "Obi-Wan", "Han" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

  @Test
  public void should_pass_if_actual_starts_with_sequence_according_to_custom_comparison_strategy() {
    Stream<String> names = asList("Luke", "Leia").stream();
    String[] sequence = { "LUKE" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    Stream<String> names = asList("Luke", "Leia").stream();
    String[] sequence = { "LUKE", "lEIA" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

}