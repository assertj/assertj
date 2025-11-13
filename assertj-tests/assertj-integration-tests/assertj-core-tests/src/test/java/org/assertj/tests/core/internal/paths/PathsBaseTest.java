/*
 * Copyright 2012-2025 the original author or authors.
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

import static java.nio.file.Files.createSymbolicLink;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.assertj.tests.core.testkit.FieldTestUtils.readField;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import java.nio.file.Path;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.Diff;
import org.assertj.core.internal.NioFilesWrapper;
import org.assertj.core.internal.Paths;
import org.assertj.tests.core.testkit.TestData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.commons.function.Try;
import org.opentest4j.TestAbortedException;

public abstract class PathsBaseTest {

  protected static final AssertionInfo INFO = TestData.someInfo();

  protected static Paths underTest = Paths.instance();
  protected static NioFilesWrapper nioFilesWrapper = spy((NioFilesWrapper) readField(underTest, "nioFilesWrapper"));
  protected static Diff diff = spy((Diff) readField(underTest, "diff"));
  protected static BinaryDiff binaryDiff = spy((BinaryDiff) readField(underTest, "binaryDiff"));

  @TempDir
  protected Path tempDir;

  @BeforeAll
  static void injectSpies() throws IllegalAccessException {
    writeField(underTest, "nioFilesWrapper", nioFilesWrapper, true);
    writeField(underTest, "diff", diff, true);
    writeField(underTest, "binaryDiff", binaryDiff, true);
  }

  @AfterAll
  static void removeSpies() throws IllegalAccessException {
    writeField(underTest, "nioFilesWrapper", getSpiedInstance(nioFilesWrapper), true);
    writeField(underTest, "diff", getSpiedInstance(diff), true);
    writeField(underTest, "binaryDiff", getSpiedInstance(binaryDiff), true);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getSpiedInstance(Object spy) {
    return (T) mockingDetails(spy).getMockCreationSettings().getSpiedInstance();
  }

  @BeforeEach
  void resetSpies() {
    reset(nioFilesWrapper, diff, binaryDiff);
  }

  // https://github.com/assertj/assertj/issues/3183
  protected static Path tryToCreateSymbolicLink(Path link, Path target) {
    return Try.call(() -> createSymbolicLink(link, target))
              .getOrThrow(e -> new TestAbortedException("Failed to create symbolic link", e));
  }

}
