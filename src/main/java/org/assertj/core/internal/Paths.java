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

import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.error.ShouldBeAbsolutePath.shouldBeAbsolutePath;
import static org.assertj.core.error.ShouldBeCanonicalPath.shouldBeCanonicalPath;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldBeEmptyDirectory.shouldBeEmptyDirectory;
import static org.assertj.core.error.ShouldBeExecutable.shouldBeExecutable;
import static org.assertj.core.error.ShouldBeNormalized.shouldBeNormalized;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldBeRelativePath.shouldBeRelativePath;
import static org.assertj.core.error.ShouldBeSymbolicLink.shouldBeSymbolicLink;
import static org.assertj.core.error.ShouldBeWritable.shouldBeWritable;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.error.ShouldEndWithPath.shouldEndWith;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldExist.shouldExistNoFollowLinks;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.error.ShouldHaveContent.shouldHaveContent;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.error.ShouldHaveName.shouldHaveName;
import static org.assertj.core.error.ShouldHaveNoParent.shouldHaveNoParent;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.directoryShouldNotContain;
import static org.assertj.core.error.ShouldNotExist.shouldNotExist;
import static org.assertj.core.error.ShouldStartWithPath.shouldStartWith;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.exception.PathsException;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;

/**
 * Core assertion class for {@link Path} assertions
 *
 * @author Valeriy Vyrva
 */
public class Paths {

  private static final String FAILED_TO_RESOLVE_ARGUMENT_REAL_PATH = "failed to resolve argument real path";
  private static final String FAILED_TO_RESOLVE_ACTUAL_REAL_PATH = "failed to resolve actual real path";
  private static final String UNABLE_TO_COMPARE_PATH_CONTENTS = "Unable to compare contents of paths:<%s> and:<%s>";

  public static final String IOERROR_FORMAT = "I/O error attempting to process assertion for path: <%s>";

