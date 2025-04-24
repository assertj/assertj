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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.api.comparisonstrategy.AtomicReferenceArrayElementComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ConfigurableRecursiveFieldByFieldComparator;
import org.assertj.core.internal.ObjectArrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AtomicReferenceArrayAssert_usingRecursiveFieldByFieldElementComparator_Test
    extends AtomicReferenceArrayAssertBaseTest {

  private static final String DEFAULT_RECURSIVE_COMPARATOR_DESCRIPTION = CONFIGURATION_PROVIDER.representation()
                                                                                               .toStringOf(new ConfigurableRecursiveFieldByFieldComparator(new RecursiveComparisonConfiguration()));
  private ObjectArrays arraysBefore;

  @BeforeEach
  void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.usingRecursiveFieldByFieldElementComparator();
  }

  @Override
  protected void verify_internal_effects() {
    then(arraysBefore).isNotSameAs(getArrays(assertions));
    then(getArrays(assertions).getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    then(getObjects(assertions).getComparisonStrategy()).isInstanceOf(AtomicReferenceArrayElementComparisonStrategy.class);
  }

  @Test
  void successful_isEqualTo_assertion_using_recursive_field_by_field_element_comparator() {
    // GIVEN
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", new Bar(1)));
    Foo[] array2 = { new Foo("id", new Bar(1)) };
    // WHEN/THEN
    then(array1).usingRecursiveFieldByFieldElementComparator().isEqualTo(array2);
  }

  @Test
  void successful_isIn_assertion_using_recursive_field_by_field_element_comparator() {
    // GIVEN
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", new Bar(1)));
    Foo[] array2 = { new Foo("id", new Bar(1)) };
    // WHEN/THEN
    then(array1).usingRecursiveFieldByFieldElementComparator().isIn(new Object[] { (array2) });
  }

  @Test
  void failed_isEqualTo_assertion_using_recursive_field_by_field_element_comparator() {
    // GIVEN
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", new Bar(1)));
    Foo[] array2 = { new Foo("id", new Bar(2)) };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(array1).usingRecursiveFieldByFieldElementComparator()
                                                                                 .isEqualTo(array2));
    // THEN
    then(assertionError).hasMessage(format(shouldBeEqualMessage("[Foo(id=id, bar=Bar(id=1))]", "[Foo(id=id, bar=Bar(id=2))]") +
                                           "%n" +
                                           "when comparing elements using %s", DEFAULT_RECURSIVE_COMPARATOR_DESCRIPTION));
  }

  @Test
  void failed_isIn_assertion_using_recursive_field_by_field_element_comparator() {
    // GIVEN
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", new Bar(1)));
    Foo[] array2 = { new Foo("id", new Bar(2)) };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(array1).usingRecursiveFieldByFieldElementComparator()
                                                                                 .isIn(new Object[] { array2 }));
    // THEN
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
