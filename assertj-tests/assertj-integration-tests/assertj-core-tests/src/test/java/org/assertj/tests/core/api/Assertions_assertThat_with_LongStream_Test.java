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

import java.util.stream.LongStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class Assertions_assertThat_with_LongStream_Test {

  private final LongStream longStream = LongStream.empty();

  @Test
  void should_create_Assert() {
    Object assertions = assertThat(LongStream.of(823952L, 1947230585L));
    assertThat(assertions).isNotNull();
  }

  @Test
  void should_assert_on_size() {
    assertThat(LongStream.empty()).isEmpty();
    assertThat(LongStream.of(123L, 5674L, 363L)).isNotEmpty()
                                                .hasSize(3);
  }

  @Test
  void isEqualTo_should_honor_comparing_the_same_mocked_stream() {
    LongStream stream = mock(LongStream.class);
    assertThat(stream).isEqualTo(stream);
  }

  @Test
  void stream_can_be_asserted_twice() {
    LongStream names = LongStream.of(823952L, 1947230585L);
    assertThat(names).containsExactly(823952L, 1947230585L)
                     .containsExactly(823952L, 1947230585L);
  }

  @Test
  void should_not_consume_stream_when_asserting_non_null() {
    LongStream stream = mock(LongStream.class);
    assertThat(stream).isNotNull();
    verifyNoInteractions(stream);
  }

  @Test
  void isInstanceOf_should_check_the_original_stream_without_consuming_it() {
    LongStream stream = mock(LongStream.class);
    assertThat(stream).isInstanceOf(LongStream.class);
    verifyNoInteractions(stream);
  }

  @Test
  void isInstanceOfAny_should_check_the_original_stream_without_consuming_it() {
    LongStream stream = mock(LongStream.class);
    assertThat(stream).isInstanceOfAny(LongStream.class, String.class);
    verifyNoInteractions(stream);
  }

  @Test
  void isOfAnyClassIn_should_check_the_original_stream_without_consuming_it() {
    LongStream stream = mock(LongStream.class);
    assertThat(stream).isOfAnyClassIn(Double.class, stream.getClass());
  }

  @Test
  void isExactlyInstanceOf_should_check_the_original_stream() {
    // factory creates use internal classes
    assertThat(longStream).isExactlyInstanceOf(longStream.getClass());
  }

  @Test
  void isNotExactlyInstanceOf_should_check_the_original_stream() {
    assertThat(longStream).isNotExactlyInstanceOf(LongStream.class);

    Throwable error = catchThrowable(() -> assertThat(longStream).isNotExactlyInstanceOf(longStream.getClass()));

    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  void isNotInstanceOf_should_check_the_original_stream() {
    assertThat(longStream).isNotInstanceOf(Long.class);
  }

  @Test
  void isNotInstanceOfAny_should_check_the_original_stream() {
    assertThat(longStream).isNotInstanceOfAny(Long.class, String.class);
  }

  @Test
  void isNotOfAnyClassIn_should_check_the_original_stream() {
    assertThat(longStream).isNotOfAnyClassIn(Long.class, String.class);
  }

  @Test
  void isSameAs_should_check_the_original_stream_without_consuming_it() {
    LongStream stream = mock(LongStream.class);
    assertThat(stream).isSameAs(stream);
    verifyNoInteractions(stream);
  }

  @Test
  void isNotSameAs_should_check_the_original_stream_without_consuming_it() {
    LongStream stream = mock(LongStream.class);
    try {
      assertThat(stream).isNotSameAs(stream);
    } catch (AssertionError e) {
      verifyNoInteractions(stream);
      return;
    }
    Assertions.fail("Expected assertionError, because assert notSame on same stream.");
  }
}
