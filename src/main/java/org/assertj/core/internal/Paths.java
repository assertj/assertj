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
import static org.assertj.core.error.ShouldBeAbsolutePath.shouldBeAbsolutePath;
import static org.assertj.core.error.ShouldBeCanonicalPath.shouldBeCanonicalPath;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldBeExecutable.shouldBeExecutable;
import static org.assertj.core.error.ShouldBeNormalized.shouldBeNormalized;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldBeRelativePath.shouldBeRelativePath;
import static org.assertj.core.error.ShouldBeSymbolicLink.shouldBeSymbolicLink;
import static org.assertj.core.error.ShouldBeWritable.shouldBeWritable;
import static org.assertj.core.error.ShouldEndWithPath.shouldEndWith;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldExist.shouldExistNoFollowLinks;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.error.ShouldHaveContent.shouldHaveContent;
import static org.assertj.core.error.ShouldHaveName.shouldHaveName;
import static org.assertj.core.error.ShouldHaveNoParent.shouldHaveNoParent;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.error.ShouldNotExist.shouldNotExist;
import static org.assertj.core.error.ShouldStartWithPath.shouldStartWith;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.exception.PathsException;
import org.assertj.core.api.exception.RuntimeIOException;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;

/**
 * Core assertion class for {@link Path} assertions
 */
public class Paths {

  private static final String FAILED_TO_RESOLVE_ARGUMENT_REAL_PATH = "failed to resolve argument real path";
  private static final String FAILED_TO_RESOLVE_ACTUAL_REAL_PATH = "failed to resolve actual real path";
  @VisibleForTesting
  public static final String IOERROR_FORMAT = "I/O error attempting to process assertion for path: <%s>";

  private static final Paths INSTANCE = new Paths();

  @VisibleForTesting
  Diff diff = new Diff();
  @VisibleForTesting
  BinaryDiff binaryDiff = new BinaryDiff();
  @VisibleForTesting
  Failures failures = Failures.instance();

  private NioFilesWrapper nioFilesWrapper;

  public static Paths instance() {
	return INSTANCE;
  }

  @VisibleForTesting
  Paths(NioFilesWrapper nioFilesWrapper) {
	this.nioFilesWrapper = nioFilesWrapper;
  }

  private Paths() {
	this(NioFilesWrapper.instance());
  }

  public void assertIsReadable(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	assertExists(info, actual);
	if (!nioFilesWrapper.isReadable(actual)) throw failures.failure(info, shouldBeReadable(actual));
  }

  public void assertIsWritable(AssertionInfo info, Path actual) {
	assertNotNull(info, actual);
	assertExists(info, actual);
	if (!nioFilesWrapper.isWritable(actual)) throw failures.failure(info, shouldBeWritable(actual));
  }

  public void assertIsExecutable(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	assertExists(info, actual);
	if (!nioFilesWrapper.isExecutable(actual)) throw failures.failure(info, shouldBeExecutable(actual));
  }

  public void assertExists(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	if (!nioFilesWrapper.exists(actual)) throw failures.failure(info, shouldExist(actual));
  }

  public void assertExistsNoFollowLinks(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	if (!nioFilesWrapper.exists(actual, LinkOption.NOFOLLOW_LINKS))
	  throw failures.failure(info, shouldExistNoFollowLinks(actual));
  }

  public void assertDoesNotExist(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	if (!nioFilesWrapper.notExists(actual, LinkOption.NOFOLLOW_LINKS))
	  throw failures.failure(info, shouldNotExist(actual));
  }

  public void assertIsRegularFile(final AssertionInfo info, final Path actual) {
	assertExists(info, actual);
	if (!nioFilesWrapper.isRegularFile(actual)) throw failures.failure(info, shouldBeRegularFile(actual));
  }

  public void assertIsDirectory(final AssertionInfo info, final Path actual) {
	assertExists(info, actual);
	if (!nioFilesWrapper.isDirectory(actual)) throw failures.failure(info, shouldBeDirectory(actual));
  }

  public void assertIsSymbolicLink(final AssertionInfo info, final Path actual) {
	assertExistsNoFollowLinks(info, actual);
	if (!nioFilesWrapper.isSymbolicLink(actual)) throw failures.failure(info, shouldBeSymbolicLink(actual));
  }

  public void assertIsAbsolute(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	if (!actual.isAbsolute()) throw failures.failure(info, shouldBeAbsolutePath(actual));
  }

  public void assertIsRelative(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	if (actual.isAbsolute()) throw failures.failure(info, shouldBeRelativePath(actual));
  }

  public void assertIsNormalized(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	if (!actual.normalize().equals(actual)) throw failures.failure(info, shouldBeNormalized(actual));
  }

  public void assertIsCanonical(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	try {
	  if (!actual.equals(actual.toRealPath())) throw failures.failure(info, shouldBeCanonicalPath(actual));
	} catch (IOException e) {
	  throw new PathsException(FAILED_TO_RESOLVE_ACTUAL_REAL_PATH, e);
	}
  }

  public void assertHasParent(final AssertionInfo info, final Path actual, final Path expected) {
	assertNotNull(info, actual);
	checkExpectedParentPathIsNotNull(expected);

	final Path canonicalActual;
	try {
	  canonicalActual = actual.toRealPath();
	} catch (IOException e) {
	  throw new PathsException(FAILED_TO_RESOLVE_ACTUAL_REAL_PATH, e);
	}

	final Path canonicalExpected;
	try {
	  canonicalExpected = expected.toRealPath();
	} catch (IOException e) {
	  throw new PathsException(FAILED_TO_RESOLVE_ARGUMENT_REAL_PATH, e);
	}

	final Path actualParent = canonicalActual.getParent();
	if (actualParent == null) throw failures.failure(info, shouldHaveParent(actual, expected));
	if (!actualParent.equals(canonicalExpected))
	  throw failures.failure(info, shouldHaveParent(actual, actualParent, expected));
  }

