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
package org.assertj.core.error;

public class ShouldHaveCauseReference extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveCauseReference(Throwable actualCause, Throwable expectedCause) {
    return actualCause == null
      ? new ShouldHaveCauseReference(expectedCause)
      : new ShouldHaveCauseReference(actualCause, expectedCause);
  }

  private ShouldHaveCauseReference(Throwable expectedCause){
    super("Expecting actual cause reference to be:%n" +
        "  %s%n" +
        "but was:%n" +
        "  null",
      expectedCause);
  }

  private ShouldHaveCauseReference(Throwable actualCause, Throwable expectedCause) {
    super("Expecting actual cause reference to be:%n" +
        "  %s%n" +
        "but was:%n" +
        "  %s%n" +
        "Throwable that failed the check:%n" +
        "%n" + escapePercent(getStackTrace(actualCause)),
      expectedCause, actualCause);
  }
}
