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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldHaveExtension.shouldHaveExtension;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

/**
 * @author Jean-Christophe Gay
 */
class ShouldHaveExtension_create_Test {

  private final String expectedExtension = "java";

  private final File actual = new FakeFile("actual-file.png");

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveExtension(actual, "png", expectedExtension);
    // WHEN
    String message = factory.create(new TestDescription("TEST"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  " + actual + "%n" +
                                   "to have extension:%n" +
                                   "  \"" + expectedExtension + "\"%n" +
                                   "but had:%n" +
                                   "  \"png\"."));
  }

  @Test
  void should_create_error_message_when_actual_does_not_have_extension() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveExtension(actual, null, expectedExtension);
    // WHEN
    String message = factory.create(new TestDescription("TEST"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  " + actual + "%n" +
                                   "to have extension:%n" +
                                   "  \"" + expectedExtension + "\"%n" +
                                   "but had no extension."));
  }

  @Test
  void should_create_error_message_with_path_when_actual_does_not_have_extension() {
    // GIVEN
    Path actual = Paths.get("file");
    String expectedExtension = "txt";
    ErrorMessageFactory factory = shouldHaveExtension(actual, expectedExtension);
    // WHEN
    String message = factory.create(new TestDescription("TEST"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  " + actual + "%n" +
                                   "to have extension:%n" +
                                   "  \"" + expectedExtension + "\"%n" +
                                   "but had no extension."));
  }

  @Test
  void should_create_error_message_with_path_when_actual_has_extension() {
    // GIVEN
    Path actual = Paths.get("file.txt");
    String expectedExtension = "log";
    ErrorMessageFactory factory = shouldHaveExtension(actual, "txt", expectedExtension);
    // WHEN
    String message = factory.create(new TestDescription("TEST"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  " + actual + "%n" +
                                   "to have extension:%n" +
                                   "  \"" + expectedExtension + "\"%n" +
                                   "but had:%n" +
                                   "  \"txt\"."));
  }

}
