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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEmptyDirectory.shouldBeEmptyDirectory;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

public class ShouldBeEmptyDirectory_create_Test {

  private static final TestDescription DESCRIPTION = new TestDescription("Test");

  @Test
  public void should_create_error_message_for_Path() {
    // GIVEN
    final Path directory = mockPath("/root");
    final Path file1 = mockPath("/bin/file1");
    final Path file2 = mockPath("/bin/file2");
    List<Path> directoryContent = list(file1, file2);
    // WHEN
    String message = shouldBeEmptyDirectory(directory, directoryContent).create(DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo("[Test] %nExpecting:%n" +
                                  "  </root>%n" +
                                  "to be an empty directory but it contained:%n" +
                                  "  [/bin/file1, /bin/file2]",
                                  directory, directoryContent);
  }

  @Test
  public void should_create_error_message_for_File() {
    final File directory = new FakeFile("root");
    final File file1 = new FakeFile("file1");
    final File file2 = new FakeFile("file1");
    List<File> directoryContent = list(file1, file2);
    // WHEN
    String message = shouldBeEmptyDirectory(directory, directoryContent).create(DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo("[Test] %nExpecting:%n" +
                                  "  <%s>%n" +
                                  "to be an empty directory but it contained:%n" +
                                  "  %s",
                                  directory, directoryContent);
  }

  private static Path mockPath(String pathToString) {
    final Path directory = mock(Path.class);
    given(directory.toString()).willReturn(pathToString);
    return directory;
  }

}
