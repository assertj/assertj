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
package org.assertj.core.api.abstract_;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.configuration.Configuration;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.description.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AbstractAssert describedAs")
class AbstractAssert_describedAs_printed_to_console_Test {

  private static ByteArrayOutputStream systemOutContent;
  private final static PrintStream originalSystemOut = System.out;
  private final static boolean originalIsPrintAssertionsDescriptionEnabled = assertJConfig().printAssertionsDescription();

  private final static Consumer<Description> originalConsumerDescription = assertJConfig().consumerDescription();

  private static Configuration assertJConfig() {
    return ConfigurationProvider.CONFIGURATION_PROVIDER.configuration();
  }

  @BeforeEach
  void setUpStreams() {
    Assertions.setPrintAssertionsDescription(true);
    systemOutContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(systemOutContent));
  }

  @AfterEach
  void restoreOriginalState() {
    System.setOut(originalSystemOut);
    Assertions.setPrintAssertionsDescription(originalIsPrintAssertionsDescriptionEnabled);
    Assertions.setConsumerDescription(originalConsumerDescription);
  }

  @Test
  void should_print_successful_assertions_description_to_console_with_new_line() {
    // GIVEN
    String description = RandomStringUtils.randomAlphanumeric(20);
    // WHEN
    assertThat("abc").as(description + "1")
                     .startsWith("a")
                     .as(description + "2")
                     .contains("b")
                     .as(" ")
                     .endsWith("c");
    // THEN
    then(systemOutContent).hasToString(format("%s%n%s%n %n", description + "1", description + "2"));
  }

  @Test
  void should_print_successful_assertions_description_to_console_with_new_line_until_first_failed_assertion_included() {
    // GIVEN
    String description = RandomStringUtils.randomAlphanumeric(20);
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat("abc").as(description + "1")
                                                                .startsWith("a")
                                                                .as(description + "2")
                                                                .startsWith("b")
                                                                .as("not printed as previous assertion failed")
                                                                .endsWith("a"));
    // THEN
    then(throwable).isInstanceOf(AssertionError.class);
    then(systemOutContent).hasToString(format("%s%n%s%n", description + "1", description + "2"));
  }

  @Test
  void should_print_all_soft_assertions_failed_or_successful() {
    // GIVEN
    String description = RandomStringUtils.randomAlphanumeric(20);
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat("abc").as("1" + description)
          .startsWith("a")
          .as("2" + description)
          .startsWith("b")
          .as("") // description not printed as it is empty
          .startsWith("c")
          .as("3" + description)
          .endsWith("a");
    // THEN
    then(systemOutContent).hasToString(format("%s%n%s%n%s%n", "1" + description, "2" + description, "3" + description));
    // we don't care about the assertions result, we just want to check the description
  }

  @Test
  void should_be_printed_and_consumed_by_configured_description_consumer() {
    final StringBuffer consumedDescription = new StringBuffer("");
    Assertions.setConsumerDescription(description -> consumedDescription.append(description.toString()));
    String description = RandomStringUtils.randomAlphanumeric(20);
    // WHEN
    assertThat("abc").as("1" + description)
                     .startsWith("a")
                     .as("2" + description)
                     .contains("b")
                     .as(" ")
                     .endsWith("c");
    // THEN
    then(consumedDescription).hasToString("1" + description + "2" + description + " ");
    then(systemOutContent).hasToString(format("%s%n%s%n %n", "1" + description, "2" + description));
  }

  @Test
  void should_not_print_assertions_description_to_console_by_default() {
    // GIVEN
    Assertions.setPrintAssertionsDescription(originalIsPrintAssertionsDescriptionEnabled);
    String description = RandomStringUtils.randomAlphanumeric(20);
    // WHEN
    assertThat("abc").as(description + "1")
                     .startsWith("a")
                     .as(description + "2")
                     .contains("b")
                     .as(" ")
                     .endsWith("c");
    // THEN
    then(systemOutContent.toString()).isEmpty();
  }

}
