/*
 * Created on Mar 17, 2012
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
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.ObjectArrayFactory.emptyArray;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.fest.assertions.internal.ObjectArrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectArrayAssert#haveAtMost(Condition, int)}</code>.
 * 
 * @author Nicolas François 
 */
public class ObjectArrayAssert_haveAtMost_Test {

	  private ObjectArrays arrays;
	  private ObjectArrayAssert assertions;
	  private static Condition<Object> condition;

	  @Before public void setUp() {
	    arrays = mock(ObjectArrays.class);
	    assertions = new ObjectArrayAssert(emptyArray());
	    assertions.arrays = arrays;
	    condition = new TestCondition<Object>();
	  }

	  @Test public void should_verify_that_elements_have_at_most() {
	    assertions.haveAtMost(2, condition);
	    verify(arrays).assertHaveAtMost(assertions.info, assertions.actual, 2, condition);
	  }

	  @Test public void should_return_this() {
	    assertSame(assertions, assertions.haveAtMost(2, condition));
	  }	
	
}
