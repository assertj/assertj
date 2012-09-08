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
package org.fest.assertions.api.abstract_;

import static junit.framework.Assert.assertEquals;

import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import org.junit.Test;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.ConcreteAssert;

/**
 * Tests for <code>{@link AbstractAssert#getWClass()}</code>.
 * 
 * @author Joel Costigliola
 */
public class AbstractAssert_get_writable_info_Test {

  @Test
  public void should_keep_specific_error_message_and_description_set_by_user() {
    try {
      new ConcreteAssert(6L).as("user description").checkNull();
    } catch (AssertionError e) {
      assertEquals("[user description] specific error message", e.getMessage());
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
