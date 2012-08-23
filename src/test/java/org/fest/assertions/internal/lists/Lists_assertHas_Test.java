package org.fest.assertions.internal.lists;

import static java.util.Collections.emptyList;

import static org.fest.assertions.data.Index.atIndex;
import static org.fest.assertions.error.ShouldHaveAtIndex.shouldHaveAtIndex;
import static org.fest.util.FailureMessages.actualIsEmpty;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someIndex;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Lists.newArrayList;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.fest.assertions.data.Index;
import org.fest.assertions.internal.Lists;
import org.fest.assertions.internal.ListsBaseTest;

/**
 * Tests for <code>{@link Lists#assertHas(AssertionInfo, List, Condition, Index)}</code>.
 * 
 * @author Bo Gotthardt
 */
public class Lists_assertHas_Test extends ListsBaseTest {

  private static TestCondition<String> condition;
  private static List<String> actual = newArrayList("Yoda", "Luke", "Leia");

  @BeforeClass
  public static void setUpOnce() {
    condition = new TestCondition<String>();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    lists.assertHas(someInfo(), null, condition, someIndex());
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    thrown.expectAssertionError(actualIsEmpty());
    List<String> empty = emptyList();
    lists.assertHas(someInfo(), empty, condition, someIndex());
  }

  @Test
  public void should_throw_error_if_Index_is_null() {
    thrown.expectNullPointerException("Index should not be null");
    lists.assertHas(someInfo(), actual, condition, null);
  }

  @Test
  public void should_throw_error_if_Index_is_out_of_bounds() {
    thrown.expectIndexOutOfBoundsException("Index should be between <0> and <2> (inclusive,) but was <6>");
    lists.assertHas(someInfo(), actual, condition, atIndex(6));
  }

  @Test
  public void should_throw_error_if_Condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    lists.assertHas(someInfo(), actual, null, someIndex());
  }

  @Test
  public void should_fail_if_actual_does_not_satisfy_condition_at_index() {
    condition.shouldMatch(false);
    AssertionInfo info = someInfo();
    Index index = atIndex(1);
    try {
      lists.assertHas(info, actual, condition, index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveAtIndex(actual, condition, index, "Luke"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_satisfies_condition_at_index() {
    condition.shouldMatch(true);
    lists.assertHas(someInfo(), actual, condition, someIndex());
  }
}
