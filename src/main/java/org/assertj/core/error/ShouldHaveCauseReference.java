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
/** .
 * Creates an error message indicating that an assertion
 * that verifies that a {@link Throwable} have a certain cause
 * reference
 *
 * @author weiyilei
 */
public class ShouldHaveCauseReference extends BasicErrorMessageFactory {
  /** .
   * because Utility classes should not have a public or default constructor.
   */
  private ShouldHaveCauseReference() {
    //not called
  }
  /** .
   * Creates a String explaining the detailed
   * information about the failed assertion
   *
   * @param actualCause the actual cause reference
   *                    in the failed assertion
   * @param expectedCause the expected cause reference
   * @return a String explaining the detailed
   * information about the failed assertion
   */
  public static ErrorMessageFactory shouldHaveCauseReference(Throwable actualCause, Throwable expectedCause) {
    return actualCause == null
      ? new ShouldHaveCauseReference(expectedCause)
      : new ShouldHaveCauseReference(actualCause, expectedCause);
  }

  /** .
   * When the actual cause reference is null
   *
   * @param expectedCause the expected cause reference
   * @return a String explaining the detailed
   * information about the failed assertion
   */
  private ShouldHaveCauseReference(Throwable expectedCause){
    super("Expecting actual cause reference to be:%n"
        + "  %s%n"
        + "but was:%n"
        + "  null",
      expectedCause);
  }

  /** .
   * When the actual cause reference is not null
   *
   * @param actualCause the actual cause reference
   *                    in the failed assertion
   * @param expectedCause the expected cause reference
   * @return a String explaining the detailed
   * information about the failed assertion
   */
  private ShouldHaveCauseReference(Throwable actualCause, Throwable expectedCause) {
    super("Expecting actual cause reference to be:%n"
        + "  %s%n"
        + "but was:%n"
        + "  %s%n"
        + "Throwable that failed the check:%n"
        + "%n"
        + escapePercent(getStackTrace(actualCause)),
      expectedCause, actualCause);
  }
}
