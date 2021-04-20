package org.assertj.core.internal.objectarrays;

import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertHasExactlyElementsOfTypes(AssertionInfo, Object[], Class...)}</code>.
 */
class ObjectArrays_assertHasExactlyElementsOfTypes_Test extends ObjectArraysBaseTest
{
  private static final Object[] arrayOfObjects = {"a", 'b', new LinkedList<>(), 10L};

  @Test
  void should_pass_if_actual_has_exactly_elements_of_the_expected_types_in_order()
  {
    arrays.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects, String.class, Character.class, LinkedList.class, Long.class);
  }

  @Test
  void should_fail_if_actual_is_null()
  {
    // GIVEN
    Object[] array = null;
    // GIVEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasExactlyElementsOfTypes(someInfo(), array, String.class));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_one_element_in_actual_does_not_belong_to_the_expected_type() {
    // GIVEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects,
      String.class, Character.class, LinkedList.class, Double.class));
    // THEN
    Object[] expected = {String.class, Character.class, LinkedList.class, Double.class};
    assertThat(error).hasMessage(shouldContainExactly(arrayOfObjects, asList(expected), newArrayList(Double.class),newArrayList(Long.class)).create());
  }

  @Test
  void should_fail_if_types_of_elements_are_not_in_the_same_order_as_expected()
  {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arrays.assertHasExactlyElementsOfTypes(someInfo(), arrayOfObjects,
      Character.class, String.class, LinkedList.class, Long.class));

    assertThat(error).isInstanceOf(AssertionError.class);

    verify(failures).failure(info, elementsDifferAtIndex(String.class, Character.class, 0));
  }

  @Test
  void should_fail_if_actual_has_more_elements_than_expected()
  {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arrays.assertHasExactlyElementsOfTypes(info, arrayOfObjects,String.class, Character.class));
    Object[] expected = {String.class, Character.class};
    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(arrayOfObjects, asList(expected),
      newArrayList(), newArrayList(LinkedList.class, Long.class),
      StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_actual_contains_all_types_but_has_less_elements_than_expected()
  {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() ->
      arrays.assertHasExactlyElementsOfTypes(info, arrayOfObjects,String.class, Character.class,LinkedList.class, Long.class, Long.class));
    Object[] expected = {String.class, Character.class,LinkedList.class, Long.class, Long.class};
    assertThat(error).isInstanceOf(AssertionError.class);

    verify(failures).failure(info, shouldContainExactly(arrayOfObjects, asList(expected),
      newArrayList(Long.class), newArrayList(),
      StandardComparisonStrategy.instance()));
  }
}
