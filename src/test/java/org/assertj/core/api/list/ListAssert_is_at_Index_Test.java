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
    condition = new TestCondition<>();
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
