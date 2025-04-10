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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.api.comparisonstrategy.AtomicReferenceArrayElementComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ConfigurableRecursiveFieldByFieldComparator;
import org.assertj.core.internal.ObjectArrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AtomicReferenceArrayAssert_usingRecursiveFieldByFieldElementComparator_with_RecursiveComparisonConfiguration_Test
    extends AtomicReferenceArrayAssertBaseTest {

  private ObjectArrays arraysBefore;
  private RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();

  @BeforeEach
  void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.usingRecursiveFieldByFieldElementComparator(recursiveComparisonConfiguration);
  }

  @Override
  protected void verify_internal_effects() {
    then(arraysBefore).isNotSameAs(getArrays(assertions));
    then(getArrays(assertions).getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    then(getObjects(assertions).getComparisonStrategy()).isInstanceOf(AtomicReferenceArrayElementComparisonStrategy.class);
    ConfigurableRecursiveFieldByFieldComparator expectedComparator = new ConfigurableRecursiveFieldByFieldComparator(recursiveComparisonConfiguration);
    then(getArrays(assertions).getComparator()).isEqualTo(expectedComparator);
    then(getObjects(assertions).getComparisonStrategy()).extracting("elementComparator").isEqualTo(expectedComparator);
  }

  @Test
  void should_be_able_to_use_specific_RecursiveComparisonConfiguration_when_using_recursive_field_by_field_element_comparator() {
    // GIVEN
    Foo actual = new Foo("1", new Bar(1));
    Foo other = new Foo("2", new Bar(1));
    RecursiveComparisonConfiguration configuration = new RecursiveComparisonConfiguration();
    configuration.ignoreFields("id");
    // WHEN/THEN
    then(atomicArrayOf(actual)).usingRecursiveFieldByFieldElementComparator(configuration)
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
