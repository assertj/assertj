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
package org.assertj.core.api.iterable;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ConfigurableRecursiveFieldByFieldComparator;
import org.assertj.core.internal.IterableElementComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IterableAssert_usingRecursiveFieldByFieldElementComparator_Test extends IterableAssertBaseTest {

  private static final String DEFAULT_RECURSIVE_COMPARATOR_DESCRIPTION = CONFIGURATION_PROVIDER.representation()
                                                                                               .toStringOf(new ConfigurableRecursiveFieldByFieldComparator(new RecursiveComparisonConfiguration()));
  private Iterables iterablesBefore;

  @BeforeEach
  void before() {
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
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    ConfigurableRecursiveFieldByFieldComparator expectedComparator = new ConfigurableRecursiveFieldByFieldComparator(recursiveComparisonConfiguration);
    then(getIterables(assertions).getComparator()).isEqualTo(expectedComparator);
    then(getObjects(assertions).getComparisonStrategy()).extracting("elementComparator").isEqualTo(expectedComparator);
  }

  @Test
  void successful_isEqualTo_assertion_using_recursive_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", new Bar(1)));
    List<Foo> list2 = singletonList(new Foo("id", new Bar(1)));
    assertThat(list1).usingRecursiveFieldByFieldElementComparator().isEqualTo(list2);
  }

  @Test
  void successful_isIn_assertion_using_recursive_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", new Bar(1)));
    List<Foo> list2 = singletonList(new Foo("id", new Bar(1)));
    assertThat(list1).usingRecursiveFieldByFieldElementComparator().isIn(singletonList(list2));
  }

  @Test
  void failed_isEqualTo_assertion_using_recursive_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", new Bar(1)));
    List<Foo> list2 = singletonList(new Foo("id", new Bar(2)));

    AssertionError assertionError = expectAssertionError(() -> assertThat(list1).usingRecursiveFieldByFieldElementComparator()
                                                                                .isEqualTo(list2));

    then(assertionError).hasMessage(format(shouldBeEqualMessage("[Foo(id=id, bar=Bar(id=1))]", "[Foo(id=id, bar=Bar(id=2))]") +
                                           "%n" +
                                           "when comparing elements using %s", DEFAULT_RECURSIVE_COMPARATOR_DESCRIPTION));
  }

  @Test
  void failed_isIn_assertion_using_recursive_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", new Bar(1)));
    List<Foo> list2 = singletonList(new Foo("id", new Bar(2)));

    AssertionError assertionError = expectAssertionError(() -> assertThat(list1).usingRecursiveFieldByFieldElementComparator()
                                                                                .isIn(list(list2)));

    then(assertionError).hasMessage(format("%nExpecting actual:%n" +
                                           "  [Foo(id=id, bar=Bar(id=1))]%n" +
                                           "to be in:%n" +
                                           "  [[Foo(id=id, bar=Bar(id=2))]]%n" +
                                           "when comparing elements using %s", DEFAULT_RECURSIVE_COMPARATOR_DESCRIPTION));
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
      return "Bar(id=" + id + ")";
    }
  }
}
