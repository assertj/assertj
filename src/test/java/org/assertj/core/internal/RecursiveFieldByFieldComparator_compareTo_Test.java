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
package org.assertj.core.internal;

import static java.util.Collections.EMPTY_MAP;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RecursiveFieldByFieldComparator_compareTo_Test {

  @SuppressWarnings("unchecked")
  private static final RecursiveFieldByFieldComparator RECURSIVE_FIELD_BY_FIELD_COMPARATOR = new RecursiveFieldByFieldComparator(EMPTY_MAP,
                                                                                                                                 new TypeComparators());

  @Test
  public void should_return_true_if_Objects_are_equal() {
    assertThat(RECURSIVE_FIELD_BY_FIELD_COMPARATOR.compare(new Foo("id", new Bar(1)),
                                                           new Foo("id", new Bar(1)))).isZero();
  }

  @Test
  public void should_return_false_if_Objects_are_not_equal() {
    assertThat(RECURSIVE_FIELD_BY_FIELD_COMPARATOR.compare(new Foo("id", new Bar(1)),
                                                           new Foo("id", new Bar(2)))).isNotZero();
  }

  @Test
  public void should_return_are_not_equal_if_first_Object_is_null_and_second_is_not() {
    assertThat(RECURSIVE_FIELD_BY_FIELD_COMPARATOR.compare(null, new Foo("id", new Bar(1)))).isNotZero();
  }

  @Test
  public void should_return_are_not_equal_if_second_Object_is_null_and_first_is_not() {
    assertThat(RECURSIVE_FIELD_BY_FIELD_COMPARATOR.compare(new Foo("id", new Bar(1)), null)).isNotZero();
  }

  @Test
  public void should_return_are_not_equal_if_Objects_do_not_have_the_same_properties() {
    assertThat(RECURSIVE_FIELD_BY_FIELD_COMPARATOR.compare(new Foo("id", new Bar(1)), 2)).isNotZero();
  }

  public static class Foo {
    public String id;
    public Bar bar;

    public Foo(String id, Bar bar) {
      this.id = id;
      this.bar = bar;
    }
  }

  public static class Bar {
    public int id;

    public Bar(int id) {
      this.id = id;
    }
  }

}
