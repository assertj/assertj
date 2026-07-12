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
package org.assertj.core.internal;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.walk;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.error.ShouldBeAbsolutePath.shouldBeAbsolutePath;
import static org.assertj.core.error.ShouldBeCanonicalPath.shouldBeCanonicalPath;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeEmptyDirectory.shouldBeEmptyDirectory;
import static org.assertj.core.error.ShouldBeExecutable.shouldBeExecutable;
import static org.assertj.core.error.ShouldBeNormalized.shouldBeNormalized;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldBeRelativePath.shouldBeRelativePath;
import static org.assertj.core.error.ShouldBeSymbolicLink.shouldBeSymbolicLink;
import static org.assertj.core.error.ShouldBeWritable.shouldBeWritable;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.error.ShouldContainRecursively.directoryShouldContainRecursively;
import static org.assertj.core.error.ShouldEndWithPath.shouldEndWith;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldExist.shouldExistNoFollowLinks;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.error.ShouldHaveContent.shouldHaveContent;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.error.ShouldHaveExtension.shouldHaveExtension;
import static org.assertj.core.error.ShouldHaveFileSystem.shouldHaveFileSystem;
import static org.assertj.core.error.ShouldHaveName.shouldHaveName;
import static org.assertj.core.error.ShouldHaveNoExtension.shouldHaveNoExtension;
import static org.assertj.core.error.ShouldHaveNoParent.shouldHaveNoParent;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.error.ShouldHaveSameFileSystemAs.shouldHaveSameFileSystemAs;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.directoryShouldNotContain;
import static org.assertj.core.error.ShouldNotExist.shouldNotExist;
import static org.assertj.core.error.ShouldStartWithPath.shouldStartWith;
import static org.assertj.core.util.Files.getFileNameExtension;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.diff.Delta;

/**
 * Core assertion class for {@link Path} assertions
 *
 * @author Valeriy Vyrva
 */
public class Paths {

  private static final String UNABLE_TO_COMPARE_PATH_CONTENTS = "Unable to compare contents of paths:<%s> and:<%s>";

  private static final Paths INSTANCE = new Paths();
  private static final Filter<Path> ANY = any -> true;

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Diff diff = new Diff();
  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  BinaryDiff binaryDiff = new BinaryDiff();
  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Failures failures = Failures.instance();
  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  NioFilesWrapper nioFilesWrapper = NioFilesWrapper.instance();

  /**
   * Returns the shared path assertions instance.
   *
   * @return the shared instance
   */
  public static Paths instance() {
    return INSTANCE;
  }

  private Paths() {}

