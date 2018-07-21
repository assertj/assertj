/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

public class ThrowableAssert_built_from_ThrowingCallable_Test {

  @Test
  public void should_build_ThrowableAssert_with_runtime_exception_thrown_by_callable_code() {
	// check that actual exception is the one thrown by ThrowingCallable()#run
	assertThatThrownBy(new ThrowingCallable() {
	  @Override
	  public void call() {
		throw new IllegalArgumentException("something was wrong");
	  }
	}).isInstanceOf(IllegalArgumentException.class).hasMessage("something was wrong");
  }

  @Test
  public void should_build_ThrowableAssert_with_throwable_thrown_by_callable_code() {
	assertThatThrownBy(new ThrowingCallable() {
	  @Override
	  public void call() throws Exception {
		throw new Exception("something was wrong");
	  }
	}).isInstanceOf(Exception.class).hasMessage("something was wrong");
  }

  @Test
  public void should_fail_if_nothing_is_thrown_by_callable_code() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      assertThatThrownBy(new ThrowingCallable() {
        @Override
        public void call() {
          // no exception
        }
      });
    }).withMessage(String.format("%nExpecting code to raise a throwable."));
  }

}
