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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionThrownBy;
import static org.assertj.core.api.Fail.shouldHaveThrown;

import java.util.concurrent.Callable;

import org.junit.Test;

public class ThrowableAssert_built_from_Callable_Test {

  @Test
  public void should_build_ThrowableAssert_with_runtime_exception_thrown_by_callable_code() {
	// check that actual exception is the one thrown by Callable<Void>#run
	assertThatExceptionThrownBy(new Callable<Void>() {
	  @Override
	  public Void call() {
		throw new IllegalArgumentException("something was wrong");
	  }
	}).isInstanceOf(IllegalArgumentException.class).hasMessage("something was wrong");
  }

  @Test
  public void should_build_ThrowableAssert_with_throwable_thrown_by_callable_code() {
	assertThatExceptionThrownBy(new Callable<Void>() {
	  @Override
	  public Void call() throws Exception {
		throw new Exception("something was wrong");
	  }
	}).isInstanceOf(Exception.class).hasMessage("something was wrong");
  }

  @Test
  public void should_fail_if_nothing_is_thrown_by_callable_code() throws Exception {
	try {
	  assertThatExceptionThrownBy(new Callable<Void>() {
		@Override
		public Void call() {
		  // no exception
		  return null;
		}
	  });
	  shouldHaveThrown(AssertionError.class);
	} catch (AssertionError e) {
	  assertThat(e).hasMessage("Expecting code to throw an exception.");
	}
  }

}
