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
 * Creates an error message indicating that buffer's expected marked position and its actual marked position
 * do no match.
 *
 * @author Jean de Leeuw
 */
public class ShouldBeMarkedAt extends BasicErrorMessageFactory {

  private static final String EXPECTING_TO_BE_MARKED_AT = "%nExpected%n  <%s>%nto be marked at position%n  <%s>%nbut was marked at position%n  <%s>%n";

  /**
   * Creates a new <code>{@link ShouldBeMarkedAt}</code>.
   *
   * @param expected the expected marked position of the buffer in the failed assertion.
   * @param actual the actual marked position of the buffer in the failed assertion.
   * @param buffer the actual buffer in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeMarkedAt(int expected, int actual, Buffer buffer) {
    return new ShouldBeMarkedAt(expected, actual, buffer);
  }

  private ShouldBeMarkedAt(int expected, int actual, Buffer buffer) {
    super(EXPECTING_TO_BE_MARKED_AT, buffer, expected, actual);
  }
}
