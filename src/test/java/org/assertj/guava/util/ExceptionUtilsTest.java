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
package org.assertj.guava.util;

import static org.assertj.guava.util.ExceptionUtils.throwIllegalArgumentExceptionIfTrue;

import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExceptionUtilsTest {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_IllegalArgumentException_with_given_message_if_condition_is_true() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("error message with arg1, arg2");
    throwIllegalArgumentExceptionIfTrue(true, "error message with %s, %s", "arg1", "arg2");
  }

  @Test
  public void should_not_throw_IllegalArgumentException_if_condition_is_false() {
    throwIllegalArgumentExceptionIfTrue(false, "some message");
  }

}
