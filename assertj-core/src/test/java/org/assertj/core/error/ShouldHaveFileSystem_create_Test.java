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
import static org.assertj.core.error.ShouldHaveFileSystem.shouldHaveFileSystem;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ShouldHaveFileSystem}.
 *
 * @author Ashley Scopes
 */
class ShouldHaveFileSystem_create_Test {

  @Test
  void should_create_error_message_for_expected_file_system() {
    // GIVEN
    Path actual = Paths.get("/foo/bar");
    FileSystem fileSystem = mock(FileSystem.class);
    given(fileSystem.toString()).willReturn("MySpecialFileSystem");
    // WHEN
    String message = shouldHaveFileSystem(actual, fileSystem).create();
    // THEN
    then(message).isEqualTo(format("%nExpecting path:%n" +
                                   "  %sfoo%sbar%n" +
                                   "to have file system:%n" +
                                   "  MySpecialFileSystem",
                                   File.separator, File.separator));
  }

  @Test
  void should_create_error_message_for_expected_file_system_with_test_description() {
    // GIVEN
    Path actual = Paths.get("/foo/bar");
    FileSystem fileSystem = mock(FileSystem.class);
    given(fileSystem.toString()).willReturn("MySpecialFileSystem");
    // WHEN
    String message = shouldHaveFileSystem(actual, fileSystem).create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting path:%n" +
                                   "  %sfoo%sbar%n" +
                                   "to have file system:%n" +
                                   "  MySpecialFileSystem",
                                   File.separator, File.separator));
  }
}
