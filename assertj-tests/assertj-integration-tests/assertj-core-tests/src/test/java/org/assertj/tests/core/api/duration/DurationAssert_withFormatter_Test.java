/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.time.Duration;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

/**
 * @author Eric Rizzo
 */
class DurationAssert_withFormatter_Test {

  @Test
  void should_throw_IllegalArgumentException_when_formatter_is_null() {
    // GIVEN
    Duration actual = Duration.ZERO;
    // WHEN
    var illegalArgumentException = catchIllegalArgumentException(() -> assertThat(actual).withFormatter(null));
    // THEN
    then(illegalArgumentException).hasMessage("formatter should not be null");
  }

  @Test
  void failure_message_should_use_formatter_function() {
    // GIVEN
    Duration actual = Duration.ofDays(3);
    Function<Duration, String> formatter = d -> "%s days or %s hours".formatted(d.toDays(), d.toHours());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).withFormatter(formatter).isNull());
    // THEN
    then(assertionError).hasMessageContaining("3 days or 72 hours");
  }
}
