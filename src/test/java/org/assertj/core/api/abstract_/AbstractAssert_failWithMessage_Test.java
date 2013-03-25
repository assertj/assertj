/*
 * Created on Oct 20, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import org.junit.Test;

import org.assertj.core.api.ConcreteAssert;


/**
 * Tests for <code>AbstractAssert#failWithMessage(String, Object...)</code>.
 * 
 * @author Joel Costigliola
 */
public class AbstractAssert_failWithMessage_Test {

  @Test
  public void should_fail() {
    // should not fail
    new ConcreteAssert("foo").failIfTrue(false);
    // should fail
    try {
      new ConcreteAssert("foo").failIfTrue(true);
    } catch (AssertionError e) {
      assertEquals("'predefined' error message", e.getMessage());
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_keep_specific_error_message_and_description_set_by_user() {
    try {
      new ConcreteAssert("foo").as("user description").overridingErrorMessage("my error %s", "!").failIfTrue(true);
    } catch (AssertionError e) {
      assertEquals("[user description] my error !", e.getMessage());
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
}
