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
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.IterableAssert.LazyIterable;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.StringStream;
import org.junit.Rule;
import org.junit.Test;

public class Assertions_assertThat_with_Stream_Test {

  @Rule
  public ExpectedException thrown = none();

  private StringStream stringStream = new StringStream();

  @Test
  public void should_create_Assert() {
    Object assertions = assertThat(Stream.of("Luke", "Leia"));
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
  public void isInstanceOf_should_check_the_original_stream_without_consuming_it() {
    Stream<?> stream = mock(Stream.class);
    assertThat(stream).isInstanceOf(Stream.class);
    verifyZeroInteractions(stream);
  }

  @Test
  public void isInstanceOfAny_should_check_the_original_stream_without_consuming_it() {
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

  @Test
  public void test_issue_245() throws Exception {
    Foo foo1 = new Foo("id", 1);
    foo1._f2 = "foo1";
    Foo foo2 = new Foo("id", 2);
    foo2._f2 = "foo1";
    List<Foo> stream2 = newArrayList(foo2);
    assertThat(Stream.of(foo1)).usingElementComparatorOnFields("_f2").isEqualTo(stream2);
    assertThat(Stream.of(foo1)).usingElementComparatorOnFields("id").isEqualTo(stream2);
    assertThat(Stream.of(foo1)).usingElementComparatorIgnoringFields("bar").isEqualTo(stream2);
  }

  @Test
  public void test_issue_236() throws Exception {
    List<Foo> stream2 = newArrayList(new Foo("id", 2));
    assertThat(Stream.of(new Foo("id", 1))).usingElementComparatorOnFields("id")
                                           .isEqualTo(stream2);
    assertThat(Stream.of(new Foo("id", 1))).usingElementComparatorIgnoringFields("bar")
                                           .isEqualTo(stream2);
  }

  @Test
  public void stream_with_upper_bound_assertions() {
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
