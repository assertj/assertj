/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static java.io.File.separator;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Strings.concat;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.test.ExpectedException.none;

import java.io.File;
import java.util.List;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Files#fileNamesIn(String, boolean)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Files_fileNamesIn_Test extends Files_TestCase {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_directory_does_not_exist() {
    String path = concat("root", separator, "not_existing_dir");
    thrown.expectIllegalArgumentException();
    Files.fileNamesIn(path, false);
  }

  @Test
  public void should_throw_error_if_path_does_not_belong_to_a_directory() throws Exception {
    String fileName = "file_1";
    root.addFiles(fileName);
    String path = concat("root", separator, fileName);
    thrown.expectIllegalArgumentException();
    Files.fileNamesIn(path, false);
  }

  @Test
  public void should_return_names_of_files_in_given_directory_but_not_subdirectories() {
    String path = concat("root", separator, "dir_1");
    assertThatContainsFiles(newArrayList("file_1_1", "file_1_2"), Files.fileNamesIn(path, false));
  }

  @Test
  public void should_return_names_of_files_in_given_directory_and_its_subdirectories() {
    String path = concat("root", separator, "dir_1");
    assertThatContainsFiles(newArrayList("file_1_1", "file_1_2", "file_1_1_1"), Files.fileNamesIn(path, true));
  }

  private void assertThatContainsFiles(List<String> expectedFiles, List<String> actualFiles) {
    assertThat(actualFiles).doesNotHaveDuplicates();
    for (String fileName : actualFiles) {
      assertThat(expectedFiles.remove(pathNameFor(fileName))).isTrue();
    }
    assertThat(expectedFiles).isEmpty();
  }

  private String pathNameFor(String fileName) {
    return new File(fileName).getName();
  }
}
