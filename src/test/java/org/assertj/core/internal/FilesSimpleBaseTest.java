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
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * New base class for testing <code>{@link Files}</code>.
 * <p>That is a lighter alternative to {@link FilesBaseTest}.<br>
 * Contrary to {@link FilesBaseTest}, {@link FilesSimpleBaseTest}  doesn't try to abstract and mock the filesystem API.
 * <br>
 *   Please update that javadoc if the philosophy of that simple base test class evolves
 * </p>
 *
 * @author David Haccoun
 */
public abstract class FilesSimpleBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected Path tempDir;
  protected File tempDirAsFile;

  protected Files files;
  protected Failures failures;

  @BeforeEach
  public void setUp(@TempDir Path tempDir) {
    this.tempDir = tempDir;
    tempDirAsFile = tempDir.toFile();
    failures = spy(new Failures());
    files = new Files();
    files.failures = failures;
  }

  public Path createDirectory(Path parent, String name, String... files) {
    Path directory = parent.resolve(name);
    try {
      java.nio.file.Files.createDirectory(directory);
      Arrays.stream(files)
            .forEach(f -> createFile(directory, f));
    } catch (IOException e) {
      throw new UncheckedIOException("error during fixture directory creation", e);
    }

    return directory;
  }

  public Path createDirectoryWithDefaultParent(String name, String... files) {
    return createDirectory(tempDir, name, files);
  }

  private void createFile(Path directory, String f) {
    try {
      java.nio.file.Files.createFile(directory.resolve(f));
    } catch (IOException e) {
      throw new UncheckedIOException("error during fixture file creation", e);
    }
  }

}
