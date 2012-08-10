package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.TestData.someIndex;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.fest.assertions.data.Index;
import org.fest.assertions.internal.Lists;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link ListAssert#is(Condition, Index)}</code>.
 *
 * @author Bo Gotthardt
 */
public class ListAssert_is_at_Index_Test {

  private static Condition<Object> condition;

  private Lists lists;
  private ListAssert<Object> assertions;

  @BeforeClass public static void setUpOnce() {
    condition = new TestCondition<Object>();
  }

  @Before public void setUp() {
    lists = mock(Lists.class);
    assertions = new ListAssert<Object>(Collections.<Object>emptyList());
    assertions.lists = lists;
  }

  @Test public void should_verify_that_actual_satisfies_condition_at_index() {
    Index index = someIndex();
    assertions.is(condition, index);
    verify(lists).assertIs(assertions.info, assertions.actual, condition, index);
  }

  @Test public void should_return_this() {
    ListAssert<Object> returned = assertions.is(condition, someIndex());
    assertSame(assertions, returned);
  }
}
