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
package org.assertj.core.api.double_;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

import org.assertj.core.api.DoubleAssert;

/**
 * A class provides shared test parameters for {@link DoubleAssert}-related test classes.
 * 
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class DoubleAssertTestParameters {

  private static LongStream signs() {
    return LongStream.of(0b0__00000000_000__00000000_00000000_00000000_00000000_00000000_00000000__0000L,
                         0b1__00000000_000__00000000_00000000_00000000_00000000_00000000_00000000__0000L);
  }

  static DoubleStream zeros() {
    return signs()
      .mapToDouble(Double::longBitsToDouble)
      .peek(v-> assertThat(v).isZero().isNotNaN().isFinite());
  }

  static DoubleStream subnormalValues() {
    return LongStream.range(0, 128)
      .map(i -> {
        final long e = 0b0__00000000_000__00000000_00000000_00000000_00000000_00000000_00000000__0000L;
        final long g = (current().nextLong(Long.MAX_VALUE) + 1L) >>> 12;
        return e | g;
      })
      .flatMap(v -> signs().map(s -> s | v))
      .mapToDouble(Double::longBitsToDouble)
      .peek(v -> assertThat(v).isNotZero().isNotNaN().isFinite());
  }

  static DoubleStream normalValues() {
    return LongStream.range(0, 128)
      .map(i -> {
        final long e = current().nextLong(0x001L, 0x7FFL) << 52;
        final long g = current().nextInt() >>> 12;
        return e | g;
      })
      .flatMap(v -> signs().map(s -> s | v))
      .mapToDouble(Double::longBitsToDouble)
      .peek(v -> assertThat(v).isNotZero().isNotNaN().isFinite());
  }

  private DoubleAssertTestParameters() {
    super();
  }
}
