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

import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Offset.strictOffset;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import org.assertj.core.data.Offset;
import org.assertj.core.data.TemporalUnitLessThanOffset;
import org.assertj.core.data.TemporalUnitOffset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EntryPointAssertions_byLessThan_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("bigDecimalOffsetFactories")
  void should_create_BigDecimal_strictOffset(Function<BigDecimal, Offset<BigDecimal>> offsetFactory) {
    // GIVEN
    BigDecimal offsetValue = BigDecimal.ONE;
    // WHEN
    Offset<BigDecimal> offset = offsetFactory.apply(offsetValue);
    // THEN
    then(offset).isEqualTo(strictOffset(offsetValue));
  }

  private static Stream<Function<BigDecimal, Offset<BigDecimal>>> bigDecimalOffsetFactories() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }

  @ParameterizedTest
  @MethodSource("bigIntegerOffsetFactories")
  void should_create_BigInteger_strictOffset(Function<BigInteger, Offset<BigInteger>> offsetFactory) {
    // GIVEN
    BigInteger offsetValue = BigInteger.ONE;
    // WHEN
    Offset<BigInteger> offset = offsetFactory.apply(offsetValue);
    // THEN
    then(offset).isEqualTo(strictOffset(offsetValue));
  }

  private static Stream<Function<BigInteger, Offset<BigInteger>>> bigIntegerOffsetFactories() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }

  @ParameterizedTest
  @MethodSource("byteOffsetFactories")
  void should_create_Byte_strictOffset(Function<Byte, Offset<Byte>> offsetFactory) {
    // GIVEN
    Byte offsetValue = Byte.MAX_VALUE;
    // WHEN
    Offset<Byte> offset = offsetFactory.apply(offsetValue);
    // THEN
    then(offset).isEqualTo(strictOffset(offsetValue));
  }

  private static Stream<Function<Byte, Offset<Byte>>> byteOffsetFactories() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }

  @ParameterizedTest
  @MethodSource("doubleOffsetFactories")
  void should_create_Double_strictOffset(Function<Double, Offset<Double>> offsetFactory) {
    // GIVEN
    Double offsetValue = Double.MAX_VALUE;
    // WHEN
    Offset<Double> offset = offsetFactory.apply(offsetValue);
    // THEN
    then(offset).isEqualTo(strictOffset(offsetValue));
  }

  private static Stream<Function<Double, Offset<Double>>> doubleOffsetFactories() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }

  @ParameterizedTest
  @MethodSource("floatOffsetFactories")
  void should_create_Float_strictOffset(Function<Float, Offset<Float>> offsetFactory) {
    // GIVEN
    Float offsetValue = Float.MAX_VALUE;
    // WHEN
    Offset<Float> offset = offsetFactory.apply(offsetValue);
    // THEN
    then(offset).isEqualTo(strictOffset(offsetValue));
  }

  private static Stream<Function<Float, Offset<Float>>> floatOffsetFactories() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }

  @ParameterizedTest
  @MethodSource("integerOffsetFactories")
  void should_create_Integer_strictOffset(Function<Integer, Offset<Integer>> offsetFactory) {
    // GIVEN
    Integer offsetValue = Integer.MAX_VALUE;
    // WHEN
    Offset<Integer> offset = offsetFactory.apply(offsetValue);
    // THEN
    then(offset).isEqualTo(strictOffset(offsetValue));
  }

  private static Stream<Function<Integer, Offset<Integer>>> integerOffsetFactories() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }

  @ParameterizedTest
  @MethodSource("longOffsetFactories")
  void should_create_Long_strictOffset(Function<Long, Offset<Long>> offsetFactory) {
    // GIVEN
    Long offsetValue = Long.MAX_VALUE;
    // WHEN
    Offset<Long> offset = offsetFactory.apply(offsetValue);
    // THEN
    then(offset).isEqualTo(strictOffset(offsetValue));
  }

  private static Stream<Function<Long, Offset<Long>>> longOffsetFactories() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }

  @ParameterizedTest
  @MethodSource
  void should_create_temporal_strictOffset(BiFunction<Long, TemporalUnit, TemporalUnitOffset> offsetFactory) {
    // GIVEN
    long value = Long.MAX_VALUE;
    TemporalUnit temporalUnit = ChronoUnit.MINUTES;
    // WHEN
    TemporalUnitOffset offset = offsetFactory.apply(value, temporalUnit);
    // THEN
    then(offset).isEqualTo(new TemporalUnitLessThanOffset(value, temporalUnit));
  }

  private static Stream<BiFunction<Long, TemporalUnit, TemporalUnitOffset>> should_create_temporal_strictOffset() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }

  @ParameterizedTest
  @MethodSource("temporal_strictOffset_from_duration")
  void should_create_temporal_strictOffset_from_duration(Function<Duration, TemporalUnitOffset> offsetFactory) {
    // GIVEN
    Duration duration = Duration.ofNanos(123);
    // WHEN
    TemporalUnitOffset offset = offsetFactory.apply(duration);
    // THEN
    then(offset).isEqualTo(new TemporalUnitLessThanOffset(123, ChronoUnit.NANOS));
  }

  @ParameterizedTest
  @MethodSource("temporal_strictOffset_from_duration")
  void should_fail_if_duration_is_null(Function<Duration, TemporalUnitOffset> offsetFactory) {
    // GIVEN
    Duration duration = null;
    // WHEN
    NullPointerException npe = catchNullPointerException(() -> offsetFactory.apply(duration));
    // THEN
    then(npe).hasMessage("non null duration expected");
  }

  private static Stream<Function<Duration, TemporalUnitOffset>> temporal_strictOffset_from_duration() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }

  @ParameterizedTest
  @MethodSource("shortOffsetFactories")
  void should_create_Short_strictOffset(Function<Short, Offset<Short>> offsetFactory) {
    // GIVEN
    Short offsetValue = Short.MAX_VALUE;
    // WHEN
    Offset<Short> index = offsetFactory.apply(offsetValue);
    // THEN
    then(index).isEqualTo(strictOffset(offsetValue));
  }

  private static Stream<Function<Short, Offset<Short>>> shortOffsetFactories() {
    return Stream.of(Assertions::byLessThan, BDDAssertions::byLessThan, withAssertions::byLessThan);
  }
}
