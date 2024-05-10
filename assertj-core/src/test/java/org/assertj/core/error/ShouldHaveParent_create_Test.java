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
import static org.assertj.core.error.ShouldHaveParent.FILE_NOT_EXPECTED_PARENT;
import static org.assertj.core.error.ShouldHaveParent.FILE_NO_PARENT;
import static org.assertj.core.error.ShouldHaveParent.PATH_NOT_EXPECTED_PARENT;
import static org.assertj.core.error.ShouldHaveParent.PATH_NO_PARENT;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.nio.file.Path;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author Jean-Christophe Gay
 * @author Francis Galiegue
 */
class ShouldHaveParent_create_Test {
  private final File expectedFileParent = new FakeFile("expected.parent");
  private final Path expectedPathParent = mock(Path.class);
  @TempDir
  File tempDir;
  private TestDescription description;

  @BeforeEach
  void setup() {
    description = new TestDescription("Test");
  }

  @Test
  void should_create_error_message_when_file_has_no_parent() {
    // GIVEN
    final File actual = new FakeFile("actual", true);
    ErrorMessageFactory factory = shouldHaveParent(actual, expectedFileParent);
    // WHEN
    String actualMessage = factory.create(description, STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo(format("[Test] " + FILE_NO_PARENT, actual, expectedFileParent));
  }

  @Test
  void should_create_error_message_when_file_does_not_have_expected_parent() {
    // GIVEN
    final File actual = new File(tempDir, "actual");
    // WHEN
    String actualMessage = shouldHaveParent(actual, expectedFileParent).create(description, STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo(format("[Test] " + FILE_NOT_EXPECTED_PARENT, actual, expectedFileParent, tempDir));
  }

  @Test
  void should_create_error_message_when_path_has_no_parent() {
    // GIVEN
    final Path actual = mock(Path.class);
    // WHEN
    String actualMessage = shouldHaveParent(actual, expectedPathParent).create(description, STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo(format("[Test] " + PATH_NO_PARENT, actual, expectedPathParent));
  }

  @Test
  void should_create_error_message_when_path_does_not_have_expected_parent() {
    // GIVEN
    final Path actual = mock(Path.class);
    final Path actualParent = mock(Path.class);
    // WHEN
    String actualMessage = shouldHaveParent(actual, actualParent, expectedPathParent).create(description,
                                                                                             STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo(format("[Test] " + PATH_NOT_EXPECTED_PARENT, actual, expectedPathParent, actualParent));
  }
}
