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
package org.assertj.core.internal.iterables;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Iterator;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsExactly(AssertionInfo, Iterable, Object[])}</code>.
 *
 * @author Joel Costigliola
 */
public class Iterables_assertContainsExactly_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_actual_contains_exactly_given_values() {
    iterables.assertContainsExactly(someInfo(), actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_nonrestartable_actual_contains_exactly_given_values() {
    iterables.assertContainsExactly(someInfo(), createSinglyIterable(actual), array("Luke", "Yoda", "Leia"));
  }

  private Iterable<String> createSinglyIterable(final List<String> values) {
    // can't use Iterable<> for anonymous class in java 8
    return new Iterable<String>() {
      private boolean isIteratorCreated = false;

      @Override
      public Iterator<String> iterator() {
        if (isIteratorCreated) throw new IllegalArgumentException("Cannot create two iterators on a singly-iterable sequence");
        isIteratorCreated = true;
        return new Iterator<String>() {
          private final Iterator<String> listIterator = values.iterator();

          @Override
          public boolean hasNext() {
            return listIterator.hasNext();
          }

          @Override
          public String next() {
            return listIterator.next();
          }
        };
      }
    };
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_with_null_elements() {
    iterables.assertContainsExactly(someInfo(), actual, array("Luke", "Yoda", "Leia"));
    actual.add(null);
    iterables.assertContainsExactly(someInfo(), actual, array("Luke", "Yoda", "Leia", null));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual.clear();
    iterables.assertContainsExactly(someInfo(), actual, array());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertContainsExactly(someInfo(), actual,
                                                                                                     emptyArray()));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> iterables.assertContainsExactly(someInfo(), emptyList(), null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertContainsExactly(someInfo(), null,
                                                                                                     array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };

    Throwable error = catchThrowable(() -> iterables.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    List<String> notFound = newArrayList("Han");
    List<String> notExpected = newArrayList("Leia");
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected), notFound, notExpected));
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_in_different_order() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Leia", "Yoda" };

    Throwable error = catchThrowable(() -> iterables.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, elementsDifferAtIndex("Yoda", "Leia", 1));
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };

    Throwable error = catchThrowable(() -> iterables.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                        newArrayList(), newArrayList("Luke")));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(someInfo(), actual,
                                                                         array("LUKE", "YODA", "Leia"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };

    Throwable error = catchThrowable(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(info, actual,
                                                                                                                expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                        newArrayList("Han"), newArrayList("Leia"),
                                                        comparisonStrategy));
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_in_different_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Leia", "Yoda" };

    Throwable error = catchThrowable(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(info, actual,
                                                                                                                expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, elementsDifferAtIndex("Yoda", "Leia", 1, comparisonStrategy));
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Leia", "Luke");
    Object[] expected = { "LUKE", "Leia" };

    Throwable error = catchThrowable(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(info, actual,
                                                                                                                expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                        newArrayList(), newArrayList("Luke"),
                                                        comparisonStrategy));
  }

}
