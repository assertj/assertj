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
package org.assertj.core.api.junit.jupiter;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.EventConditions.test;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

import org.assertj.core.error.AssertJMultipleFailuresError;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;

/**
 * Abstract base class for integration tests involving the {@link SoftAssertionsExtension}.
 *
 * @author Sam Brannen
 * @see SoftAssertionsExtensionIntegrationTest
 * @see BDDSoftAssertionsExtensionIntegrationTest
 * @since 3.13
 */
abstract class AbstractSoftAssertionsExtensionIntegrationTests {

  @Test
  final void test_instance_per_method() {
    assertExecutionResults(getTestInstancePerMethodTestCase(), false);
  }

  @Test
  void test_instance_per_class() {
    assertExecutionResults(getTestInstancePerClassTestCase(), false);
  }

  @Test
  void test_instance_per_method_with_nested_tests() {
    assertExecutionResults(getTestInstancePerMethodNestedTestCase(), true);
  }

  @Test
  void test_instance_per_class_with_nested_tests() {
    assertExecutionResults(getTestInstancePerClassNestedTestCase(), true);
  }

  protected abstract Class<?> getTestInstancePerMethodTestCase();

  protected abstract Class<?> getTestInstancePerClassTestCase();

  protected abstract Class<?> getTestInstancePerMethodNestedTestCase();

  protected abstract Class<?> getTestInstancePerClassNestedTestCase();

  private void assertExecutionResults(Class<?> testClass, boolean nested) {
    EngineTestKit.engine("junit-jupiter")
                 .selectors(selectClass(testClass))
                 .configurationParameter("junit.jupiter.conditions.deactivate", "*")
                 .execute().testEvents()
                 .assertStatistics(stats -> stats.started(nested ? 8 : 4).succeeded(nested ? 4 : 2).failed(nested ? 4 : 2))
                 .failed()
                 // @format:off
                 .assertThatEvents().haveExactly(nested ? 2 : 1,
                                                 event(test("multipleFailures"),
                                                       finishedWithFailure(instanceOf(AssertJMultipleFailuresError.class),
                                                                           message(msg -> msg.contains("Multiple Failures (2 failures)")))))
                                    .haveExactly(nested ? 2 : 1,
                                                 event(test("parameterizedTest"),
                                                       finishedWithFailure(instanceOf(AssertJMultipleFailuresError.class),
                                                                           message(msg -> msg.contains("Multiple Failures (1 failure)")))));
                 // @format:on
  }

}
