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

import java.util.stream.DoubleStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class Assertions_assertThat_with_DoubleStream_Test {

  private final DoubleStream intStream = DoubleStream.empty();

  @Test
  void should_create_Assert() {
    Object assertions = assertThat(DoubleStream.of(823952.8, 1947230585.9));
    assertThat(assertions).isNotNull();
  }

  @Test
  void should_assert_on_size() {
    assertThat(DoubleStream.empty()).isEmpty();
    assertThat(DoubleStream.of(123.3, 5674.5, 363.4)).isNotEmpty()
                                                     .hasSize(3);
  }

  @Test
  void isEqualTo_should_honor_comparing_the_same_mocked_stream() {
    DoubleStream stream = mock(DoubleStream.class);
    assertThat(stream).isEqualTo(stream);
  }

  @Test
  void stream_can_be_asserted_twice() {
    DoubleStream names = DoubleStream.of(823952.8, 1947230585.9);
    assertThat(names).containsExactly(823952.8, 1947230585.9)
                     .containsExactly(823952.8, 1947230585.9);
  }

  @Test
  void should_not_consume_stream_when_asserting_non_null() {
    DoubleStream stream = mock(DoubleStream.class);
    assertThat(stream).isNotNull();
    verifyNoInteractions(stream);
  }

  @Test
  void isInstanceOf_should_check_the_original_stream_without_consuming_it() {
    DoubleStream stream = mock(DoubleStream.class);
    assertThat(stream).isInstanceOf(DoubleStream.class);
    verifyNoInteractions(stream);
  }

  @Test
  void isInstanceOfAny_should_check_the_original_stream_without_consuming_it() {
    DoubleStream stream = mock(DoubleStream.class);
    assertThat(stream).isInstanceOfAny(DoubleStream.class, String.class);
    verifyNoInteractions(stream);
  }

  @Test
  void isOfAnyClassIn_should_check_the_original_stream_without_consuming_it() {
    DoubleStream stream = mock(DoubleStream.class);
    assertThat(stream).isOfAnyClassIn(Double.class, stream.getClass());
  }

  @Test
  void isExactlyInstanceOf_should_check_the_original_stream() {
    // factory creates use internal classes
    assertThat(intStream).isExactlyInstanceOf(intStream.getClass());
  }

  @Test
  void isNotExactlyInstanceOf_should_check_the_original_stream() {
    assertThat(intStream).isNotExactlyInstanceOf(DoubleStream.class);

    Throwable error = catchThrowable(() -> assertThat(intStream).isNotExactlyInstanceOf(intStream.getClass()));

    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  void isNotInstanceOf_should_check_the_original_stream() {
    assertThat(intStream).isNotInstanceOf(String.class);
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
    DoubleStream stream = mock(DoubleStream.class);
    assertThat(stream).isSameAs(stream);
    verifyNoInteractions(stream);
  }

  @Test
  void isNotSameAs_should_check_the_original_stream_without_consuming_it() {
    DoubleStream stream = mock(DoubleStream.class);
    try {
      assertThat(stream).isNotSameAs(stream);
    } catch (AssertionError e) {
      verifyNoInteractions(stream);
      return;
    }
    Assertions.fail("Expected assertionError, because assert notSame on same stream.");
  }
}
