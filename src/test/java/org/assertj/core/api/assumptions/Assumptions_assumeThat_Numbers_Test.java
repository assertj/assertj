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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static java.math.BigDecimal.ZERO;
import static java.math.BigInteger.ONE;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.AssumptionViolatedException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class Assumptions_assumeThat_Numbers_Test {

  static Stream<AssumptionRunner<?>> provideAssumptionsRunners() {
    return Stream.of(
        new AssumptionRunner<Byte>() {
          @Override
          public void runFailingAssumption() {
            assumeThat((byte) 4).isLessThan((byte) 2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat((byte) 4).isGreaterThan((byte) 2);
          }
        },
        new AssumptionRunner<Byte>((byte) 4) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).isLessThan((byte) 2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).isGreaterThan((byte) 2);
          }
        },
        new AssumptionRunner<byte[]>(new byte[] { 2, 4, 2 }) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).containsOnlyOnce(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).containsOnlyOnce(4);
          }
        },
        new AssumptionRunner<Short>() {
          @Override
          public void runFailingAssumption() {
            assumeThat((short) 4).isLessThan((short) 2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat((short) 4).isGreaterThan((short) 2);
          }
        },
        new AssumptionRunner<Short>((short) 4) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).isLessThan((short) 2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).isGreaterThan((short) 2);
          }
        },
        new AssumptionRunner<short[]>(new short[] { 2, 4, 2 }) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).containsOnlyOnce((short) 2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).containsOnlyOnce((short) 4);
          }
        },
        new AssumptionRunner<Integer>() {
          @Override
          public void runFailingAssumption() {
            assumeThat(4).isLessThan(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(4).isGreaterThan(2);
          }
        },
        new AssumptionRunner<Integer>(4) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).isLessThan(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).isGreaterThan(2);
          }
        },
        new AssumptionRunner<int[]>(new int[] { 2, 4, 2 }) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).containsOnlyOnce(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).containsOnlyOnce(4);
          }
        },
        new AssumptionRunner<Long>() {
          @Override
          public void runFailingAssumption() {
            assumeThat(4L).isLessThan(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(4L).isGreaterThan(2);
          }
        },
        new AssumptionRunner<Long>(4L) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).isLessThan(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).isGreaterThan(2);
          }
        },
        new AssumptionRunner<long[]>(new long[] { 2, 4, 2 }) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).containsOnlyOnce(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).containsOnlyOnce(4);
          }
        },

        new AssumptionRunner<Float>() {
          @Override
          public void runFailingAssumption() {
            assumeThat(4.0f).isLessThan(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(4.0f).isGreaterThan((byte) 2);
          }
        },
        new AssumptionRunner<Float>(4.0f) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).isLessThan(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).isGreaterThan(2);
          }
        },
        new AssumptionRunner<float[]>(new float[] { 2, 4, 2 }) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasSize(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasSize(3);
          }
        },
        new AssumptionRunner<Double>() {
          @Override
          public void runFailingAssumption() {
            assumeThat(4.0).isLessThan(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(4.0).isGreaterThan((byte) 2);
          }
        },
        new AssumptionRunner<Double>(4.0) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).isLessThan(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).isGreaterThan(2);
          }
        },
        new AssumptionRunner<double[]>(new double[] { 2, 4, 2 }) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasSize(2);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasSize(3);
          }
        },
        new AssumptionRunner<BigDecimal>(new BigDecimal(4)) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).isLessThan(ZERO);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).isGreaterThan(ZERO);
          }
        },
        new AssumptionRunner<BigInteger>(BigInteger.valueOf(4)) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).isLessThan(ONE);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).isGreaterThan(ONE);
          }
        });
  }

  @ParameterizedTest
  @MethodSource("provideAssumptionsRunners")
  public void should_ignore_test_when_assumption_fails(AssumptionRunner<?> assumptionRunner) {
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumptionRunner.runFailingAssumption());
  }

  @ParameterizedTest
  @MethodSource("provideAssumptionsRunners")
  public void should_run_test_when_assumption_passes(AssumptionRunner<?> assumptionRunner) {
    assertThatCode(() -> assumptionRunner.runPassingAssumption()).doesNotThrowAnyException();
  }
}
