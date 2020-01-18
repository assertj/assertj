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
package org.assertj.core.error;

import java.time.Duration;

/**
 * @author Filip Hrisafov
 */
public class ShouldHaveDuration extends BasicErrorMessageFactory {

  private static final String EXPECTED_PREFIX = "%n"
                                                + "Expecting Duration:%n"
                                                + " <%s>%n"
                                                + "to have %s ";

  public static ShouldHaveDuration shouldHaveNanos(Duration actual, long actualNanos, long expectedNanos) {
    String metric;
    if (expectedNanos == 1 || expectedNanos == -1) {
      metric = "nano";
    } else {
      metric = "nanos";
    }
    return new ShouldHaveDuration(actual, actualNanos, expectedNanos, metric);
  }

  public static ShouldHaveDuration shouldHaveMillis(Duration actual, long actualMillis, long expectedMillis) {
    String metric;
    if (expectedMillis == 1 || expectedMillis == -1) {
      metric = "milli";
    } else {
      metric = "millis";
    }
    return new ShouldHaveDuration(actual, actualMillis, expectedMillis, metric);
  }

  public static ShouldHaveDuration shouldHaveSeconds(Duration actual, long actualSeconds, long expectedSeconds) {
    String metric;
    if (expectedSeconds == 1 || expectedSeconds == -1) {
      metric = "second";
    } else {
      metric = "seconds";
    }
    return new ShouldHaveDuration(actual, actualSeconds, expectedSeconds, metric);
  }

  public static ShouldHaveDuration shouldHaveMinutes(Duration actual, long actualMinutes, long expectedMinutes) {
    String metric;
    if (expectedMinutes == 1 || expectedMinutes == -1) {
      metric = "minute";
    } else {
      metric = "minutes";
    }
    return new ShouldHaveDuration(actual, actualMinutes, expectedMinutes, metric);
  }

  public static ShouldHaveDuration shouldHaveHours(Duration actual, long actualHours, long expectedHours) {
    String metric;
    if (expectedHours == 1 || expectedHours == -1) {
      metric = "hour";
    } else {
      metric = "hours";
    }
    return new ShouldHaveDuration(actual, actualHours, expectedHours, metric);
  }

  public static ShouldHaveDuration shouldHaveDays(Duration actual, long actualDays, long expectedDays) {
    String metric;
    if (expectedDays == 1 || expectedDays == -1) {
      metric = "day";
    } else {
      metric = "days";
    }
    return new ShouldHaveDuration(actual, actualDays, expectedDays, metric);
  }

  private ShouldHaveDuration(Duration actual, long actualSpecific, long expectedSpecific, String metric) {
    super(EXPECTED_PREFIX + metric + " but had %s", actual, expectedSpecific, actualSpecific);
  }
}