  private static final Paths INSTANCE = new Paths();
  private static final Predicate<Path> ANY = any -> true;

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
    requireNonNull(fileName, "expected fileName should not be null");
    if (!actual.getFileName().endsWith(fileName)) throw failures.failure(info, shouldHaveName(actual, fileName));
  }

  public void assertHasContent(final AssertionInfo info, Path actual, String expected, Charset charset) {
    requireNonNull(expected, "The text to compare to should not be null");
    assertIsReadable(info, actual);
    try {
      List<Delta<String>> diffs = diff.diff(actual, expected, charset);
      if (diffs.isEmpty()) return;
      throw failures.failure(info, shouldHaveContent(actual, charset, diffs));
    } catch (IOException e) {
      throw new UncheckedIOException(format("Unable to verify text contents of path:<%s>", actual), e);
    }
  }

  public void assertHasBinaryContent(AssertionInfo info, Path actual, byte[] expected) {
    requireNonNull(expected, "The binary content to compare to should not be null");
    assertIsReadable(info, actual);
    try {
      BinaryDiffResult diffResult = binaryDiff.diff(actual, expected);
      if (diffResult.hasNoDiff()) return;
      throw failures.failure(info, shouldHaveBinaryContent(actual, diffResult));
    } catch (IOException e) {
      throw new UncheckedIOException(format("Unable to verify binary contents of path:<%s>", actual), e);
    }
  }

  public void assertHasSameBinaryContentAs(AssertionInfo info, Path actual, Path expected) {
    requireNonNull(expected, "The given Path to compare actual content to should not be null");
    assertIsReadable(info, actual);
    checkArgument(nioFilesWrapper.exists(expected), "The given Path <%s> to compare actual content to should exist", expected);
    checkArgument(nioFilesWrapper.isReadable(expected), "The given Path <%s> to compare actual content to should be readable",
                  expected);
    try {
      BinaryDiffResult binaryDiffResult = binaryDiff.diff(actual, readAllBytes(expected));
      if (binaryDiffResult.hasDiff()) throw failures.failure(info, shouldHaveBinaryContent(actual, binaryDiffResult));
    } catch (IOException ioe) {
      throw new UncheckedIOException(format(UNABLE_TO_COMPARE_PATH_CONTENTS, actual, expected), ioe);
    }
  }

  public void assertHasSameContentAs(AssertionInfo info, Path actual, Charset actualCharset, Path expected,
                                     Charset expectedCharset) {
    requireNonNull(expected, "The given Path to compare actual content to should not be null");
    checkArgument(nioFilesWrapper.exists(expected), "The given Path <%s> to compare actual content to should exist", expected);
    checkArgument(nioFilesWrapper.isReadable(expected), "The given Path <%s> to compare actual content to should be readable",
                  expected);
    assertIsReadable(info, actual);
    try {
      List<Delta<String>> diffs = diff.diff(actual, actualCharset, expected, expectedCharset);
      if (diffs.isEmpty()) return;
      throw failures.failure(info, shouldHaveSameContent(actual, expected, diffs));
    } catch (IOException e) {
      throw new UncheckedIOException(format(UNABLE_TO_COMPARE_PATH_CONTENTS, actual, expected), e);
    }
  }

  public void assertHasDigest(AssertionInfo info, Path actual, MessageDigest digest, byte[] expected) {
    requireNonNull(digest, "The message digest algorithm should not be null");
    requireNonNull(expected, "The binary representation of digest to compare to should not be null");
    assertIsRegularFile(info, actual);
    assertIsReadable(info, actual);
    try (InputStream actualStream = nioFilesWrapper.newInputStream(actual)) {
      DigestDiff diff = Digests.digestDiff(actualStream, digest, expected);
      if (diff.digestsDiffer()) throw failures.failure(info, shouldHaveDigest(actual, diff));
    } catch (IOException e) {
      throw new UncheckedIOException(format("Unable to calculate digest of path:<%s>", actual), e);
    }
  }

  public void assertHasDigest(AssertionInfo info, Path actual, MessageDigest digest, String expected) {
    requireNonNull(expected, "The string representation of digest to compare to should not be null");
    assertHasDigest(info, actual, digest, Digests.fromHex(expected));
  }

  public void assertHasDigest(AssertionInfo info, Path actual, String algorithm, byte[] expected) {
    requireNonNull(algorithm, "The message digest algorithm should not be null");
    try {
      assertHasDigest(info, actual, MessageDigest.getInstance(algorithm), expected);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(format("Unable to find digest implementation for: <%s>", algorithm), e);
    }
  }

  public void assertHasDigest(AssertionInfo info, Path actual, String algorithm, String expected) {
    requireNonNull(expected, "The string representation of digest to compare to should not be null");
    assertHasDigest(info, actual, algorithm, Digests.fromHex(expected));
  }

  public void assertIsDirectoryContaining(AssertionInfo info, Path actual, Predicate<Path> filter) {
    requireNonNull(filter, "The paths filter should not be null");
    assertIsDirectoryContaining(info, actual, filter, "the given filter");
  }

  public void assertIsDirectoryContaining(AssertionInfo info, Path actual, String syntaxAndPattern) {
    requireNonNull(syntaxAndPattern, "The syntax and pattern should not be null");
    PathMatcher pathMatcher = pathMatcher(info, actual, syntaxAndPattern);
    assertIsDirectoryContaining(info, actual, pathMatcher::matches, format("the '%s' pattern", syntaxAndPattern));
  }

  public void assertIsDirectoryNotContaining(AssertionInfo info, Path actual, Predicate<Path> filter) {
    requireNonNull(filter, "The paths filter should not be null");
    assertIsDirectoryNotContaining(info, actual, filter, "the given filter");
  }

  public void assertIsDirectoryNotContaining(AssertionInfo info, Path actual, String syntaxAndPattern) {
    requireNonNull(syntaxAndPattern, "The syntax and pattern should not be null");
    PathMatcher pathMatcher = pathMatcher(info, actual, syntaxAndPattern);
    assertIsDirectoryNotContaining(info, actual, pathMatcher::matches, format("the '%s' pattern", syntaxAndPattern));
  }

  public void assertIsEmptyDirectory(AssertionInfo info, Path actual) {
    List<Path> items = directoryContent(info, actual);
    if (!items.isEmpty()) throw failures.failure(info, shouldBeEmptyDirectory(actual, items));
  }

  public void assertIsNotEmptyDirectory(AssertionInfo info, Path actual) {
    boolean isEmptyDirectory = directoryContent(info, actual).isEmpty();
    if (isEmptyDirectory) throw failures.failure(info, shouldNotBeEmpty());
  }

  public static List<String> toFileNames(List<Path> files) {
    return files.stream()
                .map(Path::toString)
                .collect(toList());
  }

  // non public section

  private List<Path> filterDirectory(AssertionInfo info, Path actual, Predicate<Path> filter) {
    assertIsDirectory(info, actual);
    try (DirectoryStream<Path> stream = nioFilesWrapper.newDirectoryStream(actual, filter)) {
      return stream(stream.spliterator(), false).collect(toList());
    } catch (IOException e) {
      throw new UncheckedIOException(format("Unable to list directory content: <%s>", actual), e);
    }
  }

  private List<Path> directoryContent(AssertionInfo info, Path actual) {
    return filterDirectory(info, actual, ANY);
  }

  private void assertIsDirectoryContaining(AssertionInfo info, Path actual, Predicate<Path> filter, String filterPresentation) {
    List<Path> matchingFiles = filterDirectory(info, actual, filter);
    if (matchingFiles.isEmpty()) {
      throw failures.failure(info, directoryShouldContain(actual, directoryContentDescription(info, actual), filterPresentation));
    }
  }

  private void assertIsDirectoryNotContaining(AssertionInfo info, Path actual, Predicate<Path> filter,
                                              String filterPresentation) {
    List<Path> matchingFiles = filterDirectory(info, actual, filter);
    if (matchingFiles.size() > 0) {
      throw failures.failure(info, directoryShouldNotContain(actual, toFileNames(matchingFiles), filterPresentation));
    }
  }

  private List<String> directoryContentDescription(AssertionInfo info, Path actual) {
    return toFileNames(directoryContent(info, actual));
  }

  private PathMatcher pathMatcher(AssertionInfo info, Path actual, String syntaxAndPattern) {
    assertNotNull(info, actual);
    return actual.getFileSystem().getPathMatcher(syntaxAndPattern);
  }

  private static void assertNotNull(final AssertionInfo info, final Path actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private static void checkExpectedParentPathIsNotNull(final Path expected) {
    requireNonNull(expected, "expected parent path should not be null");
  }

  private static void assertExpectedStartPathIsNotNull(final Path start) {
    requireNonNull(start, "the expected start path should not be null");
  }

  private static void assertExpectedEndPathIsNotNull(final Path end) {
    requireNonNull(end, "the expected end path should not be null");
  }

}
