/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.tests.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class ListAssert_isEqualTo_Test {

  @Test
  void should_pass_with_stream_without_consuming_it() {
    // GIVEN
    Stream<?> actual = mock();
    // WHEN
    assertThat(actual).isEqualTo(actual);
    // THEN
    verifyNoInteractions(actual);
  }

  @Test
  void should_pass_with_stream_when_using_element_comparator_on_fields() {
    // GIVEN
    Foo foo1 = new Foo("id", 1);
    foo1._f2 = "foo1";
    Foo foo2 = new Foo("id", 2);
    foo2._f2 = "foo1";
    // WHEN/THEN
    assertThat(Stream.of(foo1)).usingElementComparatorOnFields("_f2")
                               .isEqualTo(newArrayList(foo2));
    assertThat(Stream.of(foo1)).usingElementComparatorOnFields("id")
                               .isEqualTo(newArrayList(foo2));
  }

  @Test
  void should_pass_with_stream_when_using_element_comparator_ignoring_fields() {
    // GIVEN
    Foo actual = new Foo("id", 1);
    Foo expected = new Foo("id", 2);
    // WHEN/THEN
    assertThat(Stream.of(actual)).usingElementComparatorIgnoringFields("bar")
                                 .isEqualTo(newArrayList(expected));
  }

  public static class Foo {
    private String id;
    private int bar;
    public String _f2;

    Foo(String id, int bar) {
      super();
      this.id = id;
      this.bar = bar;
    }

    public String getId() {
      return id;
    }

    public int getBar() {
      return bar;
    }
  }

}