  /**
   * Verifies that the path is readable.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsReadable(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    assertExists(info, actual);
    if (!Files.isReadable(actual)) throw failures.failure(info, shouldBeReadable(actual));
  }

  /**
   * Verifies that the path is writable.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsWritable(AssertionInfo info, Path actual) {
    assertNotNull(info, actual);
    assertExists(info, actual);
    if (!Files.isWritable(actual)) throw failures.failure(info, shouldBeWritable(actual));
  }

  /**
   * Verifies that the path is executable.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsExecutable(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    assertExists(info, actual);
    if (!Files.isExecutable(actual)) throw failures.failure(info, shouldBeExecutable(actual));
  }

  /**
   * Verifies that the path exists.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertExists(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    if (!Files.exists(actual)) throw failures.failure(info, shouldExist(actual));
  }

  /**
   * Verifies without following links that the path exists.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertExistsNoFollowLinks(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    if (!Files.exists(actual, LinkOption.NOFOLLOW_LINKS)) throw failures.failure(info, shouldExistNoFollowLinks(actual));
  }

  /**
   * Verifies that the path does not exist.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertDoesNotExist(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    if (!Files.notExists(actual, LinkOption.NOFOLLOW_LINKS)) throw failures.failure(info, shouldNotExist(actual));
  }

  /**
   * Verifies that the path is a regular file.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsRegularFile(final AssertionInfo info, final Path actual) {
    assertExists(info, actual);
    if (!Files.isRegularFile(actual)) throw failures.failure(info, shouldBeRegularFile(actual));
  }

  /**
   * Verifies that the path is a directory.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsDirectory(final AssertionInfo info, final Path actual) {
    assertExists(info, actual);
    if (!Files.isDirectory(actual)) throw failures.failure(info, shouldBeDirectory(actual));
  }

  /**
   * Verifies that the path is a symbolic link.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsSymbolicLink(final AssertionInfo info, final Path actual) {
    assertExistsNoFollowLinks(info, actual);
    if (!Files.isSymbolicLink(actual)) throw failures.failure(info, shouldBeSymbolicLink(actual));
  }

  /**
   * Verifies that the path is absolute.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsAbsolute(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    if (!actual.isAbsolute()) throw failures.failure(info, shouldBeAbsolutePath(actual));
  }

  /**
   * Verifies that the path is relative.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsRelative(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    if (actual.isAbsolute()) throw failures.failure(info, shouldBeRelativePath(actual));
  }

  /**
   * Verifies that the path is normalized.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsNormalized(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    if (!actual.normalize().equals(actual)) throw failures.failure(info, shouldBeNormalized(actual));
  }

  /**
   * Verifies that the path is canonical.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsCanonical(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    if (!actual.equals(toRealPath(actual))) throw failures.failure(info, shouldBeCanonicalPath(actual));
  }

  /**
   * Verifies the normalized parent path.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param expected the expected parent
   */
  public void assertHasParent(final AssertionInfo info, final Path actual, final Path expected) {
    assertNotNull(info, actual);
    checkExpectedParentPathIsNotNull(expected);
    Path parent = toRealPath(actual).getParent();
    if (parent == null) throw failures.failure(info, shouldHaveParent(actual, expected));
    if (!parent.equals(toRealPath(expected))) throw failures.failure(info, shouldHaveParent(actual, parent, expected));
  }

  /**
   * Verifies the raw parent path.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param expected the expected parent
   */
  public void assertHasParentRaw(final AssertionInfo info, final Path actual, final Path expected) {
    assertNotNull(info, actual);
    checkExpectedParentPathIsNotNull(expected);
    Path parent = actual.getParent();
    if (parent == null) throw failures.failure(info, shouldHaveParent(actual, expected));
    if (!parent.equals(expected)) throw failures.failure(info, shouldHaveParent(actual, parent, expected));
  }

