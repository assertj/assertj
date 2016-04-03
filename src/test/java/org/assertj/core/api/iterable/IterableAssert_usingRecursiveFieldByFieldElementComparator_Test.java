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
package org.assertj.core.api.iterable;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import java.util.List;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IterableElementComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.junit.Before;
import org.junit.Test;

public class IterableAssert_usingRecursiveFieldByFieldElementComparator_Test extends IterableAssertBaseTest {

  private Iterables iterablesBefore;

  @Before
  public void before() {
    iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.usingRecursiveFieldByFieldElementComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(iterablesBefore).isNotSameAs(getIterables(assertions));
    assertThat(getIterables(assertions).getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    assertThat(getObjects(assertions).getComparisonStrategy()).isInstanceOf(IterableElementComparisonStrategy.class);
  }

  @Test
  public void successful_isEqualTo_assertion_using_recursive_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", new Bar(1)));
    List<Foo> list2 = singletonList(new Foo("id", new Bar(1)));
    assertThat(list1).usingRecursiveFieldByFieldElementComparator().isEqualTo(list2);
  }

  @Test
  public void successful_isIn_assertion_using_recursive_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", new Bar(1)));
    List<Foo> list2 = singletonList(new Foo("id", new Bar(1)));
    assertThat(list1).usingRecursiveFieldByFieldElementComparator().isIn(singletonList(list2));
  }

  @Test
  public void failed_isEqualTo_assertion_using_recursive_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", new Bar(1)));
    List<Foo> list2 = singletonList(new Foo("id", new Bar(2)));
    try {
      assertThat(list1).usingRecursiveFieldByFieldElementComparator().isEqualTo(list2);
    } catch (AssertionError e) {
      // @format:off
      assertThat(e).hasMessage(format("%nExpecting:%n" +
                                      " <[Foo(id=id, bar=Bar [id=1])]>%n" +
                                      "to be equal to:%n" +
                                      " <[Foo(id=id, bar=Bar [id=2])]>%n" +
                                      "when comparing elements using 'recursive field/property by field/property comparator on all fields/properties' but was not."));
      // @format:on
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void failed_isIn_assertion_using_recursive_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", new Bar(1)));
    List<Foo> list2 = singletonList(new Foo("id", new Bar(2)));
    try {
      assertThat(list1).usingRecursiveFieldByFieldElementComparator().isIn(singletonList(list2));
    } catch (AssertionError e) {
      assertThat(e).hasMessage(String.format("%nExpecting:%n" +
                                             " <[Foo(id=id, bar=Bar [id=1])]>%n" +
                                             "to be in:%n" +
                                             " <[[Foo(id=id, bar=Bar [id=2])]]>%n" +
                                             "when comparing elements using 'recursive field/property by field/property comparator on all fields/properties'"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  public static class Foo {
    public String id;
    public Bar bar;

    public Foo(String id, Bar bar) {
      this.id = id;
      this.bar = bar;
    }

    @Override
    public String toString() {
      return "Foo(id=" + id + ", bar=" + bar + ")";
    }
  }

  public static class Bar {
    public int id;

    public Bar(int id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return "Bar [id=" + id + "]";
    }
  }
}
