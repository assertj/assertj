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

import static org.fest.assertions.error.ElementsShouldHave.elementsShouldHave;
import static org.fest.assertions.error.ConditionAndGroupGenericParameterTypeShouldBeTheSame.shouldBeSameGenericBetweenIterableAndCondition;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.fest.assertions.condition.JediPowerCondition;
import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link ObjectArrays#have(org.fest.assertions.core.AssertionInfo, Object[], org.fest.assertions.core.Condition)}</code>
 * .
 * 
 * @author Nicolas François
 */
public class ObjectArrays_assertHave_Test extends AbstractTest_for_ObjectArrays {

	private Condition<String> jediPower = new JediPowerCondition();
	private Failures failures;
	private TestCondition<Object> testCondition;
	private Conditions conditions;	

	@Override
	@Before
	public void setUp() {
		super.setUp();
		failures = spy(new Failures());
		testCondition = new TestCondition<Object>();
		arrays = new ObjectArrays();
		arrays.failures = failures;
		conditions = spy(new Conditions());		
		arrays.conditions = conditions;		
	}	
	
	
	@Test
	public void should_pass_if_each_element_satisfies_condition() {
		actual = array("Yoda", "Luke");
		arrays.assertHave(someInfo(), actual, jediPower);
		verify(conditions).assertIsNotNull(jediPower);
	}	
	
	@Test
	public void should_throw_error_if_condition_is_null() {
		thrown.expectNullPointerException("The condition to evaluate should not be null");
		arrays.assertHave(someInfo(), actual, null);
		verify(conditions).assertIsNotNull(null);
	}
	
	@Test
	public void should_throw_error_if_condition_has_bad_type() {
		actual = array(42);
	    AssertionInfo info = someInfo();
	    try {
	    	arrays.assertHave(someInfo(), actual, jediPower);
	    } catch (AssertionError e) {
	      verify(conditions).assertIsNotNull(jediPower);
	      verify(failures).failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, jediPower));
	      return;
	    }
	    failBecauseExpectedAssertionErrorWasNotThrown();		
	}	

	@Test
	public void should_fail_if_Condition_is_not_met() {
	    testCondition.shouldMatch(false);
	    AssertionInfo info = someInfo();
	    try {
	    	actual = array("Yoda", "Luke", "Leia");
	    	arrays.assertHave(someInfo(), actual, jediPower);
	    } catch (AssertionError e) {
	    	verify(conditions).assertIsNotNull(jediPower);	
	    	verify(failures).failure(info, elementsShouldHave(actual, list("Leia"), jediPower));
	      verify(failures).failure(info, elementsShouldHave(actual, list("Leia"), jediPower));
	      return;
	    }
	    failBecauseExpectedAssertionErrorWasNotThrown();
	}	

}