  public void assertHasParentRaw(final AssertionInfo info, final Path actual, final Path expected) {
	assertNotNull(info, actual);
	checkExpectedParentPathIsNotNull(expected);

	final Path actualParent = actual.getParent();
	if (actualParent == null) throw failures.failure(info, shouldHaveParent(actual, expected));
	if (!actualParent.equals(expected))
	  throw failures.failure(info, shouldHaveParent(actual, actualParent, expected));
  }

  public void assertHasNoParent(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	try {
	  final Path canonicalActual = actual.toRealPath();
	  if (canonicalActual.getParent() != null) throw failures.failure(info, shouldHaveNoParent(actual));
	} catch (IOException e) {
	  throw new PathsException(FAILED_TO_RESOLVE_ACTUAL_REAL_PATH, e);
	}
  }

  public void assertHasNoParentRaw(final AssertionInfo info, final Path actual) {
	assertNotNull(info, actual);
	if (actual.getParent() != null) throw failures.failure(info, shouldHaveNoParent(actual));
  }

  public void assertStartsWith(final AssertionInfo info, final Path actual, final Path start) {
	assertNotNull(info, actual);
	assertExpectedStartPathIsNotNull(start);

	final Path canonicalActual;
	try {
	  canonicalActual = actual.toRealPath();
	} catch (IOException e) {
	  throw new PathsException(FAILED_TO_RESOLVE_ACTUAL_REAL_PATH, e);
	}

	final Path canonicalOther;
	try {
	  canonicalOther = start.toRealPath();
	} catch (IOException e) {
	  throw new PathsException(FAILED_TO_RESOLVE_ARGUMENT_REAL_PATH, e);
	}

	if (!canonicalActual.startsWith(canonicalOther)) throw failures.failure(info, shouldStartWith(actual, start));
  }

  public void assertStartsWithRaw(final AssertionInfo info, final Path actual, final Path other) {
	assertNotNull(info, actual);
	assertExpectedStartPathIsNotNull(other);
	if (!actual.startsWith(other)) throw failures.failure(info, shouldStartWith(actual, other));
  }

  public void assertEndsWith(final AssertionInfo info, final Path actual, final Path end) {
	assertNotNull(info, actual);
	assertExpectedEndPathIsNotNull(end);
	try {
	  final Path canonicalActual = actual.toRealPath();
	  if (!canonicalActual.endsWith(end.normalize())) throw failures.failure(info, shouldEndWith(actual, end));
	} catch (IOException e) {
	  throw new PathsException(FAILED_TO_RESOLVE_ACTUAL_REAL_PATH, e);
	}
  }

  public void assertEndsWithRaw(final AssertionInfo info, final Path actual, final Path end) {
	assertNotNull(info, actual);
	assertExpectedEndPathIsNotNull(end);
	if (!actual.endsWith(end)) throw failures.failure(info, shouldEndWith(actual, end));
  }

  public void assertHasFileName(final AssertionInfo info, Path actual, String fileName) {
	assertNotNull(info, actual);
	checkNotNull(fileName, "expected fileName should not be null");
	if (!actual.getFileName().endsWith(fileName)) throw failures.failure(info, shouldHaveName(actual, fileName));
  }

  private static void assertNotNull(final AssertionInfo info, final Path actual) {
	Objects.instance().assertNotNull(info, actual);
  }

  private static void checkExpectedParentPathIsNotNull(final Path expected) {
    checkNotNull(expected, "expected parent path should not be null");
  }

  private static void assertExpectedStartPathIsNotNull(final Path start) {
    checkNotNull(start, "the expected start path should not be null");
  }

  private static void assertExpectedEndPathIsNotNull(final Path end) {
    checkNotNull(end, "the expected end path should not be null");
  }

  public void assertHasContent(final AssertionInfo info, Path actual, String expected, Charset charset) {
    checkNotNull(expected, "The text to compare to should not be null");
	assertIsReadable(info, actual);
	try {
	  List<Delta<String>> diffs = diff.diff(actual, expected, charset);
	  if (diffs.isEmpty()) return;
	  throw failures.failure(info, shouldHaveContent(actual, charset, diffs));
	} catch (IOException e) {
	  throw new RuntimeIOException(format("Unable to verify text contents of path:<%s>", actual), e);
	}
  }

  public void assertHasBinaryContent(AssertionInfo info, Path actual, byte[] expected) {
    checkNotNull(expected, "The binary content to compare to should not be null");
	assertIsReadable(info, actual);
	try {
	  BinaryDiffResult diffResult = binaryDiff.diff(actual, expected);
	  if (diffResult.hasNoDiff()) return;
	  throw failures.failure(info, shouldHaveBinaryContent(actual, diffResult));
	} catch (IOException e) {
	  throw new RuntimeIOException(format("Unable to verify binary contents of path:<%s>", actual), e);
	}
  }

  public void assertHasSameContentAs(AssertionInfo info, Path actual, Charset actualCharset, Path expected, Charset expectedCharset) {
    checkNotNull(expected, "The given Path to compare actual content to should not be null");
    checkArgument(nioFilesWrapper.isReadable(expected), "The given Path <%s> to compare actual content to should be readable", expected);
	assertIsReadable(info, actual);
	try {
	  List<Delta<String>> diffs = diff.diff(actual, actualCharset, expected, expectedCharset);
	  if (diffs.isEmpty()) return;
	  throw failures.failure(info, shouldHaveSameContent(actual, expected, diffs));
	} catch (IOException e) {
	  throw new RuntimeIOException(format("Unable to compare contents of paths:<%s> and:<%s>", actual, expected), e);
	}
  }

}
