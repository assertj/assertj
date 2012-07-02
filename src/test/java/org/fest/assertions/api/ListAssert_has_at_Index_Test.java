package org.fest.assertions.api;

import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.fest.assertions.data.Index;
import org.fest.assertions.internal.Lists;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.TestData.someIndex;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link ListAssert#has(Condition, Index)}</code>.
 *
 * @author Bo Gotthardt
 */
public class ListAssert_has_at_Index_Test {

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
    assertions.has(condition, index);
    verify(lists).assertHas(assertions.info, assertions.actual, condition, index);
  }

  @Test public void should_return_this() {
    ListAssert<Object> returned = assertions.has(condition, someIndex());
    assertSame(assertions, returned);
  }
}
