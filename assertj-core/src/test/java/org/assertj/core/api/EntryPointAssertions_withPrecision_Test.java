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
import static org.assertj.core.data.Offset.offset;

import java.util.function.Function;
import java.util.stream.Stream;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions withPrecision method")
class EntryPointAssertions_withPrecision_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("doubleOffsetFactories")
  void should_create_Double_offset(Function<Double, Offset<Double>> withinFactory) {
    // GIVEN
    Double offsetValue = Double.MAX_VALUE;
    // WHEN
    Offset<Double> index = withinFactory.apply(offsetValue);
    // THEN
    then(index).isEqualTo(offset(offsetValue));
  }

  private static Stream<Function<Double, Offset<Double>>> doubleOffsetFactories() {
    return Stream.of(Assertions::withPrecision, BDDAssertions::withPrecision, withAssertions::withPrecision);
  }

  @ParameterizedTest
  @MethodSource("floatOffsetFactories")
  void should_create_Float_offset(Function<Float, Offset<Float>> withinFactory) {
    // GIVEN
    Float offsetValue = Float.MAX_VALUE;
    // WHEN
    Offset<Float> index = withinFactory.apply(offsetValue);
    // THEN
    then(index).isEqualTo(offset(offsetValue));
  }

  private static Stream<Function<Float, Offset<Float>>> floatOffsetFactories() {
    return Stream.of(Assertions::withPrecision, BDDAssertions::withPrecision, withAssertions::withPrecision);
  }

}
