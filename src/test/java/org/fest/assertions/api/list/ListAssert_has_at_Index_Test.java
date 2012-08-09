package org.fest.assertions.api.list;

import static org.fest.assertions.test.TestData.someIndex;

import static org.mockito.Mockito.verify;

import org.junit.BeforeClass;

import org.fest.assertions.api.ListAssert;
import org.fest.assertions.api.ListAssertBaseTest;
import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.fest.assertions.data.Index;

/**
 * Tests for <code>{@link ListAssert#has(Condition, Index)}</code>.
 * 
 * @author Bo Gotthardt
 */
public class ListAssert_has_at_Index_Test extends ListAssertBaseTest {

  private static Condition<Object> condition;
  private static Index index;

  @BeforeClass
  public static void setUpOnce() {
    condition = new TestCondition<Object>();
    index = someIndex();
  }

  @Override
  protected ListAssert<String> invoke_api_method() {
    return assertions.has(condition, index);
  }

  @Override
  protected void verify_internal_effects() {
    verify(lists).assertHas(getInfo(assertions), getActual(assertions), condition, index);
  }
}
