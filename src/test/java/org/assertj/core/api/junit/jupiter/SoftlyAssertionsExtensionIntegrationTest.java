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
package org.assertj.core.api.junit.jupiter;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.EventType;

/**
 * Integration tests for {@link SoftlyExtension}.
 */
@DisplayName("JUnit Jupiter SoftlyExtension integration tests")
class SoftlyAssertionsExtensionIntegrationTest extends AbstractSoftAssertionsExtensionIntegrationTests {

  @Override
  protected Class<?> getTestInstancePerMethodTestCase() {
    return TestInstancePerMethodExample.class;
  }

  @Override
  protected Class<?> getTestInstancePerClassTestCase() {
    return TestInstancePerClassExample.class;
  }

  @Override
  protected Class<?> getTestInstancePerMethodNestedTestCase() {
    return TestInstancePerMethodNestedExample.class;
  }

  @Override
  protected Class<?> getTestInstancePerClassNestedTestCase() {
    return TestInstancePerClassNestedExample.class;
  }

  // -------------------------------------------------------------------------

  @Override
  @Test
  void test_instance_per_class() {
    check_PerClass_lifecycle_tests_are_detected_as_invalid(TestInstancePerClassExample.class);
  }

  @Override
  @Test
  void test_instance_per_class_with_nested_tests() {
    check_PerClass_lifecycle_tests_are_detected_as_invalid(TestInstancePerClassNestedExample.class);
  }

  private static void check_PerClass_lifecycle_tests_are_detected_as_invalid(Class<?> clazz) {
    EngineTestKit.engine("junit-jupiter")
                 .selectors(selectClass(clazz))
                 .configurationParameter("junit.jupiter.conditions.deactivate", "*")
                 .execute().allEvents()
                 .assertStatistics(stats -> stats.started(2).succeeded(1).failed(1))
                 .assertThatEvents().anySatisfy(event -> {
                   assertThat(event.getType()).isEqualTo(EventType.FINISHED);
                   // payload and throwable are Optional fields, we need to extract their value
                   assertThat(event).extracting("payload.value.status")
                                    .isEqualTo(Status.FAILED);
                   assertThat(event).extracting("payload.value.throwable.value", as(THROWABLE))
                                    .isInstanceOf(IllegalStateException.class)
                                    .hasMessageStartingWith("A SoftAssertions field is not permitted in test classes with PER_CLASS life cycle");
                 });
  }

  @Test
  void test_too_many_SoftlyExtension_fields() {
    EngineTestKit.engine("junit-jupiter")
                 .selectors(selectClass(TestInstancePerMethodWithTwoSoftAssertionsFields.class))
                 .configurationParameter("junit.jupiter.conditions.deactivate", "*")
                 .execute().testEvents()
                 .assertStatistics(stats -> stats.started(1).succeeded(0).failed(1))
                 .assertThatEvents().anySatisfy(event -> {
                   assertThat(event.getType()).isEqualTo(EventType.FINISHED);
                   assertThat(event).extracting("payload.value.status")
                                    .isEqualTo(Status.FAILED);
                   // payload and throwable are Optional fields, we need to extract their value
                   assertThat(event).extracting("payload.value.throwable.value", as(THROWABLE))
                                    .isInstanceOf(IllegalStateException.class)
                                    .hasMessageStartingWith("Only one field of type org.assertj.core.api.SoftAssertions should be defined but found 2");
                 });
  }

  @Test
  void test_should_raise_an_IllegalStateException_if_no_SoftlyExtension_fields_is_found() {
    EngineTestKit.engine("junit-jupiter")
                 .selectors(selectClass(TestInstancePerMethodWithNoSoftAssertionsFields.class))
                 .configurationParameter("junit.jupiter.conditions.deactivate", "*")
                 .execute().testEvents()
                 .assertStatistics(stats -> stats.started(1).succeeded(0).failed(1))
                 .assertThatEvents().anySatisfy(event -> {
                   assertThat(event.getType()).isEqualTo(EventType.FINISHED);
                   assertThat(event).extracting("payload.value.status")
                                    .isEqualTo(Status.FAILED);
                   // payload and throwable are Optional fields, we need to extract their value
                   assertThat(event).extracting("payload.value.throwable.value", as(THROWABLE))
                                    .isInstanceOf(IllegalStateException.class)
                                    .hasMessageStartingWith("No SoftlyExtension field found");
                 });
  }

  // -------------------------------------------------------------------------

  @ExtendWith(SoftlyExtension.class)
  @TestMethodOrder(OrderAnnotation.class)
  private static abstract class AbstractSoftAssertionsExample {

    private SoftAssertions softly;

    @Test
    @Order(1)
    void multipleFailures() {
      softly.assertThat(1).isEqualTo(0);
      softly.assertThat(2).isEqualTo(2);
      softly.assertThat(3).isEqualTo(4);
    }

    @Test
    @Order(2)
    void allAssertionsShouldPass() {
      softly.assertThat(1).isEqualTo(1);
      softly.assertThat(list(1, 2)).containsOnly(1, 2);
    }

    @ParameterizedTest
    @CsvSource({ "1, 1, 2", "1, 2, 3" })
    @Order(3)
    void parameterizedTest(int a, int b, int sum) {
      softly.assertThat(a + b).as("sum").isEqualTo(sum);
      softly.assertThat(a).as("operand 1 is equal to operand 2").isEqualTo(b);
    }
  }

  @TestInstance(PER_METHOD)
  @Disabled("Executed via the JUnit Platform Test Kit")
  static class TestInstancePerMethodExample extends AbstractSoftAssertionsExample {
  }

  @TestInstance(PER_CLASS)
  @Disabled
  static class TestInstancePerClassExample extends AbstractSoftAssertionsExample {
  }

  @TestInstance(PER_METHOD)
  @Disabled("Executed via the JUnit Platform Test Kit")
  static class TestInstancePerMethodNestedExample extends AbstractSoftAssertionsExample {

    @Nested
    @Disabled("Executed via the JUnit Platform Test Kit")
    class InnerExample extends AbstractSoftAssertionsExample {
    }
  }

  @TestInstance(PER_CLASS)
  @Disabled("Executed via the JUnit Platform Test Kit")
  static class TestInstancePerClassNestedExample extends AbstractSoftAssertionsExample {

    @Nested
    @Disabled("Executed via the JUnit Platform Test Kit")
    class InnerExample extends AbstractSoftAssertionsExample {
    }
  }

  @TestInstance(PER_METHOD)
  @ExtendWith(SoftlyExtension.class)
  @Disabled("Executed via the JUnit Platform Test Kit")
  static class TestInstancePerMethodWithTwoSoftAssertionsFields {

    private SoftAssertions softly1;
    private SoftAssertions softly2;

    @Test
    void allAssertionsShouldPass() {
      softly1.assertThat(1).isEqualTo(1);
      softly2.assertThat(list(1, 2)).containsOnly(1, 2);
    }

  }

  @TestInstance(PER_METHOD)
  @ExtendWith(SoftlyExtension.class)
  @Disabled("Executed via the JUnit Platform Test Kit")
  static class TestInstancePerMethodWithNoSoftAssertionsFields {

    @Test
    void allAssertionsShouldPass() {
      assertThat(1).isEqualTo(1);
    }

  }

}
