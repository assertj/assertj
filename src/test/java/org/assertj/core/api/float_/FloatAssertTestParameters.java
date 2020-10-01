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
package org.assertj.core.api.float_;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.assertj.core.api.FloatAssert;

/**
 * A class provides shared test parameters for {@link FloatAssert}-related test classes.
 * 
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class FloatAssertTestParameters {

  private static IntStream signs() {
    return IntStream.of(0b0__00000000__00000000_00000000_0000_000,
                        0b1__00000000__00000000_00000000_0000_000);
  }

  static Stream<Float> zeros() {
    return signs()
      .mapToObj(Float::intBitsToFloat)
      .peek(v -> assertThat(v.floatValue()).isZero().isNotNaN().isFinite());
  }

  static Stream<Float> subnormalValues() {
    return IntStream.range(0, 128)
      .map(i -> {
        final int e = 0b0__00000000__00000000_00000000_0000_000;
        final int g = (current().nextInt(Integer.MAX_VALUE) + 1) >>> 9;
        return e | g;
      })
      .flatMap(v -> signs().map(s -> s | v))
      .mapToObj(Float::intBitsToFloat)
      .peek(v ->        assertThat(v.floatValue()).isNotZero().isNotNaN().isFinite()      );
  }

  static Stream<Float> normalValues() {
    return IntStream.range(0, 128)
      .map(i -> {
        int e = current().nextInt(0x01, 0xFF) << 23;
        int g = current().nextInt() >>> 9;
        return e | g;
      })
      .flatMap(v -> signs().map(s -> s | v))
      .mapToObj(Float::intBitsToFloat)
      .peek(v ->        assertThat(v.floatValue()).isNotZero().isNotNaN().isFinite());
  }

  private FloatAssertTestParameters() {
    super();
  }
}
