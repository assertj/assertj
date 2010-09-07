/*
 * Created on Aug 2, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.test;

import org.junit.rules.ExpectedException;

/**
 * Expected exceptions in the test suite.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public final class ExpectedExceptions {

  public static void expectErrorWhenDescriptionIsNull(ExpectedException rule) {
    rule.expect(NullPointerException.class);
    rule.expectMessage("The description to set should not be null");
  }

  public static void expectAssertionErrorWithMessage(ExpectedException rule, String message) {
    rule.expect(AssertionError.class);
    rule.expectMessage(message);
  }

  public static void expectError(ExpectedException rule, Throwable toExpect) {
    rule.expect(toExpect.getClass());
    rule.expectMessage(toExpect.getMessage());
  }

  private ExpectedExceptions() {}
}
