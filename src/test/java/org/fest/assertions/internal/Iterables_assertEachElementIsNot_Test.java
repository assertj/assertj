/*
 * Created on Mar 5, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldBeSameGenericBetweenIterableAndCondition.shouldBeSameGenericBetweenIterableAndCondition;
import static org.fest.assertions.error.EachElementShouldNotBe.eachElementShouldNotBe;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Collections.list;
import static org.fest.util.Collections.set;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Set;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link Iterables#assertEachElementIsNot(AssertionInfo, Iterable, org.fest.assertions.core.Condition)}</code>
 * .
 * 
 * @author Nicolas François
 */
public class Iterables_assertEachElementIsNot_Test extends AbstractTest_for_Iterables {

	private Failures failures;
	private TestCondition<Object> testCondition;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		failures = spy(new Failures());
		testCondition = new TestCondition<Object>();
		iterables = new Iterables();
		iterables.failures = failures;
	}
	
	private final Condition<String> condition = new Condition<String>() {

		private final Set<String> jedis = set("Luke", "Yoda", "Obiwan");

		@Override
		public boolean matches(String value) {
			return jedis.contains(value);
		};

	};

	@Test
	public void should_pass_if_each_element_not_satisfies_condition() {
		actual = list("Darth Vader", "Leia");
		iterables.assertEachElementIsNot(someInfo(), actual, condition);
	}

	@Test
	public void should_throw_error_if_condition_is_null() {
		thrown.expectNullPointerException("The condition to evaluate should not be null");
		actual = list("Darth Vader", "Leia");
		iterables.assertEachElementIsNot(someInfo(), actual, null);
	}
	
	@Test
	public void should_fail_if_condition_has_bad_type() {
	    AssertionInfo info = someInfo();
	    List<Integer> actual = list(42);
	    try {
	    	iterables.assertEachElementIsNot(someInfo(), actual, condition);
	    } catch (AssertionError e) {
	      verify(failures).failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
	      return;
	    }
	    failBecauseExpectedAssertionErrorWasNotThrown();
	}	

	@Test
	public void should_fail_if_condition_is_met() {
	    testCondition.shouldMatch(false);
	    AssertionInfo info = someInfo();
	    try {
	    	actual = list("Darth Vader", "Leia", "Yoda");
	    	iterables.assertEachElementIsNot(someInfo(), actual, condition);
	    } catch (AssertionError e) {
	      verify(failures).failure(info, eachElementShouldNotBe(actual, list("Yoda"), condition));
	      return;
	    }
	    failBecauseExpectedAssertionErrorWasNotThrown();
	}

}
