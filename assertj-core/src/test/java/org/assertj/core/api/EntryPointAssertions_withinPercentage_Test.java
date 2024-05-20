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
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Percentage.withPercentage;

import java.util.function.Function;
import java.util.stream.Stream;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions withinPercentage method")
class EntryPointAssertions_withinPercentage_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("doublePercentageFactories")
  void should_create_Double_offset(Function<Double, Percentage> percentageFactory) {
    // GIVEN
    Double value = 90.0;
    // WHEN
    Percentage percentage = percentageFactory.apply(value);
    // THEN
    then(percentage).isEqualTo(withPercentage(value));
  }

  private static Stream<Function<Double, Percentage>> doublePercentageFactories() {
    return Stream.of(Assertions::withinPercentage, BDDAssertions::withinPercentage, withAssertions::withinPercentage);
  }

  @ParameterizedTest
  @MethodSource("integerPercentageFactories")
  void should_create_Integer_offset(Function<Integer, Percentage> percentageFactory) {
    // GIVEN
    Integer value = 90;
    // WHEN
    Percentage percentage = percentageFactory.apply(value);
    // THEN
    then(percentage).isEqualTo(withPercentage(value));
  }

  private static Stream<Function<Integer, Percentage>> integerPercentageFactories() {
    return Stream.of(Assertions::withinPercentage, BDDAssertions::withinPercentage, withAssertions::withinPercentage);
  }

  @ParameterizedTest
  @MethodSource("longPercentageFactories")
  void should_create_Long_offset(Function<Long, Percentage> percentageFactory) {
    // GIVEN
    Long value = 90L;
    // WHEN
    Percentage percentage = percentageFactory.apply(value);
    // THEN
    then(percentage).isEqualTo(withPercentage(value));
  }

  private static Stream<Function<Long, Percentage>> longPercentageFactories() {
    return Stream.of(Assertions::withinPercentage, BDDAssertions::withinPercentage, withAssertions::withinPercentage);
  }

}
