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
package org.assertj.core.internal;

import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.error.ShouldBeAbsolutePath.shouldBeAbsolutePath;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldBeRelativePath.shouldBeRelativePath;
import static org.assertj.core.error.ShouldBeWritable.shouldBeWritable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.error.ShouldHaveContent.shouldHaveContent;
import static org.assertj.core.error.ShouldHaveExtension.shouldHaveExtension;
import static org.assertj.core.error.ShouldHaveName.shouldHaveName;
import static org.assertj.core.error.ShouldHaveNoParent.shouldHaveNoParent;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.error.ShouldNotExist.shouldNotExist;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.exception.RuntimeIOException;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;

/**
 * Reusable assertions for <code>{@link File}</code>s.
 * 
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Olivier Demeijer
 */
public class Files {

  private static final String UNABLE_TO_COMPARE_FILE_CONTENTS = "Unable to compare contents of files:<%s> and:<%s>";
  private static final Files INSTANCE = new Files();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Files instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Diff diff = new Diff();
  @VisibleForTesting
  BinaryDiff binaryDiff = new BinaryDiff();
  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Files() {}

  /**
   * Asserts that the given files have same content. Adapted from <a
   * href="http://junit-addons.sourceforge.net/junitx/framework/FileAssert.html" target="_blank">FileAssert</a> (from <a
   * href="http://sourceforge.net/projects/junit-addons">JUnit-addons</a>.)
   * @param info contains information about the assertion.
   * @param actual the "actual" file.
   * @param actualCharset {@link Charset} of the "actual" file.
   * @param expected the "expected" file.
   * @param expectedCharset {@link Charset} of the "actual" file.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws IllegalArgumentException if {@code expected} is not an existing file.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if {@code actual} is not an existing file.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the given files do not have same content.
   */
  public void assertSameContentAs(AssertionInfo info, File actual, Charset actualCharset, File expected,
                                  Charset expectedCharset) {
    verifyIsFile(expected);
    assertIsFile(info, actual);
    try {
      List<Delta<String>> diffs = diff.diff(actual, actualCharset, expected, expectedCharset);
      if (diffs.isEmpty()) return;
      throw failures.failure(info, shouldHaveSameContent(actual, expected, diffs));
    } catch (MalformedInputException e) {
      try {
        // MalformedInputException is thrown by readLine() called in diff
        // compute a binary diff, if there is a binary diff, it it shows the offset of the malformed input
        BinaryDiffResult binaryDiffResult = binaryDiff.diff(actual, readAllBytes(expected.toPath()));
        if (binaryDiffResult.hasNoDiff()) {
          // fall back to the RuntimeIOException : not throwing an error is wrong as there was one in the first place.
          throw e;
        }
        throw failures.failure(info, shouldHaveBinaryContent(actual, binaryDiffResult));
      } catch (IOException ioe) {
        throw new RuntimeIOException(format(UNABLE_TO_COMPARE_FILE_CONTENTS, actual, expected), ioe);
      }
    } catch (IOException e) {
      throw new RuntimeIOException(format(UNABLE_TO_COMPARE_FILE_CONTENTS, actual, expected), e);
    }
  }

  /**
   * Asserts that the given file has the given binary content.
   * @param info contains information about the assertion.
   * @param actual the "actual" file.
   * @param expected the "expected" binary content.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if {@code actual} is not an existing file.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the file does not have the binary content.
   */
  public void assertHasBinaryContent(AssertionInfo info, File actual, byte[] expected) {
    checkNotNull(expected, "The binary content to compare to should not be null");
    assertIsFile(info, actual);
    try {
      BinaryDiffResult result = binaryDiff.diff(actual, expected);
      if (result.hasNoDiff()) return;
      throw failures.failure(info, shouldHaveBinaryContent(actual, result));
    } catch (IOException e) {
      String msg = String.format("Unable to verify binary contents of file:<%s>", actual);
      throw new RuntimeIOException(msg, e);
    }
  }

  /**
   * Asserts that the given file has the given text content.
   * @param info contains information about the assertion.
   * @param actual the "actual" file.
   * @param expected the "expected" text content.
   * @param charset the charset to use to read the file.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if {@code actual} is not an existing file.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the file does not have the text content.
   */
  public void assertHasContent(AssertionInfo info, File actual, String expected, Charset charset) {
    checkNotNull(expected, "The text to compare to should not be null");
    assertIsFile(info, actual);
    try {
      List<Delta<String>> diffs = diff.diff(actual, expected, charset);
      if (diffs.isEmpty()) return;
      throw failures.failure(info, shouldHaveContent(actual, charset, diffs));
    } catch (IOException e) {
      String msg = String.format("Unable to verify text contents of file:<%s>", actual);
      throw new RuntimeIOException(msg, e);
    }
  }

  private void verifyIsFile(File expected) {
    checkNotNull(expected, "The file to compare to should not be null");
    checkArgument(expected.isFile(), "Expected file:<'%s'> should be an existing file", expected);
  }

  /**
   * Asserts that the given file is an existing file.
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @throws AssertionError if the given file is {@code null}.
   * @throws AssertionError if the given file is not an existing file.
   */
  public void assertIsFile(AssertionInfo info, File actual) {
    assertNotNull(info, actual);
    if (actual.isFile()) return;
    throw failures.failure(info, shouldBeFile(actual));
  }

