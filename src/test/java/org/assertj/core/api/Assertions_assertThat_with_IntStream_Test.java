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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.List;
import java.util.stream.IntStream;

import org.assertj.core.api.IterableAssert.LazyIterable;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class Assertions_assertThat_with_IntStream_Test {

  @Rule
  public ExpectedException thrown = none();

  private IntStream intStream = IntStream.empty();

  @Test
  public void should_create_Assert() {
    Object assertions = assertThat(IntStream.of(823952, 1947230585));
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_assert_on_size() {
    assertThat(IntStream.empty()).isEmpty();
    assertThat(IntStream.of(123, 5674, 363)).isNotEmpty()
                                            .hasSize(3);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_initialise_actual() {
    IntStream iterator = IntStream.of(823952, 1947230585);
    List<? extends Integer> actual = assertThat(iterator).actual;
    assertThat((List<Integer>) actual).contains(823952, atIndex(0))
                                      .contains(1947230585, atIndex(1));
  }

  @Test
  public void should_allow_null() {
    assertThat(assertThat((IntStream) null).actual).isNull();
  }

  @Test
  public void isEqualTo_should_honor_comparing_the_same_mocked_stream() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isEqualTo(stream);
  }

  @Test
  public void stream_can_be_asserted_twice() {
    IntStream names = IntStream.of(823952, 1947230585);
    assertThat(names).containsExactly(823952, 1947230585)
                     .containsExactly(823952, 1947230585);
  }

  @Test
  public void should_not_consume_stream_when_asserting_non_null() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isNotNull();
    verifyZeroInteractions(stream);
  }

  @Test
  public void isInstanceOf_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isInstanceOf(IntStream.class);
    verifyZeroInteractions(stream);
  }

  @Test
  public void isInstanceOfAny_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isInstanceOfAny(IntStream.class, String.class);
    verifyZeroInteractions(stream);
  }

  @Test
  public void isOfAnyClassIn_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isOfAnyClassIn(Double.class, stream.getClass());
  }

  @Test
  public void isExactlyInstanceOf_should_check_the_original_stream() {
    // factory creates use internal classes
    assertThat(intStream).isExactlyInstanceOf(intStream.getClass());
  }

  @Test
  public void isNotExactlyInstanceOf_should_check_the_original_stream() {
    assertThat(intStream).isNotExactlyInstanceOf(IntStream.class);
    try {
      assertThat(intStream).isNotExactlyInstanceOf(intStream.getClass());
    } catch (AssertionError e) {
      // ok
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void isNotInstanceOf_should_check_the_original_stream() {
    assertThat(intStream).isNotInstanceOf(LazyIterable.class);
  }

  @Test
  public void isNotInstanceOfAny_should_check_the_original_stream() {
    assertThat(intStream).isNotInstanceOfAny(LazyIterable.class, String.class);
  }

  @Test
  public void isNotOfAnyClassIn_should_check_the_original_stream() {
    assertThat(intStream).isNotOfAnyClassIn(LazyIterable.class, String.class);
  }

  @Test
  public void isSameAs_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isSameAs(stream);
    verifyZeroInteractions(stream);
  }

  @Test
  public void isNotSameAs_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    try {
      assertThat(stream).isNotSameAs(stream);
    } catch (AssertionError e) {
      verifyZeroInteractions(stream);
      return;
    }
    Assertions.fail("Expected assertionError, because assert notSame on same stream.");
  }
}
