package org.assertj.core.api.list;

import static org.assertj.core.test.TestData.someIndex;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.Condition;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ListAssertBaseTest;
import org.assertj.core.api.TestCondition;
import org.assertj.core.data.Index;
import org.junit.BeforeClass;


/**
 * Tests for <code>{@link ListAssert#is(Condition, Index)}</code>.
 * 
 * @author Bo Gotthardt
 */
public class ListAssert_is_at_Index_Test extends ListAssertBaseTest {

  private static Condition<Object> condition;
  private static Index index;

  @BeforeClass
  public static void setUpOnce() {
    condition = new TestCondition<Object>();
    index = someIndex();
  }

  @Override
  protected ListAssert<String> invoke_api_method() {
    return assertions.is(condition, index);
  }

  @Override
  protected void verify_internal_effects() {
    verify(lists).assertIs(getInfo(assertions), getActual(assertions), condition, index);
  }
}
