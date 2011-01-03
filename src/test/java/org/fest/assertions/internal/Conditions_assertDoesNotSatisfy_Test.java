/*
 * Created on Sep 30, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.IsSatisfied.satisfied;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.TestData.someInfo;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Conditions#assertDoesNotSatisfy(AssertionInfo, Object, Condition)}</code>.
 *
 * @author Alex Ruiz
 */
public class Conditions_assertDoesNotSatisfy_Test {

  private static Object actual;

  @Rule public ExpectedException thrown = none();

  @BeforeClass public static void setUpOnce() {
    actual = "Yoda";
  }

  private Failures failures;
  private TestCondition<Object> condition;
  private Conditions conditions;

  @Before public void setUp() {
    failures = spy(Failures.instance());
    condition = new TestCondition<Object>();
    conditions = new Conditions(failures);
  }

  @Test public void should_throw_error_if_Condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    conditions.assertDoesNotSatisfy(someInfo(), actual, null);
  }

  @Test public void should_pass_if_Condition_is_not_met() {
    condition.shouldMatch(false);
    conditions.assertDoesNotSatisfy(someInfo(), actual, condition);
  }

  @Test public void should_fail_if_Condition_is_met() {
    condition.shouldMatch(true);
    AssertionInfo info = someInfo();
    try {
      conditions.assertDoesNotSatisfy(info, actual, condition);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, satisfied(actual, condition));
  }
}
