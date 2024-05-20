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
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.FILE_SHOULD_BE_DIRECTORY;
import static org.assertj.core.error.ShouldBeDirectory.PATH_SHOULD_BE_DIRECTORY;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.nio.file.Path;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldBeDirectory_create_Test {

  private static final TestDescription DESCRIPTION = new TestDescription("Test");

  @Test
  void should_create_error_message_for_Path() {
    // GIVEN
    final Path path = mock(Path.class);
    // WHEN
    String message = shouldBeDirectory(path).create(DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[Test] " + PATH_SHOULD_BE_DIRECTORY, path);
  }

  @Test
  void should_create_error_message_for_File() {
    final File file = new FakeFile("xyz");
    // WHEN
    String message = shouldBeDirectory(file).create(DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[Test] " + FILE_SHOULD_BE_DIRECTORY, file);
  }
}
