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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.util.Strings.escapePercent;
import static org.assertj.core.util.Throwables.getStackTrace;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Throwable} have a cause
 * <b>exactly</b> instance of a certain type.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveCauseExactlyInstance extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expectedCauseType the expected cause instance.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveCauseExactlyInstance(Throwable actual,
                                                                   Class<? extends Throwable> expectedCauseType) {
    return actual.getCause() == null
        ? new ShouldHaveCauseExactlyInstance(expectedCauseType, actual)
        : new ShouldHaveCauseExactlyInstance(actual, expectedCauseType);
  }

  private ShouldHaveCauseExactlyInstance(Throwable actual, Class<? extends Throwable> expectedCauseType) {
    super("%nExpecting a throwable with cause being exactly an instance of:%n" +
          "  %s%n" +
          "but was an instance of:%n" +
          "  %s%n" +
          "Throwable that failed the check:%n" +
          "%n" + escapePercent(getStackTrace(actual)),
          expectedCauseType, actual.getCause().getClass());
  }

  private ShouldHaveCauseExactlyInstance(Class<? extends Throwable> expectedCauseType, Throwable actual) {
    super("%nExpecting a throwable with cause being exactly an instance of:%n  %s%nbut current throwable has no cause." +
          "%nThrowable that failed the check:%n" + escapePercent(getStackTrace(actual)), expectedCauseType);
  }
}
