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
import static org.assertj.core.util.Strings.join;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

import java.io.File;

import org.assertj.core.api.exception.RuntimeIOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for <code>{@link Files#newFile(String)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Files_newFile_Test extends Files_TestCase {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_file_path_belongs_to_directory_that_is_not_empty() {
    thrown.expect(RuntimeIOException.class);
    Files.newFile("root");
  }

  @Test
  public void should_throw_error_if_file_path_belongs_to_an_existing_file() {
    String path = join("root", "dir_1", "file_1_1").with(separator);
    thrown.expect(RuntimeIOException.class);
    Files.newFile(path);
  }

  @Test
  public void should_create_new_file() {
    File f = null;
    try {
      f = Files.newFile("file");
      assertThat(f).isFile();
    } finally {
      if (f != null) f.delete();
    }
  }
}