  /**
   * Verifies that the normalized path has no parent.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertHasNoParent(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    if (toRealPath(actual).getParent() != null) throw failures.failure(info, shouldHaveNoParent(actual));
  }

  /**
   * Verifies that the raw path has no parent.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertHasNoParentRaw(final AssertionInfo info, final Path actual) {
    assertNotNull(info, actual);
    if (actual.getParent() != null) throw failures.failure(info, shouldHaveNoParent(actual));
  }

  /**
   * Verifies the path size.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param expectedSize the expected size
   */
  public void assertHasSize(final AssertionInfo info, final Path actual, long expectedSize) {
    assertIsRegularFile(info, actual);
    try {
      long actualSize = nioFilesWrapper.size(actual);
      if (actualSize != expectedSize) throw failures.failure(info, shouldHaveSize(actual, expectedSize));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Verifies that the normalized path starts with another path.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param other the expected prefix
   */
  public void assertStartsWith(final AssertionInfo info, final Path actual, final Path other) {
    assertNotNull(info, actual);
    assertExpectedStartPathIsNotNull(other);

    Path absoluteActual = Files.exists(actual) ? toRealPath(actual) : actual.toAbsolutePath().normalize();
    Path absoluteOther = Files.exists(other) ? toRealPath(other) : other.toAbsolutePath().normalize();

    if (!absoluteActual.startsWith(absoluteOther)) {
      throw failures.failure(info, shouldStartWith(actual, other));
    }
  }

  /**
   * Verifies that the raw path starts with another path.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param other the expected prefix
   */
  public void assertStartsWithRaw(final AssertionInfo info, final Path actual, final Path other) {
    assertNotNull(info, actual);
    assertExpectedStartPathIsNotNull(other);
    if (!actual.startsWith(other)) throw failures.failure(info, shouldStartWith(actual, other));
  }

  /**
   * Verifies that the normalized path ends with another path.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param other the expected ending
   */
  public void assertEndsWith(final AssertionInfo info, final Path actual, final Path other) {
    assertNotNull(info, actual);
    assertExpectedEndPathIsNotNull(other);
    Path path = Files.exists(actual) ? toRealPath(actual) : actual;
    if (!path.endsWith(other.normalize())) throw failures.failure(info, shouldEndWith(actual, other));
  }

  /**
   * Verifies that the raw path ends with another path.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param end the expected ending
   */
  public void assertEndsWithRaw(final AssertionInfo info, final Path actual, final Path end) {
    assertNotNull(info, actual);
    assertExpectedEndPathIsNotNull(end);
    if (!actual.endsWith(end)) throw failures.failure(info, shouldEndWith(actual, end));
  }

  /**
   * Verifies the path file name.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param fileName the expected file name
   */
  public void assertHasFileName(final AssertionInfo info, Path actual, String fileName) {
    assertNotNull(info, actual);
    requireNonNull(fileName, "expected fileName should not be null");
    if (!actual.getFileName().endsWith(fileName)) throw failures.failure(info, shouldHaveName(actual, fileName));
  }

  /**
   * Verifies the path textual content.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param expected the expected text
   * @param charset the text charset
   */
  public void assertHasTextualContent(final AssertionInfo info, Path actual, String expected, Charset charset) {
    requireNonNull(expected, "The text to compare to should not be null");
    assertIsReadable(info, actual);
    try {
      List<Delta<String>> diffs = diff.diff(actual, expected, charset);
      if (!diffs.isEmpty()) throw failures.failure(info, shouldHaveContent(actual, charset, diffs));
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to verify text contents of path:<%s>".formatted(actual), e);
    }
  }

  /**
   * Verifies the path binary content.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param expected the expected bytes
   */
  public void assertHasBinaryContent(AssertionInfo info, Path actual, byte[] expected) {
    requireNonNull(expected, "The binary content to compare to should not be null");
    assertIsReadable(info, actual);
    try {
      BinaryDiffResult diffResult = binaryDiff.diff(actual, expected);
      if (!diffResult.hasNoDiff()) throw failures.failure(info, shouldHaveBinaryContent(actual, diffResult));
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to verify binary contents of path:<%s>".formatted(actual), e);
    }
  }

  /**
   * Verifies that two paths have the same binary content.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param expected the comparison path
   */
  public void assertHasSameBinaryContentAs(AssertionInfo info, Path actual, Path expected) {
    requireNonNull(expected, "The given Path to compare actual content to should not be null");
    checkArgument(Files.exists(expected), "The given Path <%s> to compare actual content to should exist", expected);
    checkArgument(Files.isReadable(expected), "The given Path <%s> to compare actual content to should be readable", expected);
    assertIsReadable(info, actual);
    try {
      BinaryDiffResult binaryDiffResult = binaryDiff.diff(actual, readAllBytes(expected));
      if (binaryDiffResult.hasDiff()) throw failures.failure(info, shouldHaveBinaryContent(actual, binaryDiffResult));
    } catch (IOException ioe) {
      throw new UncheckedIOException(UNABLE_TO_COMPARE_PATH_CONTENTS.formatted(actual, expected), ioe);
    }
  }

  /**
   * Verifies that two paths have the same textual content.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param actualCharset the actual path charset
   * @param expected the comparison path
   * @param expectedCharset the comparison path charset
   */
  public void assertHasSameTextualContentAs(AssertionInfo info, Path actual, Charset actualCharset, Path expected,
                                            Charset expectedCharset) {
    requireNonNull(expected, "The given Path to compare actual content to should not be null");
    checkArgument(Files.exists(expected), "The given Path <%s> to compare actual content to should exist", expected);
    checkArgument(Files.isReadable(expected), "The given Path <%s> to compare actual content to should be readable", expected);
    assertIsReadable(info, actual);
    try {
      List<Delta<String>> diffs = diff.diff(actual, actualCharset, expected, expectedCharset);
      if (!diffs.isEmpty()) throw failures.failure(info, shouldHaveSameContent(actual, expected, diffs));
    } catch (IOException e) {
      throw new UncheckedIOException(UNABLE_TO_COMPARE_PATH_CONTENTS.formatted(actual, expected), e);
    }
  }

  /**
   * Verifies the path digest.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param digest the digest algorithm
   * @param expected the expected digest bytes
   */
  public void assertHasDigest(AssertionInfo info, Path actual, MessageDigest digest, byte[] expected) {
    requireNonNull(digest, "The message digest algorithm should not be null");
    requireNonNull(expected, "The binary representation of digest to compare to should not be null");
    assertIsRegularFile(info, actual);
    assertIsReadable(info, actual);
    try (InputStream actualStream = nioFilesWrapper.newInputStream(actual)) {
      DigestDiff diff = Digests.digestDiff(actualStream, digest, expected);
      if (diff.digestsDiffer()) throw failures.failure(info, shouldHaveDigest(actual, diff));
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to calculate digest of path:<%s>".formatted(actual), e);
    }
  }

  /**
   * Verifies the path digest.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param digest the digest algorithm
   * @param expected the expected hexadecimal digest
   */
  public void assertHasDigest(AssertionInfo info, Path actual, MessageDigest digest, String expected) {
    requireNonNull(expected, "The string representation of digest to compare to should not be null");
    assertHasDigest(info, actual, digest, Digests.fromHex(expected));
  }

  /**
   * Verifies the path digest using the named algorithm.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param algorithm the digest algorithm
   * @param expected the expected digest bytes
   */
  public void assertHasDigest(AssertionInfo info, Path actual, String algorithm, byte[] expected) {
    requireNonNull(algorithm, "The message digest algorithm should not be null");
    try {
      assertHasDigest(info, actual, MessageDigest.getInstance(algorithm), expected);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Unable to find digest implementation for: <%s>".formatted(algorithm), e);
    }
  }

  /**
   * Verifies the path digest using the named algorithm.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param algorithm the digest algorithm
   * @param expected the expected hexadecimal digest
   */
  public void assertHasDigest(AssertionInfo info, Path actual, String algorithm, String expected) {
    requireNonNull(expected, "The string representation of digest to compare to should not be null");
    assertHasDigest(info, actual, algorithm, Digests.fromHex(expected));
  }

  /**
   * Verifies that the directory contains a matching entry.
   *
   * @param info assertion information
   * @param actual the actual directory
   * @param filter the entry filter
   */
  public void assertIsDirectoryContaining(AssertionInfo info, Path actual, Predicate<Path> filter) {
    requireNonNull(filter, "The paths filter should not be null");
    assertIsDirectoryContaining(info, actual, filter::test, "the given filter");
  }

  /**
   * Verifies that the directory contains an entry matching the pattern.
   *
   * @param info assertion information
   * @param actual the actual directory
   * @param syntaxAndPattern the path matcher syntax and pattern
   */
  public void assertIsDirectoryContaining(AssertionInfo info, Path actual, String syntaxAndPattern) {
    requireNonNull(syntaxAndPattern, "The syntax and pattern should not be null");
    PathMatcher pathMatcher = pathMatcher(info, actual, syntaxAndPattern);
    assertIsDirectoryContaining(info, actual, pathMatcher::matches, "the '%s' pattern".formatted(syntaxAndPattern));
  }

  /**
   * Verifies that the directory tree contains an entry matching the pattern.
   *
   * @param info assertion information
   * @param actual the actual directory
   * @param syntaxAndPattern the path matcher syntax and pattern
   */
  public void assertIsDirectoryRecursivelyContaining(AssertionInfo info, Path actual, String syntaxAndPattern) {
    requireNonNull(syntaxAndPattern, "The syntax and pattern should not be null");
    PathMatcher pathMatcher = pathMatcher(info, actual, syntaxAndPattern);
    assertIsDirectoryRecursivelyContaining(info, actual, pathMatcher::matches,
                                           "the '%s' pattern".formatted(syntaxAndPattern));
  }

  /**
   * Verifies that the directory tree contains a matching entry.
   *
   * @param info assertion information
   * @param actual the actual directory
   * @param filter the entry filter
   */
  public void assertIsDirectoryRecursivelyContaining(AssertionInfo info, Path actual, Predicate<Path> filter) {
    requireNonNull(filter, "The files filter should not be null");
    assertIsDirectoryRecursivelyContaining(info, actual, filter, "the given filter");
  }

  /**
   * Verifies that the directory contains no matching entry.
   *
   * @param info assertion information
   * @param actual the actual directory
   * @param filter the entry filter
   */
  public void assertIsDirectoryNotContaining(AssertionInfo info, Path actual, Predicate<Path> filter) {
    requireNonNull(filter, "The paths filter should not be null");
    assertIsDirectoryNotContaining(info, actual, filter::test, "the given filter");
  }

  /**
   * Verifies that the directory contains no entry matching the pattern.
   *
   * @param info assertion information
   * @param actual the actual directory
   * @param syntaxAndPattern the path matcher syntax and pattern
   */
  public void assertIsDirectoryNotContaining(AssertionInfo info, Path actual, String syntaxAndPattern) {
    requireNonNull(syntaxAndPattern, "The syntax and pattern should not be null");
    PathMatcher pathMatcher = pathMatcher(info, actual, syntaxAndPattern);
    assertIsDirectoryNotContaining(info, actual, pathMatcher::matches, "the '%s' pattern".formatted(syntaxAndPattern));
  }

  /**
   * Verifies that the path is an empty directory.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsEmptyDirectory(AssertionInfo info, Path actual) {
    List<Path> items = directoryContent(info, actual);
    if (!items.isEmpty()) throw failures.failure(info, shouldBeEmptyDirectory(actual, items));
  }

  /**
   * Verifies that the path is a non-empty directory.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsNotEmptyDirectory(AssertionInfo info, Path actual) {
    boolean isEmptyDirectory = directoryContent(info, actual).isEmpty();
    if (isEmptyDirectory) throw failures.failure(info, shouldNotBeEmpty(actual));
  }

  /**
   * Verifies that the path is an empty file.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsEmptyFile(AssertionInfo info, Path actual) {
    assertIsRegularFile(info, actual);
    try {
      if (nioFilesWrapper.size(actual) > 0) throw failures.failure(info, shouldBeEmpty(actual));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Verifies that the path is a non-empty file.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertIsNotEmptyFile(AssertionInfo info, Path actual) {
    assertIsRegularFile(info, actual);
    try {
      if (nioFilesWrapper.size(actual) == 0) throw failures.failure(info, shouldNotBeEmpty(actual));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Verifies the path file system.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param expectedFileSystem the expected file system
   */
  public void assertHasFileSystem(AssertionInfo info, Path actual, FileSystem expectedFileSystem) {
    assertNotNull(info, actual);
    requireNonNull(expectedFileSystem, "The expected file system should not be null");

    FileSystem actualFileSystem = actual.getFileSystem();
    requireNonNull(actualFileSystem, "The actual file system should not be null");

    if (!expectedFileSystem.equals(actualFileSystem)) {
      throw failures.failure(info, shouldHaveFileSystem(actual, expectedFileSystem), actualFileSystem, expectedFileSystem);
    }
  }

  /**
   * Verifies that two paths use the same file system.
   *
   * @param info assertion information
   * @param actualPath the actual path
   * @param expectedPath the comparison path
   */
  public void assertHasSameFileSystemAs(AssertionInfo info, Path actualPath, Path expectedPath) {
    assertNotNull(info, actualPath);
    requireNonNull(expectedPath, "The expected path should not be null");

    FileSystem actualFileSystem = actualPath.getFileSystem();
    requireNonNull(actualFileSystem, "The actual file system should not be null");
    FileSystem expectedFileSystem = expectedPath.getFileSystem();
    requireNonNull(expectedFileSystem, "The expected file system should not be null");

    if (!expectedFileSystem.equals(actualFileSystem)) {
      throw failures.failure(info, shouldHaveSameFileSystemAs(actualPath, expectedPath), actualFileSystem, expectedFileSystem);
    }
  }

  // non-public section

  private List<Path> filterDirectory(AssertionInfo info, Path actual, Filter<Path> filter) {
    assertIsDirectory(info, actual);
    try (DirectoryStream<Path> stream = nioFilesWrapper.newDirectoryStream(actual, filter)) {
      return stream(stream.spliterator(), false).collect(toList());
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to list directory content: <%s>".formatted(actual), e);
    }
  }

  private List<Path> directoryContent(AssertionInfo info, Path actual) {
    return filterDirectory(info, actual, ANY);
  }

  private void assertIsDirectoryContaining(AssertionInfo info, Path actual, Filter<Path> filter, String filterPresentation) {
    List<Path> matchingFiles = filterDirectory(info, actual, filter);
    if (matchingFiles.isEmpty()) {
      throw failures.failure(info, directoryShouldContain(actual, directoryContent(info, actual), filterPresentation));
    }
  }

  private boolean isDirectoryRecursivelyContaining(AssertionInfo info, Path actual, Predicate<Path> filter) {
    assertIsDirectory(info, actual);
    try (Stream<Path> actualContent = recursiveContentOf(actual)) {
      return actualContent.anyMatch(filter);
    }
  }

  private List<Path> sortedRecursiveContent(Path path) {
    try (Stream<Path> pathContent = recursiveContentOf(path)) {
      return pathContent.sorted().collect(toList());
    }
  }

  private Stream<Path> recursiveContentOf(Path directory) {
    try {
      return walk(directory).filter(p -> !p.equals(directory));
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to walk recursively the directory :<%s>".formatted(directory), e);
    }
  }

  private void assertIsDirectoryRecursivelyContaining(AssertionInfo info, Path actual, Predicate<Path> filter,
                                                      String filterPresentation) {
    if (!isDirectoryRecursivelyContaining(info, actual, filter)) {
      throw failures.failure(info, directoryShouldContainRecursively(actual, sortedRecursiveContent(actual), filterPresentation));
    }
  }

  private void assertIsDirectoryNotContaining(AssertionInfo info, Path actual, Filter<Path> filter, String filterPresentation) {
    List<Path> matchingPaths = filterDirectory(info, actual, filter);
    if (!matchingPaths.isEmpty()) {
      throw failures.failure(info, directoryShouldNotContain(actual, matchingPaths, filterPresentation));
    }
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

  private static Path toRealPath(Path path) {
    try {
      return path.toRealPath();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Verifies the path extension.
   *
   * @param info assertion information
   * @param actual the actual path
   * @param expected the expected extension
   */
  public void assertHasExtension(AssertionInfo info, Path actual, String expected) {
    requireNonNull(expected, "The expected extension should not be null.");
    assertIsRegularFile(info, actual);
    String extension = getExtension(actual).orElseThrow(() -> failures.failure(info, shouldHaveExtension(actual, expected)));
    if (!expected.equals(extension)) throw failures.failure(info, shouldHaveExtension(actual, extension, expected));
  }

  /**
   * Verifies that the path has no extension.
   *
   * @param info assertion information
   * @param actual the actual path
   */
  public void assertHasNoExtension(AssertionInfo info, Path actual) {
    assertIsRegularFile(info, actual);
    Optional<String> extension = getExtension(actual);
    if (extension.isPresent()) throw failures.failure(info, shouldHaveNoExtension(actual, extension.get()));
  }

  private static Optional<String> getExtension(Path path) {
    String fileName = path.getFileName().toString();
    return getFileNameExtension(fileName);
  }

}
