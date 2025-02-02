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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Consumer;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.configuration.Configuration;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.description.Description;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractAssert_describedAs_consumed_by_configured_consumer_Test {

  private static String consumedDescription;
  private final static boolean originalIsPrintAssertionsDescriptionEnabled = assertJConfig().printAssertionsDescription();
  private final static Consumer<Description> originalDescriptionConsumer = assertJConfig().descriptionConsumer();
  private final static Consumer<Description> DESCRIPTION_CONSUMER = description -> consumedDescription += "%s/".formatted(description);

  private static Configuration assertJConfig() {
    return ConfigurationProvider.CONFIGURATION_PROVIDER.configuration();
  }

  @BeforeAll
  static void beforeAllTests() {
    Assertions.setPrintAssertionsDescription(true);
  }

  @BeforeEach
  void beforeEachTest() {
    Assertions.setDescriptionConsumer(DESCRIPTION_CONSUMER);
    consumedDescription = "";
  }

  @AfterAll
  static void restoreOriginalState() {
    Assertions.setPrintAssertionsDescription(originalIsPrintAssertionsDescriptionEnabled);
    Assertions.setDescriptionConsumer(originalDescriptionConsumer);
  }

  @Test
  void should_be_consumed_by_configured_description_consumer_on_successful_assertions() {
    // GIVEN
    String description = RandomStringUtils.secure().next(20);
    // WHEN
    assertThat("abc").as("1" + description)
                     .startsWith("a")
                     .as("2" + description)
                     .contains("b")
                     .as("") // description captured even if empty, consumer can filter it if needed
                     .endsWith("c");
    // THEN
    then(consumedDescription).isEqualTo("1" + description + "/2" + description + "//");
  }

  @Test
  void should_be_consumed_by_configured_description_consumer_until_first_failed_assertion_included() {
    // GIVEN
    String description = RandomStringUtils.secure().next(20);
    // WHEN
    catchThrowable(() -> assertThat("abc").as("1" + description)
                                          .startsWith("a")
                                          .as("2" + description)
                                          .startsWith("b")
                                          .as("not displayed as previous assertion failed")
                                          .endsWith("a"));
    // THEN
    then(consumedDescription).isEqualTo("1" + description + "/2" + description + "/");
  }

  @Test
  void should_be_consumed_by_configured_description_consumer_on_all_soft_assertions_failed_or_successful() {
    // GIVEN
    String description = RandomStringUtils.secure().next(20);
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat("abc").as("1" + description)
          .startsWith("a")
          .as("2" + description)
          .startsWith("b")
          .as("3" + description)
          .endsWith("a");
    // THEN
    then(consumedDescription).isEqualTo("1" + description + "/2" + description + "/3" + description + "/");
    // we don't care about the assertions result, we just want to check the description
  }

}
