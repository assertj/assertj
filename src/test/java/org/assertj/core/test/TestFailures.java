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
package org.assertj.core.test;

import static org.assertj.core.api.Assertions.fail;

/**
 * @author Yvonne Wang
 * @author Francis Galiegue
 */
public final class TestFailures {

  public static void failBecauseExpectedAssertionErrorWasNotThrown() {
    fail("Assertion error expected");
  }

  public static void wasExpectingAssertionError() {
    throw new AssertionErrorExpectedException();
  }

  private TestFailures() {}
}
