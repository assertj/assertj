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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class Assertions_assertThat_with_Stream_startsWith_Test {

  Stream<String> infiniteStream = Stream.generate(() -> "");

  // TODO it is not possible for startsWith to support both infinite streams and assertion chaining
  // assertion chaining has been chosen over infinite streams support
  @Test
  @Disabled
  void startsWith_should_work_with_infinite_streams() {
    assertThat(infiniteStream).startsWith("", "");
  }

  @Test
  void should_reuse_stream_after_assertion() {
    Stream<String> names = Stream.of("Luke", "Leia");
    assertThat(names).startsWith(array("Luke", "Leia"))
                     .endsWith("Leia");
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(infiniteStream).startsWith((String[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_sequence_are_empty() {
    Stream<Object> empty = Stream.of();
    assertThat(empty).startsWith(emptyArray());
  }

  @Test
  void should_fail_if_sequence_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Stream<String> names = Stream.of("Luke", "Leia");
      assertThat(names).startsWith();
    });
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Stream<Object> names = null;
      assertThat(names).startsWith(emptyArray());
    }).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      String[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
      Stream<String> names = Stream.of("Luke", "Leia");
      assertThat(names).startsWith(sequence);
    });
  }

  @Test
  void should_fail_if_actual_does_not_start_with_sequence() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      String[] sequence = { "Han", "C-3PO" };
      Stream<String> names = Stream.of("Luke", "Leia");
      assertThat(names).startsWith(sequence);
    });
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      String[] sequence = { "Luke", "Yoda" };
      Stream<String> names = Stream.of("Luke", "Leia");
      assertThat(names).startsWith(sequence);
    });
  }

  @Test
  void should_pass_if_actual_starts_with_sequence() {
    Stream<String> names = Stream.of("Luke", "Leia", "Yoda");
    assertThat(names).startsWith(array("Luke", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    Stream<String> names = Stream.of("Luke", "Leia");
    assertThat(names).startsWith(array("Luke", "Leia"));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_fail_if_actual_does_not_start_with_sequence_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Stream<String> names = Stream.of("Luke", "Leia");
      String[] sequence = { "Han", "C-3PO" };
      assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
    });
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Stream<String> names = Stream.of("Luke", "Leia");
      String[] sequence = { "Luke", "Obi-Wan", "Han" };
      assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
    });
  }

  @Test
  void should_pass_if_actual_starts_with_sequence_according_to_custom_comparison_strategy() {
    Stream<String> names = Stream.of("Luke", "Leia");
    String[] sequence = { "LUKE" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    Stream<String> names = Stream.of("Luke", "Leia");
    String[] sequence = { "LUKE", "lEIA" };
    assertThat(names).usingElementComparator(CaseInsensitiveStringComparator.instance).startsWith(sequence);
  }

  @Test
  void test_issue_245() {
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
  void test_issue_236() {
    List<Foo> stream2 = newArrayList(new Foo("id", 2));
    assertThat(Stream.of(new Foo("id", 1))).usingElementComparatorOnFields("id")
                                           .isEqualTo(stream2);
    assertThat(Stream.of(new Foo("id", 1))).usingElementComparatorIgnoringFields("bar")
                                           .isEqualTo(stream2);
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
