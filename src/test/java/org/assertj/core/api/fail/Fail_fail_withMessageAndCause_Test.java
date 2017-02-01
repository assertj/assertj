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
package org.assertj.core.api.fail;

import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.api.Fail;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;


/**
 * Tests for <code>{@link Fail#fail(String, Throwable)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Fail_fail_withMessageAndCause_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void shouldThrowErrorWithGivenMessageAndCause() {
    String message = "Some Throwable";
    Throwable cause = new Throwable();
    thrown.expectWithCause(AssertionError.class, message, cause);
    Fail.fail(message, cause);
  }
}
