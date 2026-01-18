/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.System.lineSeparator;

/**
 * Wrapper record for values that should have all lines indented in error messages.
 * <p>
 * When a value is wrapped with {@link #of(Object)}, each line of its string representation
 * will be indented with 2 spaces. This is useful for multi-line values where only the first line
 * would otherwise be indented.
 */
public record IndentWrapper(Object value) {

  private static final String INDENTATION = "  ";

  /**
   * Wraps a value so that all lines of its string representation will be indented in the error message.
   * <p>
   * This is useful for multi-line values in error messages. Without this wrapper, only the first line
   * would be indented according to the format string, while subsequent lines would have no indentation.
   *
   * @param value the value to wrap for indentation
   * @return an {@link IndentWrapper} instance
   */
  public static IndentWrapper of(Object value) {
    return new IndentWrapper(value);
  }

  static String indentAllLines(String representation) {
    if (representation == null) return null;
    return representation.replace(lineSeparator(), lineSeparator() + INDENTATION);
  }

}
