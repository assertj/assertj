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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error.future;

import static org.assertj.core.util.Strings.escapePercent;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldBeCompletedWithin extends BasicErrorMessageFactory {

  private static final String SHOULD_BE_COMPLETED_WITHIN_DURATION = "%n"
                                                                    + "Expecting%n"
                                                                    + "  <%s>%n"
                                                                    + "to be completed within %s.%n%n"
                                                                    + "exception caught while trying to get the future result: ";

  private static final String SHOULD_BE_COMPLETED_WITHIN = "%n"
                                                           + "Expecting%n"
                                                           + "  <%s>%n"
                                                           + "to be completed within %s %s.%n%n"
                                                           + "exception caught while trying to get the future result: ";

  public static ErrorMessageFactory shouldBeCompletedWithin(CompletableFuture<?> actual, Duration duration, Exception exception) {
    return new ShouldBeCompletedWithin(actual, duration, exception);
  }

  public static ErrorMessageFactory shouldBeCompletedWithin(CompletableFuture<?> actual, long timeout, TimeUnit timeUnit,
                                                            Exception exception) {
    return new ShouldBeCompletedWithin(actual, timeout, timeUnit, exception);
  }

  private ShouldBeCompletedWithin(CompletableFuture<?> actual, Duration duration, Exception exception) {
    // don't put the stack trace as a parameter to avoid AssertJ default String formatting
    super(SHOULD_BE_COMPLETED_WITHIN_DURATION + escapePercent(getStackTrace(exception)), actual, duration);
  }

  private ShouldBeCompletedWithin(CompletableFuture<?> actual, long timeout, TimeUnit timeUnit, Exception exception) {
    // don't put the stack trace as a parameter to avoid AssertJ default String formatting
    // ChronoUnit toString looks better than TimeUnit
    super(SHOULD_BE_COMPLETED_WITHIN + escapePercent(getStackTrace(exception)), actual, timeout, toChronoUnit(timeUnit));
  }

  // copied from java 9 code
  private static ChronoUnit toChronoUnit(TimeUnit timeUnit) {
    switch (timeUnit) {
    case NANOSECONDS:
      return ChronoUnit.NANOS;
    case MICROSECONDS:
      return ChronoUnit.MICROS;
    case MILLISECONDS:
      return ChronoUnit.MILLIS;
    case SECONDS:
      return ChronoUnit.SECONDS;
    case MINUTES:
      return ChronoUnit.MINUTES;
    case HOURS:
      return ChronoUnit.HOURS;
    case DAYS:
      return ChronoUnit.DAYS;
    default:
      throw new AssertionError();
    }
  }

}
