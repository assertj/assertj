/*
 * Created on Sep 16, 2007
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
 * Copyright @2007-2011 the original author or authors.
 */
package org.fest.assertions.api.fail;

import static org.junit.Assert.*;

import org.junit.Test;

import org.fest.assertions.api.Fail;

/**
 * Tests for <code>{@link Fail#fail(String, Throwable)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Fail_fail_withMessageAndCause_Test {

  @Test
  public void shouldThrowErrorWithGivenMessageAndCause() {
    String message = "Some Throwable";
    Throwable cause = new Throwable();
    try {
      Fail.fail(message, cause);
      fail("AssertionError should have been thrown");
    } catch (AssertionError e) {
      assertEquals(e.getMessage(), message);
      assertSame(e.getCause(), cause);
    }
  }
}
