/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.util.Arrays.array;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class Assumptions_assumeThat_Atomics_Test {

  private static int ranTests = 0;

  private static final VolatileFieldsHolder VOLATILE_FIELDS_HOLDER = new VolatileFieldsHolder();

  private AssumptionRunner<?> assumptionRunner;

  public Assumptions_assumeThat_Atomics_Test(AssumptionRunner<?> assumptionRunner) {
    this.assumptionRunner = assumptionRunner;
  }

  @Parameters
  public static Object[][] provideAssumptionsRunners() {
    return new AssumptionRunner[][] {
        { new AssumptionRunner<AtomicBoolean>(new AtomicBoolean(true)) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).isFalse();
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).isTrue();
          }
        } },
        { new AssumptionRunner<AtomicInteger>(new AtomicInteger(42)) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasNegativeValue();
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasPositiveValue();
          }
        } },
        { new AssumptionRunner<AtomicIntegerArray>(new AtomicIntegerArray(new int[] { 2, 5, 7 })) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).contains(20);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).contains(7);
          }
        } },
        { new AssumptionRunner<AtomicIntegerFieldUpdater<VolatileFieldsHolder>>(AtomicIntegerFieldUpdater.newUpdater(VolatileFieldsHolder.class,
                                                                                                                     "intValue")) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasValue(10, VOLATILE_FIELDS_HOLDER);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasValue(0, VOLATILE_FIELDS_HOLDER);
          }
        } },
        { new AssumptionRunner<AtomicLong>(new AtomicLong(42)) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasNegativeValue();
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasPositiveValue();
          }
        } },
        { new AssumptionRunner<AtomicLongArray>(new AtomicLongArray(new long[] { 2, 5, 7 })) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).contains(20);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).contains(7);
          }
        } },
        { new AssumptionRunner<AtomicLongFieldUpdater<VolatileFieldsHolder>>(AtomicLongFieldUpdater.newUpdater(VolatileFieldsHolder.class,
                                                                                                               "longValue")) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasValue(10L, VOLATILE_FIELDS_HOLDER);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasValue(0L, VOLATILE_FIELDS_HOLDER);
          }
        } },
        { new AssumptionRunner<AtomicReference<String>>(new AtomicReference<>("test")) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasValue("other");
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasValue("test");
          }
        } },
        { new AssumptionRunner<AtomicReferenceArray<String>>(new AtomicReferenceArray<>(array("2", "5", "7"))) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).contains("20");
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).contains("7");
          }
        } },
        { new AssumptionRunner<AtomicReferenceFieldUpdater<VolatileFieldsHolder, String>>(AtomicReferenceFieldUpdater.newUpdater(VolatileFieldsHolder.class,
                                                                                                                                 String.class,
                                                                                                                                 "stringValue")) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasValue("other", VOLATILE_FIELDS_HOLDER);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasValue("test", VOLATILE_FIELDS_HOLDER);
          }
        } },
        { new AssumptionRunner<AtomicMarkableReference<String>>(new AtomicMarkableReference<>("test", true)) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasReference("other");
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasReference("test");
          }
        } },
        { new AssumptionRunner<AtomicStampedReference<String>>(new AtomicStampedReference<>("test", 1)) {
          @Override
          public void runFailingAssumption() {
            assumeThat(actual).hasStamp(0);
          }

          @Override
          public void runPassingAssumption() {
            assumeThat(actual).hasStamp(1);
          }
        } }
    };
  }

  @AfterClass
  public static void afterClass() {
    assertThat(ranTests).as("number of tests run").isEqualTo(provideAssumptionsRunners().length);
  }

  @Test
  public void should_ignore_test_when_assumption_fails() {
    assumptionRunner.runFailingAssumption();
    fail("should not arrive here");

  }

  @Test
  public void should_run_test_when_assumption_passes() {
    assumptionRunner.runPassingAssumption();
    ranTests++;
  }

  @SuppressWarnings("unused")
  private static class VolatileFieldsHolder {
    volatile int intValue;
    volatile long longValue;
    volatile String stringValue = "test";
  }
}
