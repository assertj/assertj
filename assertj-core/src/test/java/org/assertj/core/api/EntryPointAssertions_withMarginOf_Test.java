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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.time.Duration;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions withMarginOf method")
class EntryPointAssertions_withMarginOf_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("durationProviders")
  void should_return_same_Duration(Function<Duration, Duration> durationFactory) {
    // GIVEN
    Duration duration = Duration.ofHours(1);
    // WHEN
    Duration result = durationFactory.apply(duration);
    // THEN
    then(result).isSameAs(duration);
  }

  private static Stream<Function<Duration, Duration>> durationProviders() {
    return Stream.of(Assertions::withMarginOf, BDDAssertions::withMarginOf, withAssertions::withMarginOf);
  }

}
