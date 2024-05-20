/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.date;

import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

/**
 * @author Joel Costigliola
 */
class DateAssert_isEqualTo_Test {

  @Nested
  @TestInstance(PER_CLASS)
  class With_Object {

    @ParameterizedTest
    @MethodSource
    void should_pass(Date actual, Object expected) {
      // WHEN/THEN
      assertThat(actual).isEqualTo(expected);
    }

    Arguments[] should_pass() {
      return new Arguments[] {
          arguments(Date.from(parse("1970-01-01T00:00:00.000000001Z")),
                    Date.from(parse("1970-01-01T00:00:00.000000001Z"))),
          arguments(Date.from(parse("1970-01-01T00:00:00.000000001Z")),
                    Timestamp.from(parse("1970-01-01T00:00:00.000000001Z")))
      };
    }

    @ParameterizedTest
    @MethodSource
    void should_fail(Date actual, Object expected) {
      // WHEN
      AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
      // THEN
      then(error).isInstanceOf(AssertionFailedError.class);
    }

    Arguments[] should_fail() {
      return new Arguments[] {
          arguments(Timestamp.from(parse("1970-01-01T00:00:00.000000001Z")),
                    Date.from(parse("1970-01-01T00:00:00.000000001Z")))
      };
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class With_Instant {

    @ParameterizedTest
    @MethodSource
    void should_pass(Date actual, Instant expected) {
      // WHEN/THEN
      assertThat(actual).isEqualTo(expected);
    }

    Arguments[] should_pass() {
      return new Arguments[] {
          arguments(Date.from(parse("1970-01-01T00:00:00.000000001Z")),
                    parse("1970-01-01T00:00:00.000000001Z")),
          arguments(Timestamp.from(parse("1970-01-01T00:00:00.000000001Z")),
                    parse("1970-01-01T00:00:00.000000001Z"))
      };
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class With_String {

    @ParameterizedTest
    @MethodSource
    void should_pass(Date actual, String expected) {
      // WHEN/THEN
      assertThat(actual).isEqualTo(expected);
    }

    Arguments[] should_pass() {
      return new Arguments[] {
          arguments(Date.from(parse("1970-01-01T00:00:00.000000001Z")),
                    "1970-01-01T00:00:00.000Z")
      };
    }

    @ParameterizedTest
    @MethodSource
    void should_fail(Date actual, String expected) {
      // WHEN
      AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
      // THEN
      then(error).isInstanceOf(AssertionFailedError.class);
    }

    Arguments[] should_fail() {
      return new Arguments[] {
          arguments(Date.from(parse("1970-01-01T00:00:00.000000001Z")),
                    "1970-01-01T00:00:00.000000001Z"),
          arguments(Timestamp.from(parse("1970-01-01T00:00:00.000000001Z")),
                    "1970-01-01T00:00:00.000000001Z")
      };
    }

  }

}
