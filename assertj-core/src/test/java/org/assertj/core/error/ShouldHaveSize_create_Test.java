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
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for <code>{@link ShouldHaveSize#create(Description, Representation)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
class ShouldHaveSize_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  void setUp() {
    factory = shouldHaveSize(list('a', 'b'), 4, 2);
  }

  @Test
  void should_create_error_message() {
    // WHEN
    String message = factory.create(new TestDescription("TEST"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(String.format("[TEST] %nExpected size: 2 but was: 4 in:%n['a', 'b']"));
  }

  @Test
  void should_create_error_message_with_hexadecimal_representation() {
    // WHEN
    String message = factory.create(new TestDescription("TEST"), new HexadecimalRepresentation());
    // THEN
    then(message).isEqualTo(String.format("[TEST] %nExpected size: 2 but was: 4 in:%n['0x0061', '0x0062']"));
  }

  @Test
  void should_create_error_message_for_incorrect_file_size() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveSize(new FakeFile("ab%sc"), 3L);
    // WHEN
    String actualErrorMessage = factory.create(new TestDescription("TEST"), STANDARD_REPRESENTATION);
    // THEN
    then(actualErrorMessage).isEqualTo(format("[TEST] %n"
                                              + "Expecting file%n"
                                              + "  ab%%sc%n"
                                              + "to have a size of:%n"
                                              + "  3L bytes%n"
                                              + "but had:%n"
                                              + "  0L bytes"));
  }

  @Test
  void should_create_error_message_for_incorrect_path_size(@TempDir Path tempDir) throws IOException {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "content".getBytes());
    // WHEN
    String actualErrorMessage = shouldHaveSize(actual, 0L).create(new TestDescription("TEST"), STANDARD_REPRESENTATION);
    // THEN
    then(actualErrorMessage).isEqualTo("[TEST] %n"
                                       + "Expecting path%n"
                                       + "  %s%n"
                                       + "to have a size of:%n"
                                       + "  0L bytes%n"
                                       + "but had:%n"
                                       + "  7L bytes",
                                       actual);
  }

}
