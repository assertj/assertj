package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * Tests for <code>{@link ObjectArrays#assertHasExactlyElementsOfTypes(AssertionInfo, Object[], Class...)}</code>.
 */
class ObjectArrays_assertHasExactlyElementsOfTypes_Test extends ObjectArraysBaseTest
{
  private static final Object[] arrayOfObjects = {"a", new LinkedList<>(),10L};

  @Test
  void should_pass_if_actual_has_exactly_elements_of_the_expected_types_in_order()
  {
    arrays.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, String.class, LinkedList.class, Long.class);
  }

  @Test
  void should_fail_if_actual_is_null()
  {
    // GIVEN
    Object[] array = null;
    // WHEN
    AssertionError error = expectAssertionError(() ->
      arrays.assertHasExactlyElementsOfTypes(someInfo(), array, String.class));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_one_element_in_actual_does_not_belong_to_the_expected_type() {
    // WHEN
    AssertionError error = expectAssertionError(() ->
      arrays.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, String.class, LinkedList.class, Double.class));
    // THEN
    Class<?>[] expected = {String.class, LinkedList.class, Double.class};
    then(error).hasMessage(shouldContainExactly(arrayOfObjects, asList(expected), newArrayList(Double.class),newArrayList(Long.class)).create());
  }

  @Test
  void should_fail_if_types_of_elements_are_not_in_the_same_order_as_expected()
  {
    // WHEN
    AssertionError error = expectAssertionError(() ->
      arrays.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, LinkedList.class, String.class, Long.class));
    // THEN
    then(error).hasMessage(elementsDifferAtIndex(String.class, LinkedList.class,0).create());
  }

  @Test
  void should_fail_if_actual_has_more_elements_than_expected()
  {
    // WHEN
    AssertionError error = expectAssertionError(() ->
      arrays.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, String.class));
    // THEN
    Class<?>[] expected = {String.class};
    then(error).hasMessage(shouldContainExactly(arrayOfObjects, asList(expected), newArrayList(), newArrayList(LinkedList.class, Long.class)).create());
  }

  @Test
  void should_fail_if_actual_contains_all_types_but_has_less_elements_than_expected()
  {
    // WHEN
    AssertionError error = expectAssertionError(() ->
      arrays.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, String.class, LinkedList.class, Long.class, Long.class));
    // THEN
    Class<?>[] expected = {String.class, LinkedList.class, Long.class, Long.class};
    then(error).hasMessage(shouldContainExactly(arrayOfObjects, asList(expected), newArrayList(Long.class), newArrayList()).create());
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_has_exactly_elements_of_the_expected_types_in_order_according_to_custom_comparison_strategy()
  {
    arraysWithCustomComparisonStrategy.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, String.class, LinkedList.class, Long.class);
  }

  @Test
  void should_fail_if_one_element_in_actual_does_not_belong_to_the_expected_type_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError error = expectAssertionError(() ->
      arraysWithCustomComparisonStrategy.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, String.class, LinkedList.class, Double.class));
    // THEN
    Class<?>[] expected = {String.class, LinkedList.class, Double.class};
    then(error).hasMessage(shouldContainExactly(arrayOfObjects, asList(expected), newArrayList(Double.class), newArrayList(Long.class), StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_types_of_elements_are_not_in_the_same_order_as_expected_according_to_custom_comparison_strategy()
  {
    // WHEN
    AssertionError error = expectAssertionError(() ->
      arraysWithCustomComparisonStrategy.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, LinkedList.class, String.class, Long.class));
    // THEN
    then(error).hasMessage(elementsDifferAtIndex(String.class, LinkedList.class,0, StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_contains_all_types_but_has_less_elements_than_expected_according_to_custom_comparison_strategy()
  {
    // WHEN
    AssertionError error = expectAssertionError(() ->
      arraysWithCustomComparisonStrategy.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, String.class, LinkedList.class, Long.class, Long.class));
    // THEN
    Class<?>[] expected = {String.class, LinkedList.class, Long.class, Long.class};
    then(error).hasMessage(shouldContainExactly(arrayOfObjects, asList(expected), newArrayList(Long.class), newArrayList(), StandardComparisonStrategy.instance()).create());
  }
}
