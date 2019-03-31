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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error.buffer;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import java.nio.Buffer;

/**
 * Creates an error message indicating that the buffer has not been flipped.
 *
 * @author Jean de Leeuw
 */
public class ShouldBeFlipped extends BasicErrorMessageFactory {

  private static final String SHOULD_BE_FLIPPED = "%nExpected%n  <%s>%nto be flipped.%n";

  /**
   * Creates a new <code>{@link ShouldBeFlipped}</code>.
   *
   * @param actual the actual buffer in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeFlipped(Buffer actual) {
    return new ShouldBeFlipped(actual);
  }

  private ShouldBeFlipped(Buffer actual) {
    super(SHOULD_BE_FLIPPED, actual);
  }
}
