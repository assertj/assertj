/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
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

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.assertj.tests.core.testkit.StringStream;
import org.junit.jupiter.api.Test;

class Assertions_assertThat_with_Stream_Test {

  private StringStream stringStream = new StringStream();

  @Test
  void should_create_Assert() {
    Object assertions = assertThat(Stream.of("Luke", "Leia"));
    assertThat(assertions).isNotNull();
  }

  @Test
  void isEqualTo_should_honor_comparing_the_same_mocked_stream() {
    Stream<?> stream = mock();
    assertThat(stream).isEqualTo(stream);
  }

  @Test
  void stream_can_be_asserted_twice() {
    Stream<String> names = Stream.of("Luke", "Leia");
    assertThat(names).containsExactly("Luke", "Leia")
                     .containsExactly("Luke", "Leia");
  }

  @Test
  void should_not_consume_stream_when_asserting_non_null() {
    Stream<?> stream = mock();
    assertThat(stream).isNotNull();
    verifyNoInteractions(stream);
  }

  @Test
  void isInstanceOf_should_check_the_original_stream_without_consuming_it() {
    Stream<?> stream = mock();
    assertThat(stream).isInstanceOf(Stream.class);
    verifyNoInteractions(stream);
  }

  @Test
  void isInstanceOfAny_should_check_the_original_stream_without_consuming_it() {
    Stream<?> stream = mock();
    assertThat(stream).isInstanceOfAny(Stream.class, String.class);
    verifyNoInteractions(stream);
  }

  @Test
  void isOfAnyClassIn_should_check_the_original_stream_without_consuming_it() {
    assertThat(stringStream).isOfAnyClassIn(Double.class, StringStream.class);
  }

  @Test
  void isExactlyInstanceOf_should_check_the_original_stream() {
    assertThat(new StringStream()).isExactlyInstanceOf(StringStream.class);
  }

  @Test
  void isNotExactlyInstanceOf_should_check_the_original_stream() {
    assertThat(stringStream).isNotExactlyInstanceOf(Stream.class);

    Throwable error = catchThrowable(() -> assertThat(stringStream).isNotExactlyInstanceOf(StringStream.class));

    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  void isNotInstanceOf_should_check_the_original_stream() {
    assertThat(stringStream).isNotInstanceOf(Long.class);
  }

  @Test
  void isNotInstanceOfAny_should_check_the_original_stream() {
    assertThat(stringStream).isNotInstanceOfAny(Long.class, String.class);
  }

  @Test
  void isNotOfAnyClassIn_should_check_the_original_stream() {
    assertThat(stringStream).isNotOfAnyClassIn(Long.class, String.class);
  }

  @Test
  void isSameAs_should_check_the_original_stream_without_consuming_it() {
    Stream<?> stream = mock();
    assertThat(stream).isSameAs(stream);
    verifyNoInteractions(stream);
  }

  @Test
  void isNotSameAs_should_check_the_original_stream_without_consuming_it() {
    Stream<?> stream = mock();
    try {
      assertThat(stream).isNotSameAs(stream);
    } catch (AssertionError e) {
      verifyNoInteractions(stream);
      return;
    }
    Assertions.fail("Expected assertionError, because assert notSame on same stream.");
  }

  @Test
  void stream_with_upper_bound_assertions() {
    // GIVEN
    Stream<? extends Foo> foos = Stream.of();
    // THEN
    assertThat(foos).hasSize(0);
  }

  public static class Foo {
    private String id;
    private int bar;
    public String _f2;

    public String getId() {
      return id;
    }

    public int getBar() {
      return bar;
    }

    public Foo(String id, int bar) {
      super();
      this.id = id;
      this.bar = bar;
    }

    @Override
    public String toString() {
      return "Foo [id=" + id + ", bar=" + bar + "]";
    }
  }

}
