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
package org.assertj.core.api.atomic.referencearray;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.AlwaysEqualStringComparator.ALWAY_EQUALS;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.internal.AtomicReferenceArrayElementComparisonStrategy;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.junit.Before;
import org.junit.Test;

public class AtomicReferenceArrayAssert_usingRecursiveFieldByFieldElementComparator_Test
    extends AtomicReferenceArrayAssertBaseTest {

  private ObjectArrays arraysBefore;

  @Before
  public void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.usingRecursiveFieldByFieldElementComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(arraysBefore).isNotSameAs(getArrays(assertions));
    assertThat(getArrays(assertions).getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    assertThat(getObjects(assertions).getComparisonStrategy()).isInstanceOf(AtomicReferenceArrayElementComparisonStrategy.class);
  }

  @Test
  public void successful_isEqualTo_assertion_using_recursive_field_by_field_element_comparator() {
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", new Bar(1)));
    Foo[] array2 = { new Foo("id", new Bar(1)) };
    assertThat(array1).usingRecursiveFieldByFieldElementComparator().isEqualTo(array2);
  }

  @Test
  public void successful_isIn_assertion_using_recursive_field_by_field_element_comparator() {
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", new Bar(1)));
    Foo[] array2 = { new Foo("id", new Bar(1)) };
    assertThat(array1).usingRecursiveFieldByFieldElementComparator().isIn(new Object[] { (array2) });
  }

  @Test
  public void failed_isEqualTo_assertion_using_recursive_field_by_field_element_comparator() {
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", new Bar(1)));
    Foo[] array2 = { new Foo("id", new Bar(2)) };
    try {
      assertThat(array1).usingRecursiveFieldByFieldElementComparator().isEqualTo(array2);
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
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", new Bar(1)));
    Foo[] array2 = { new Foo("id", new Bar(2)) };
    try {
      assertThat(array1).usingRecursiveFieldByFieldElementComparator().isIn(new Object[] { array2 });
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("%nExpecting:%n" +
                                      " <[Foo(id=id, bar=Bar [id=1])]>%n" +
                                      "to be in:%n" +
                                      " <[[Foo(id=id, bar=Bar [id=2])]]>%n" +
                                      "when comparing elements using 'recursive field/property by field/property comparator on all fields/properties'"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_fields_of_elements_when_using_recursive_field_by_field_element_comparator() {
    Foo actual = new Foo("1", new Bar(1));
    Foo other = new Foo("1", new Bar(2));
    final class AlwaysEqualIntegerComparator implements Comparator<Integer> {
      @Override
      public int compare(Integer o1, Integer o2) {
        return 0;
      }
    }

    assertThat(atomicArrayOf(actual)).usingComparatorForElementFieldsWithNames(new AlwaysEqualIntegerComparator(),
                                                                               "bar.id")
                                     .usingRecursiveFieldByFieldElementComparator()
                                     .contains(other);
  }

  @Test
  public void comparators_for_element_field_names_should_have_precedence_over_comparators_for_element_field_types_when_using_recursive_field_by_field_element_comparator() {
    Comparator<String> comparator = new Comparator<String>() {
      public int compare(String o1, String o2) {
        return o1.compareTo(o2);
      }
    };
    Foo actual = new Foo("1", new Bar(1));
    Foo other = new Foo("2", new Bar(1));

    assertThat(atomicArrayOf(actual)).usingComparatorForElementFieldsWithNames(ALWAY_EQUALS, "id")
                                     .usingComparatorForElementFieldsWithType(comparator, String.class)
                                     .usingRecursiveFieldByFieldElementComparator()
                                     .contains(other);
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_element_fields_with_specified_type_when_using_recursive_field_by_field_element_comparator() {
    Foo actual = new Foo("1", new Bar(1));
    Foo other = new Foo("2", new Bar(1));

    assertThat(atomicArrayOf(actual)).usingComparatorForElementFieldsWithType(ALWAY_EQUALS, String.class)
                                     .usingRecursiveFieldByFieldElementComparator()
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
      return "Bar [id=" + id + "]";
    }
  }
}
