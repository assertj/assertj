/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import java.nio.Buffer;

/**
 * Creates an error message indicating that buffer's expected capacity and its actual capacity
 * do no match.
 *
 * @author Jean de Leeuw
 */
public class ShouldHaveCapacity extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_CAPACITY = "%nExpected%n  <%s>%nto have capacity%n  <%s>%nbut was%n  <%s>%n";

  /**
   * Creates a new <code>{@link ShouldHaveCapacity}</code>.
   *
   * @param expected the expected capacity of the buffer in the failed assertion.
   * @param actual the actual capacity of the buffer in the failed assertion.
   * @param buffer the actual buffer in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveCapacity(int expected, int actual, Buffer buffer) {
    return new ShouldHaveCapacity(expected, actual, buffer);
  }

  private ShouldHaveCapacity(int expected, int actual, Buffer buffer) {
    super(SHOULD_HAVE_CAPACITY, buffer, expected, actual);
  }
}
