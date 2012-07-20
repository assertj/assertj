/*
 * Created on Jul 20, 2012
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.error;

import java.io.File;

import org.fest.assertions.internal.BinaryDiffResult;

/**
 * Creates an error message indicating that an assertion that verifies that a file has a given binary content failed.
 * 
 * @author Olivier Michallat
 */
public class ShouldHaveBinaryContent extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveBinaryContent}</code>.
   * @param actual the actual file in the failed assertion.
   * @param diff the differences between {@code actual} and the given binary content.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveBinaryContent(File actual, BinaryDiffResult diff) {
    return new ShouldHaveBinaryContent(actual, diff);
  }

  private ShouldHaveBinaryContent(File actual, BinaryDiffResult diff) {
    super("file:<%s> does not have expected binary content: at offset <%s>, expected:\n<%s>\n but was:\n<%s>", actual,
        diff.offset, diff.expected, diff.actual);
  }
}