  /**
   * Asserts that the given file is an existing directory.
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @throws AssertionError if the given file is {@code null}.
   * @throws AssertionError if the given file is not an existing directory.
   */
  public void assertIsDirectory(AssertionInfo info, File actual) {
    assertNotNull(info, actual);
    if (actual.isDirectory()) return;
    throw failures.failure(info, shouldBeDirectory(actual));
  }

  /**
   * Asserts that the given file is an absolute path.
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @throws AssertionError if the given file is {@code null}.
   * @throws AssertionError if the given file is not an absolute path.
   */
  public void assertIsAbsolute(AssertionInfo info, File actual) {
    if (isAbsolutePath(info, actual)) return;
    throw failures.failure(info, shouldBeAbsolutePath(actual));
  }

  /**
   * Asserts that the given file is a relative path.
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @throws AssertionError if the given file is {@code null}.
   * @throws AssertionError if the given file is not a relative path.
   */
  public void assertIsRelative(AssertionInfo info, File actual) {
    if (!isAbsolutePath(info, actual)) return;
    throw failures.failure(info, shouldBeRelativePath(actual));
  }

  private boolean isAbsolutePath(AssertionInfo info, File actual) {
    assertNotNull(info, actual);
    return actual.isAbsolute();
  }

  /**
   * Asserts that the given file exists, regardless it's a file or directory.
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @throws AssertionError if the given file is {@code null}.
   * @throws AssertionError if the given file does not exist.
   */
  public void assertExists(AssertionInfo info, File actual) {
    assertNotNull(info, actual);
    if (actual.exists()) return;
    throw failures.failure(info, shouldExist(actual));
  }

  /**
   * Asserts that the given file does not exist.
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @throws AssertionError if the given file is {@code null}.
   * @throws AssertionError if the given file exists.
   */
  public void assertDoesNotExist(AssertionInfo info, File actual) {
    assertNotNull(info, actual);
    if (!actual.exists()) return;
    throw failures.failure(info, shouldNotExist(actual));
  }

  /**
  * Asserts that the given file can be modified by the application.
  * @param info contains information about the assertion.
  * @param actual the given file.
  * @throws AssertionError if the given file is {@code null}.
  * @throws AssertionError if the given file can not be modified.
  */
  public void assertCanWrite(AssertionInfo info, File actual) {
    assertNotNull(info, actual);
    if (actual.canWrite()) return;
    throw failures.failure(info, shouldBeWritable(actual));
  }

  /**
   * Asserts that the given file can be read by the application.
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @throws AssertionError if the given file is {@code null}.
   * @throws AssertionError if the given file can not be modified.
   */
  public void assertCanRead(AssertionInfo info, File actual) {
    assertNotNull(info, actual);
    if (actual.canRead()) return;
    throw failures.failure(info, shouldBeReadable(actual));
  }

  /**
   * Asserts that the given {@code File} has the given parent.
   * 
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @param expected the expected parent {@code File}.
   * @throws NullPointerException if the expected parent {@code File} is {@code null}.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the given {@code File} is {@code null}.
   * @throws AssertionError if the given {@code File} does not have a parent.
   * @throws AssertionError if the given {@code File} parent is not equal to the expected one.
   */
  public void assertHasParent(AssertionInfo info, File actual, File expected) {
    checkNotNull(expected, "The expected parent file should not be null.");
    assertNotNull(info, actual);
    try {
      if (actual.getParentFile() != null
          && areEqual(expected.getCanonicalFile(), actual.getParentFile().getCanonicalFile()))
        return;
    } catch (IOException e) {
      throw new RuntimeIOException(String.format("Unable to get canonical form of [%s] or [%s].", actual, expected), e);
    }
    throw failures.failure(info, shouldHaveParent(actual, expected));
  }

  /**
   * Asserts that the given {@code File} has the given extension.
   * 
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @param expected the expected extension, it does not contains the {@code '.'}
   * @throws NullPointerException if the expected extension is {@code null}.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not a file (ie a directory).
   * @throws AssertionError if the actual {@code File} does not have the expected extension.
   */
  public void assertHasExtension(AssertionInfo info, File actual, String expected) {
    checkNotNull(expected, "The expected extension should not be null.");
    assertIsFile(info, actual);
    String actualExtension = getFileExtension(actual);
    if (expected.equals(actualExtension)) return;
    throw failures.failure(info, shouldHaveExtension(actual, actualExtension, expected));
  }

  private String getFileExtension(File file) {
    String name = file.getName();
    int dotAt = name.lastIndexOf('.');
    return (dotAt == -1) ? null : name.substring(dotAt + 1);
  }

  /**
   * Asserts that the given {@code File} has the given name.
   * 
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @param expected the expected file name.
   * @throws NullPointerException if the expected name is {@code null}.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} does not have the expected name.
   */
  public void assertHasName(AssertionInfo info, File actual, String expected) {
    assertNotNull(info, actual);
    checkNotNull(expected, "The expected name should not be null.");
    if (expected.equals(actual.getName())) return;
    throw failures.failure(info, shouldHaveName(actual, expected));
  }

  private static void assertNotNull(AssertionInfo info, File actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  /**
   * Asserts that the given {@code File} does not have a parent.
   * 
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} has a parent.
   */
  public void assertHasNoParent(AssertionInfo info, File actual) {
    assertNotNull(info, actual);
    if (actual.getParentFile() == null) return;
    throw failures.failure(info, shouldHaveNoParent(actual));
  }
}
