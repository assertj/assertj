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
package org.assertj.core.error;

import org.assertj.core.util.Throwables;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Throwable} have a root cause
 * <b>exactly</b> instance of a certain type.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveRootCauseExactlyInstance extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link org.assertj.core.error.BasicErrorMessageFactory}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expectedCauseType the expected cause instance.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveRootCauseExactlyInstance(Throwable actual,
      Class<? extends Throwable> expectedCauseType) {
    return Throwables.getRootCause(actual) == null ? new ShouldHaveRootCauseExactlyInstance(expectedCauseType)
        : new ShouldHaveRootCauseExactlyInstance(actual, expectedCauseType);
  }

  private ShouldHaveRootCauseExactlyInstance(Throwable actual, Class<? extends Throwable> expectedCauseType) {
    super(
        "%nExpecting a throwable with root cause being exactly an instance of:%n <%s>%nbut was an instance of:%n <%s>",
        expectedCauseType, Throwables.getRootCause(actual));
  }

  private ShouldHaveRootCauseExactlyInstance(Class<? extends Throwable> expectedCauseType) {
    super(
        "%nExpecting a throwable with root cause being exactly an instance of:%n <%s>%nbut current throwable has no cause.",
        expectedCauseType);
  }
}
