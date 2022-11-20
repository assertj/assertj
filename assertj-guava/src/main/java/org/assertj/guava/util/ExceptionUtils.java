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
package org.assertj.guava.util;

import static java.lang.String.format;

public class ExceptionUtils {

  /**
   * Throws a {@link IllegalArgumentException} if given condition is true with message formatted with given arguments
   * using {@link String#format(String, Object...)}.
   * 
   * @param condition condition that will trigger the {@link IllegalArgumentException} if true
   * @param exceptionMessage message set in thrown IllegalArgumentException
   * @param exceptionMessageArgs arguments to be used to format exceptionMessage
   * @throws IllegalArgumentException if condition is true
   */
  public static void throwIllegalArgumentExceptionIfTrue(boolean condition, String exceptionMessage,
                                                         Object... exceptionMessageArgs) {
    if (condition) {
      throw new IllegalArgumentException(format(exceptionMessage, exceptionMessageArgs));
    }
  }

  /**
   * protected to avoid direct instanciation but allowing subclassing.
   */
  protected ExceptionUtils() {
    // empty
  }

}
