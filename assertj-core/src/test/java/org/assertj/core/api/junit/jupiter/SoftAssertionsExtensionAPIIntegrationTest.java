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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.EventConditions.test;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.DefaultAssertionErrorCollector;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.error.AssertJMultipleFailuresError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.testkit.engine.EngineTestKit;

/**
 * Integration tests for the public API functions of {@link SoftAssertionsExtension}.
 *
 * @author Fr Jeremy Krieg
 * @since 3.18
 */
class SoftAssertionsExtensionAPIIntegrationTest {

  @Disabled("Executed via the JUnit Platform Test Kit")
  @ExtendWith(ExtensionInjector.class)
  @ExtendWith(SoftAssertionsExtension.class)
  static class APITest {

    static Map<String, AssertionErrorCollector> map = new HashMap<>();

    @BeforeAll
    static void beforeAll() {
      map.clear();
    }

    @BeforeEach
    void beforeEach(ExtensionContext context) {
      SoftAssertions provider = SoftAssertionsExtension.getSoftAssertionsProvider(context, SoftAssertions.class);
      assertThat(provider.assertionErrorsCollected()).isEmpty();
      provider.assertThat("something").isEqualTo("nothing");
      assertThat(provider.assertionErrorsCollected()).as("beforeEach:after assert").hasSize(1);
      AssertionErrorCollector collector = SoftAssertionsExtension.getAssertionErrorCollector(context);
      assertThat(collector).isInstanceOf(DefaultAssertionErrorCollector.class);
      assertThat(provider.getDelegate()).contains(collector);
      map.put(context.getTestMethod().get().getName(), collector);
    }

    @Test
    void multipleFailuresCustom(ExtensionContext context, CustomSoftAssertions softly) {
      AssertionErrorCollector collector = SoftAssertionsExtension.getAssertionErrorCollector(context);
      assertThat(collector.assertionErrorsCollected()).as("init").hasSize(1);
      softly.expectThat(1).isEqualTo(0);
      assertThat(collector.assertionErrorsCollected()).as("after first").hasSize(2);
      SoftAssertions provider = SoftAssertionsExtension.getSoftAssertionsProvider(context, SoftAssertions.class);
      provider.assertThat(2).isEqualTo(2);
      assertThat(collector.assertionErrorsCollected()).as("after second").hasSize(2);
      provider.assertThat(2).isEqualTo(1);
      assertThat(collector.assertionErrorsCollected()).as("after third").hasSize(3);
      softly.expectThat(3).isEqualTo(4);
      assertThat(collector.assertionErrorsCollected()).as("after fourth").hasSize(4);
    }

    @Test
    void multipleFailuresBDD(ExtensionContext context, BDDSoftAssertions softly) {
      AssertionErrorCollector collector = SoftAssertionsExtension.getAssertionErrorCollector(context);
      assertThat(collector.assertionErrorsCollected()).as("init").hasSize(1);
      softly.then(1).isEqualTo(0);
      assertThat(collector.assertionErrorsCollected()).as("after first").hasSize(2);
      CustomSoftAssertions provider = SoftAssertionsExtension.getSoftAssertionsProvider(context, CustomSoftAssertions.class);
      provider.expectThat(2).isEqualTo(2);
      assertThat(collector.assertionErrorsCollected()).as("after second").hasSize(2);
      softly.then(3).isEqualTo(4);
      assertThat(collector.assertionErrorsCollected()).as("after third").hasSize(3);
    }

  }

  @Test
  void apiTest() {
    EngineTestKit.engine("junit-jupiter")
                 .selectors(selectClass(APITest.class))
                 .configurationParameter("junit.jupiter.conditions.deactivate", "*")
                 .execute()
                 .testEvents()
                 .assertStatistics(stats -> stats.started(2).succeeded(0).failed(2))
                 .failed()
                 // @format:off
                 .assertThatEvents().haveExactly(1,
                                                 event(test("multipleFailuresCustom"),
                                                       finishedWithFailure(instanceOf(AssertJMultipleFailuresError.class),
                                                                           message(msg -> msg.contains("Multiple Failures (4 failures)")))))
                                    .haveExactly(1,
                                                 event(test("multipleFailuresBDD"),
                                                       finishedWithFailure(instanceOf(AssertJMultipleFailuresError.class),
                                                                           message(msg -> msg.contains("Multiple Failures (3 failures)")))));
                 // @format:on
    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      List<AssertionError> collected = APITest.map.get("multipleFailuresCustom").assertionErrorsCollected();
      softly.assertThat(collected).as("size").hasSize(4);
      softly.assertThat(collected.get(0)).as("zero").hasMessageContaining("something").hasMessageContaining("nothing");
      softly.assertThat(collected.get(1)).as("one").hasMessageContaining("1").hasMessageContaining("0");
      softly.assertThat(collected.get(2)).as("two").hasMessageContaining("2").hasMessageContaining("1");
      softly.assertThat(collected.get(3)).as("three").hasMessageContaining("3").hasMessageContaining("4");

      collected = APITest.map.get("multipleFailuresBDD").assertionErrorsCollected();
      softly.assertThat(collected).as("size2").hasSize(3);
      softly.assertThat(collected.get(0)).as("zero2").hasMessageContaining("something").hasMessageContaining("nothing");
      softly.assertThat(collected.get(1)).as("one2").hasMessageContaining("1").hasMessageContaining("0");
      softly.assertThat(collected.get(2)).as("two2").hasMessageContaining("3").hasMessageContaining("4");
    }
  }

}
