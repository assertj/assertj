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
package org.assertj.core.api.junit.jupiter;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.platform.testkit.engine.EngineTestKit;

@DisplayName("SoftAssertionsExtension PER_CLASS concurrent injection test")
public class SoftAssertionsExtension_PER_CLASS_Concurrency_Test {
  // Use CountDownLatches to synchronize between the two parallel running tests to make sure that they overlap in time.
  @Disabled("Run by the testkit")
  @ExtendWith(SoftAssertionsExtension.class)
  @ExtendWith(ExtensionInjector.class)
  @Execution(ExecutionMode.CONCURRENT)
  @TestInstance(Lifecycle.PER_CLASS)
  static class ConcurrencyTest {

    @InjectSoftAssertions
    SoftAssertions softly;

    static CountDownLatch[] flags = new CountDownLatch[6];
    static Map<String, AssertionErrorCollector> map = Collections.synchronizedMap(new HashMap<>());

    @BeforeAll
    static void beforeAll() {
      map.clear();
      for (int i = 0; i < flags.length; i++) {
        flags[i] = new CountDownLatch(1);
      }
    }

    @BeforeEach
    void beforeEach(ExtensionContext context) {
      map.put(context.getTestMethod().get().getName(), SoftAssertionsExtension.getAssertionErrorCollector(context));
    }

    static void waitForFlag(int flagNum) {
      try {
        if (!flags[flagNum].await(5000, MILLISECONDS)) {
          throw new IllegalStateException("Timed out while waiting for flag " + flagNum);
        }
      } catch (InterruptedException e) {
        throw new IllegalStateException("Interrupted while waiting for flag " + flagNum, e);
      }
    }

    @Test
    void test1(ExtensionContext context) {
      softly.assertThat(1).isEqualTo(0);
      flags[0].countDown();
      waitForFlag(1);
      softly.assertThat(3).isEqualTo(4);
      flags[2].countDown();
      waitForFlag(3);
      softly.assertThat(5).isEqualTo(6);
      map.put(context.getTestMethod().get().getName(), SoftAssertionsExtension.getAssertionErrorCollector(context));
    }

    @Test
    void test2(@SuppressWarnings("unused") ExtensionContext context) {
      waitForFlag(0);
      softly.assertThat(2).isEqualTo(1);
      flags[1].countDown();
      waitForFlag(2);
      softly.assertThat(4).isEqualTo(5);
      flags[3].countDown();
    }
  }

  @Test
  void concurrent_tests_with_explicit_per_class_annotation_do_not_interfere() {
    EngineTestKit.engine("junit-jupiter")
                 .selectors(selectClass(ConcurrencyTest.class))
                 .configurationParameter("junit.jupiter.conditions.deactivate", "*")
                 .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                 .execute()
                 .testEvents()
                 .debug(System.err)
                 .assertStatistics(stats -> stats.started(2).succeeded(0).failed(2))
                 .failed();

    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      AssertionErrorCollector collector = ConcurrencyTest.map.get("test1");
      softly.assertThat(collector).as("test1").isNotNull();
      if (softly.wasSuccess()) {
        List<AssertionError> collected = collector.assertionErrorsCollected();
        softly.assertThat(collected).as("size").hasSize(3);
        softly.assertThat(collected.get(0)).as("zero").hasMessageContainingAll("1", "0");
        softly.assertThat(collected.get(1)).as("one").hasMessageContainingAll("3", "4");
        softly.assertThat(collected.get(2)).as("two").hasMessageContainingAll("5", "6");
      }
      collector = ConcurrencyTest.map.get("test2");
      softly.assertThat(collector).as("test2").isNotNull();
      if (softly.wasSuccess()) {
        List<AssertionError> collected = collector.assertionErrorsCollected();
        softly.assertThat(collected).as("size2").hasSize(2);
        softly.assertThat(collected.get(0)).as("zero2").hasMessageContainingAll("2", "1");
        softly.assertThat(collected.get(1)).as("one2").hasMessageContainingAll("4", "5");
      }
    }
  }
}
