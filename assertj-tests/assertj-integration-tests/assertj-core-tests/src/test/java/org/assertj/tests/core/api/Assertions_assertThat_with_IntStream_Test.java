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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class Assertions_assertThat_with_IntStream_Test {

  private final IntStream intStream = IntStream.empty();

  @Test
  void should_create_Assert() {
    Object assertions = assertThat(IntStream.of(823952, 1947230585));
    assertThat(assertions).isNotNull();
  }

  @Test
  void should_assert_on_size() {
    assertThat(IntStream.empty()).isEmpty();
    assertThat(IntStream.of(123, 5674, 363)).isNotEmpty()
                                            .hasSize(3);
  }

  @Test
  void isEqualTo_should_honor_comparing_the_same_mocked_stream() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isEqualTo(stream);
  }

  @Test
  void stream_can_be_asserted_twice() {
    IntStream names = IntStream.of(823952, 1947230585);
    assertThat(names).containsExactly(823952, 1947230585)
                     .containsExactly(823952, 1947230585);
  }

  @Test
  void should_not_consume_stream_when_asserting_non_null() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isNotNull();
    verifyNoInteractions(stream);
  }

  @Test
  void isInstanceOf_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isInstanceOf(IntStream.class);
    verifyNoInteractions(stream);
  }

  @Test
  void isInstanceOfAny_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isInstanceOfAny(IntStream.class, String.class);
    verifyNoInteractions(stream);
  }

  @Test
  void isOfAnyClassIn_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isOfAnyClassIn(Double.class, stream.getClass());
  }

  @Test
  void isExactlyInstanceOf_should_check_the_original_stream() {
    // factory creates use internal classes
    assertThat(intStream).isExactlyInstanceOf(intStream.getClass());
  }

  @Test
  void isNotExactlyInstanceOf_should_check_the_original_stream() {
    assertThat(intStream).isNotExactlyInstanceOf(IntStream.class);

    Throwable error = catchThrowable(() -> assertThat(intStream).isNotExactlyInstanceOf(intStream.getClass()));

    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  void isNotInstanceOf_should_check_the_original_stream() {
    assertThat(intStream).isNotInstanceOf(Long.class);
  }

  @Test
  void isNotInstanceOfAny_should_check_the_original_stream() {
    assertThat(intStream).isNotInstanceOfAny(Long.class, String.class);
  }

  @Test
  void isNotOfAnyClassIn_should_check_the_original_stream() {
    assertThat(intStream).isNotOfAnyClassIn(Long.class, String.class);
  }

  @Test
  void isSameAs_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    assertThat(stream).isSameAs(stream);
    verifyNoInteractions(stream);
  }

  @Test
  void isNotSameAs_should_check_the_original_stream_without_consuming_it() {
    IntStream stream = mock(IntStream.class);
    try {
      assertThat(stream).isNotSameAs(stream);
    } catch (AssertionError e) {
      verifyNoInteractions(stream);
      return;
    }
    Assertions.fail("Expected assertionError, because assert notSame on same stream.");
  }
}
