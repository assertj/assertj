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
package org.assertj.core.internal;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Optional;
import java.util.regex.Pattern;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

/**
 * Base class for testing <code>{@link Files}</code>, set up diff and failures attributes (which is why it is in
 * <code>org.assertj.core.internal</code> package.
 *
 * @author Joel Costigliola
 */
public class FilesBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected static Files underTest = new Files();

  protected static NioFilesWrapper nioFilesWrapper = mock(NioFilesWrapper.class);
  protected static Failures failures = spy(underTest.failures);
  protected static Diff diff = mock(Diff.class);
  protected static BinaryDiff binaryDiff = mock(BinaryDiff.class);
  protected static File actual = mock(File.class);

  @SuppressWarnings("unchecked")
  protected static Delta<String> delta = mock(Delta.class);

  protected Files unMockedFiles;

  @TempDir
  protected File tempDir;

  @BeforeAll
  static void injectMocks() {
    underTest.nioFilesWrapper = nioFilesWrapper;
    underTest.failures = failures;
    underTest.diff = diff;
    underTest.binaryDiff = binaryDiff;
  }

  @BeforeEach
  void resetMocks() {
    reset(nioFilesWrapper, failures, diff, binaryDiff, delta, actual);
  }

  @BeforeEach
  public void setUp() {
    unMockedFiles = new Files();
    unMockedFiles.failures = failures;
    when(delta.toString()).thenReturn("Extra lines at line 2 : [line1a, line1b]");
  }

  protected static void mockPathMatcher(File actual) {
    FileSystem fileSystem = mock(FileSystem.class);
    given(fileSystem.getPathMatcher(anyString())).will(invocation -> {
      String regex = invocation.getArgument(0).toString().split(":")[1];
      Pattern pattern = Pattern.compile("^" + regex + "$", CASE_INSENSITIVE);
      return (PathMatcher) path -> Optional.ofNullable(path.getFileName())
                                           .map(Path::toString)
                                           .filter(pattern.asPredicate())
                                           .isPresent();
    });
    Path path = actual.toPath();
    if (path == null) {
      path = mock(Path.class);
      given(actual.toPath()).willReturn(path);
      given(path.toFile()).willReturn(actual);
    }
    given(path.getFileSystem()).willReturn(fileSystem);
  }

}
