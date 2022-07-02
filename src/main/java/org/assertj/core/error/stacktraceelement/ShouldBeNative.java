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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.error.stacktraceelement;

import org.assertj.core.error.BasicErrorMessageFactory;

/**
 * Error message factory for reporting that a stack trace element was unexpectedly
 * non-native.
 *
 * @author Ashley Scopes
 */
public class ShouldBeNative extends BasicErrorMessageFactory {

  private static final String SHOULD_BE_NATIVE = String.join(
    "%n",
    "",
    "Expecting stack trace element:",
    "  %s",
    "to be for a native method, but it was not"
  );

  /**
   * Initialize a new error message to report that a stack trace element is unexpectedly
   * non-native.
   *
   * @param element the stack trace element to assert upon.
   * @return the message factory.
   */
  public static ShouldBeNative shouldBeNative(StackTraceElement element) {
    return new ShouldBeNative(element);
  }

  private ShouldBeNative(StackTraceElement element) {
    super(SHOULD_BE_NATIVE, element);
  }
}