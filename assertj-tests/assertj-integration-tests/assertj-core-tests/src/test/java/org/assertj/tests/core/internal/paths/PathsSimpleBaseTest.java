/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.internal.paths;

import static java.util.Arrays.stream;
import static org.assertj.tests.core.testkit.FieldTestUtils.writeField;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author David Haccoun
 */
public abstract class PathsSimpleBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected Path tempDir;

  protected Paths paths;
  protected Failures failures;

  @BeforeEach
  public void setUp(@TempDir Path tempDir) {
    this.tempDir = tempDir;
    failures = spy(Failures.instance());
    paths = Paths.instance();
    writeField(paths, "failures", failures);
  }

  Path createDirectory(Path parent, String name, String... files) {
    Path directory = parent.resolve(name);
    try {
      java.nio.file.Files.createDirectory(directory);
      stream(files).forEach(f -> createFile(directory, f));
    } catch (IOException e) {
      throw new UncheckedIOException("error during fixture directory creation", e);
    }
    return directory;
  }

  Path createDirectoryFromRoot(String... files) {
    return createDirectory(tempDir, "root", files);
  }

  private void createFile(Path directory, String f) {
    try {
      java.nio.file.Files.createFile(directory.resolve(f));
    } catch (IOException e) {
      throw new UncheckedIOException("error during fixture file creation", e);
    }
  }

}
