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
package org.assertj.core.api;

import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class Java6Assertions_fail_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_include_message_when_failing() {
    thrown.expectAssertionError("Failed :(");
    Java6Assertions.fail("Failed :(");
  }
  
  @Test
  public void should_include_message_with_parameters_when_failing() {
    thrown.expectAssertionError("Failed :(");
    Java6Assertions.fail("Failed %s", ":(");
  }
  
  @Test
  public void should_include_message_with_cause_when_failing() {
    String message = "Some Throwable";
    Throwable cause = new Throwable();
    thrown.expectWithCause(AssertionError.class, message, cause);
    Java6Assertions.fail(message, cause);
  }
}
