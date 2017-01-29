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
package org.assertj.core.util;

public class StackTraceUtils {

  /**
   * Returns true if given {@link Throwable} stack trace contains AssertJ related elements, false otherwise.
   *
   * @param throwable the {@link Throwable} we want to check stack trace for AssertJ related elements.
   * @return true if given {@link Throwable} stack trace contains AssertJ related elements, false otherwise.
   */
  public static boolean hasStackTraceElementRelatedToAssertJ(Throwable throwable) {
    StackTraceElement[] stackTrace = throwable.getStackTrace();
    for (StackTraceElement stackTraceElement : stackTrace) {
      if (stackTraceElement.getClassName().contains("org.assert")) return true;
    }
    return false;
  }

}
