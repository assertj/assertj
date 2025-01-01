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
package org.assertj.core.error.future;

import java.time.Duration;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveCompletedExceptionallyWithin extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_COMPLETED_EXCEPTIONALLY_WITHIN_DURATION = "%n" +
                                                                                    "Expecting%n" +
                                                                                    "  <%s>%n" +
                                                                                    "to have completed exceptionally " +
                                                                                    "within %s.%n" +
                                                                                    Warning.WARNING;
  private static final String SHOULD_HAVE_COMPLETED_EXCEPTIONALLY_WITHIN = "%n" +
                                                                           "Expecting%n" +
                                                                           "  <%s>%n" +
                                                                           "to have completed exceptionally within %s %s.%n" +
                                                                           Warning.WARNING;

  public static ErrorMessageFactory shouldHaveCompletedExceptionallyWithin(Future<?> actual, Duration timeout) {
    return new ShouldHaveCompletedExceptionallyWithin(actual, timeout);
  }

  public static ErrorMessageFactory shouldHaveCompletedExceptionallyWithin(Future<?> actual, long timeout, TimeUnit unit) {
    return new ShouldHaveCompletedExceptionallyWithin(actual, timeout, unit);
  }

  private ShouldHaveCompletedExceptionallyWithin(Future<?> actual, Duration timeout) {
    super(SHOULD_HAVE_COMPLETED_EXCEPTIONALLY_WITHIN_DURATION, actual, timeout);
  }

  private ShouldHaveCompletedExceptionallyWithin(Future<?> actual, long timeout, TimeUnit unit) {
    super(SHOULD_HAVE_COMPLETED_EXCEPTIONALLY_WITHIN, actual, timeout, unit);
  }
}
