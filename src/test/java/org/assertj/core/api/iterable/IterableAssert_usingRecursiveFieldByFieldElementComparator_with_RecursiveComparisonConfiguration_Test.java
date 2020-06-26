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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ConfigurableRecursiveFieldByFieldComparator;
import org.assertj.core.internal.IterableElementComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IterableAssert_usingRecursiveFieldByFieldElementComparator_with_RecursiveComparisonConfiguration_Test extends IterableAssertBaseTest {

  private Iterables iterablesBefore;
  private RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();

  @BeforeEach
  public void before() {
    iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.usingRecursiveFieldByFieldElementComparator(recursiveComparisonConfiguration);
  }

  @Override
  protected void verify_internal_effects() {
    then(iterablesBefore).isNotSameAs(getIterables(assertions));
    then(getIterables(assertions).getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    then(getObjects(assertions).getComparisonStrategy()).isInstanceOf(IterableElementComparisonStrategy.class);
    ConfigurableRecursiveFieldByFieldComparator expectedComparator = new ConfigurableRecursiveFieldByFieldComparator(recursiveComparisonConfiguration);
    then(getIterables(assertions).getComparator()).isEqualTo(expectedComparator);
    then(getObjects(assertions).getComparisonStrategy()).extracting("elementComparator").isEqualTo(expectedComparator);
  }

  @Test
  public void should_be_able_to_use_specific_RecursiveComparisonConfiguration_when_using_recursive_field_by_field_element_comparator() {
    // GIVEN
    Foo actual = new Foo("1", new Bar(1));
    Foo other = new Foo("2", new Bar(1));
    RecursiveComparisonConfiguration configuration = new RecursiveComparisonConfiguration();
    configuration.ignoreFields("id");
    // WHEN/THEN
    then(singletonList(actual)).usingRecursiveFieldByFieldElementComparator(configuration)
                               .contains(other);
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
