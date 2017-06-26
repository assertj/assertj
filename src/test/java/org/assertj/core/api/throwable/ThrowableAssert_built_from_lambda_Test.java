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
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Jedi;
import org.junit.Rule;
import org.junit.Test;

public class ThrowableAssert_built_from_lambda_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_build_ThrowableAssert_with_runtime_exception_thrown_by_lambda() {
	assertThatThrownBy(() -> {
	  throw new IllegalArgumentException("something was wrong");
	}).isInstanceOf(IllegalArgumentException.class)
	  .hasMessage("something was wrong");
  }

  @Test
  public void should_build_ThrowableAssert_with_checked_exception_thrown_by_lambda() {
	Jedi yoda = new Jedi("Yoda", "Green");
	// @format:off
	assertThatThrownBy(() -> { throw new Exception(yoda + " is no Sith"); })
	  .isInstanceOf(Exception.class)
	  .hasMessage(yoda + " is no Sith");
	// @format:on
  }

  @Test
  public void should_fail_if_nothing_is_thrown_by_lambda() {
    thrown.expectAssertionError("%nExpecting code to raise a throwable.");
    assertThatThrownBy(() -> {});
  }

}
