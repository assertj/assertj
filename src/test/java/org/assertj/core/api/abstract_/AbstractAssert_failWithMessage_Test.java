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
package org.assertj.core.api.abstract_;

import static org.assertj.core.test.ExpectedException.none;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.test.ExpectedException;

/**
 * Tests for <code>AbstractAssert#failWithMessage(String, Object...)</code>.
 * 
 * @author Joel Costigliola
 */
public class AbstractAssert_failWithMessage_Test {

  @Rule
  public ExpectedException thrown = none();

  private ConcreteAssert assertion;

  @Before
  public void setup() {
	assertion = new ConcreteAssert("foo");
  }

  @Test
  public void should_fail_with_simple_message() {
    thrown.expectAssertionError("fail");
    assertion.failWithMessage("fail");
  }

  @Test
  public void should_fail_with_message_having_args() {
    thrown.expectAssertionError("fail 5 times");
    assertion.failWithMessage("fail %d %s", 5, "times");
  }

  @Test
  public void should_keep_description_set_by_user() {
    thrown.expectAssertionError("[user description] fail 5 times");
    assertion.as("user description").failWithMessage("fail %d %s", 5, "times");
  }

  @Test
  public void should_keep_specific_error_message_and_description_set_by_user() {
    thrown.expectAssertionError("[test context] my 5 errors !");
    assertion.as("test context").overridingErrorMessage("my %d errors %s", 5, "!").failWithMessage("%d %s", 5, "time");
  }

}
