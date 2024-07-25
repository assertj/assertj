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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.spi.FileSystemProvider;
import java.security.MessageDigest;
import java.util.function.Predicate;

import org.assertj.core.api.exception.PathsException;
import org.assertj.core.internal.Paths;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for {@link Path} objects
 *
 * <p>
 * Note that some assertions have two versions: a normal one and a "raw" one (for instance, {@code hasParent()} and
 * {@code hasParentRaw()}. The difference is that normal assertions will {@link Path#toRealPath(LinkOption...)
 * canonicalize} or {@link Path#normalize() normalize} the tested path and, where applicable, the path argument, before
 * performing the actual test. Canonicalization includes normalization.
 * </p>
 *
 * <p>
 * Canonicalization may lead to an I/O error if a path does not exist, in which case the given assertions will fail with
 * a {@link PathsException}. Also note that {@link Files#isSymbolicLink(Path) symbolic links} will be followed if the
 * filesystem supports them. Finally, if a path is not {@link Path#isAbsolute() absolute}, canonicalization will
 * resolve the path against the process' current working directory.
 * </p>
 *
 * <p>
 * These assertions are filesystem independent. You may use them on {@code Path} instances issued from the default
 * filesystem (ie, instances you get when using {@link java.nio.file.Paths#get(String, String...)}) or from other
 * filesystems. For more information, see the javadoc for {@link FileSystem}.
 * </p>
 *
 * <p>
 * Furthermore:
 * </p>
 *
 * <ul>
 * <li>Unless otherwise noted, assertions which accept arguments will not accept {@code null} arguments; if a null
 * argument is passed, these assertions will throw a {@link NullPointerException}.</li>
 * <li>It is the caller's responsibility to ensure that paths used in assertions are issued from valid filesystems which
 * are not {@link FileSystem#close() closed}. If a filesystems is closed, assertions will throw a
 * {@link ClosedFileSystemException}.</li>
 * <li>Some assertions take another {@link Path} as an argument. If this path is not issued from the same
 * {@link FileSystemProvider provider} as the tested path, assertions will throw a {@link ProviderMismatchException}.</li>
 * <li>Some assertions may need to perform I/O on the path's underlying filesystem; if an I/O error occurs when
 * accessing the filesystem, these assertions will throw a {@link PathsException}.</li>
 * </ul>
 *
 * @author Valeriy Vyrva
 *
 * @param <SELF> self type
 *
 * @see Path
 * @see java.nio.file.Paths#get(String, String...)
 * @see FileSystem
 * @see FileSystem#getPath(String, String...)
 * @see java.nio.file.FileSystems#getDefault()
 * @see Files
 */
public abstract class AbstractPathAssert<SELF extends AbstractPathAssert<SELF>> extends AbstractComparableAssert<SELF, Path> {

  @VisibleForTesting
  protected Paths paths = Paths.instance();

  @VisibleForTesting
  Charset charset = Charset.defaultCharset();

  protected AbstractPathAssert(final Path actual, final Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * <p>
   * Verifies that the content of the actual {@code Path} is the same as the given one (both paths must be a readable
   * files).
   * The charset to use when reading the actual path can be provided with {@link #usingCharset(Charset)} or
   * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
   * {@link Charset#defaultCharset()}) will be used.
   *
   * Examples:
   * <pre><code class="java"> // use the default charset
   * Path xFile = Files.write(Paths.get("xfile.txt"), "The Truth Is Out There".getBytes());
   * Path xFileUTF8 = Files.write(Paths.get("xfile-clone.txt"), "The Truth Is Out There".getBytes("UTF-8"));
   * Path xFileClone = Files.write(Paths.get("xfile-clone.txt"), "The Truth Is Out There".getBytes());
   * Path xFileFrench = Files.write(Paths.get("xfile-french.txt"), "La Vérité Est Ailleurs".getBytes());
   *
   * // The following assertion succeeds (default charset is used):
   * assertThat(xFile).hasSameContentAs(xFileClone);
   * // The following assertion succeeds (UTF-8 charset is used to read xFile):
   * assertThat(xFileUTF8).usingCharset("UTF-8").hasContent(xFileClone);
   *
   * // The following assertion fails:
   * assertThat(xFile).hasSameContentAs(xFileFrench);</code></pre>
   *
   * @param expected the given {@code Path} to compare the actual {@code Path} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code Path} is {@code null}.
   * @throws AssertionError if the actual or given {@code Path} is not an existing readable file.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @throws AssertionError if the content of the actual {@code Path} is not equal to the content of the given one.
   * @throws PathsException if an I/O error occurs.
   *
   * @deprecated use {@link #hasSameTextualContentAs(Path)} instead
   */
  @Deprecated
  public SELF hasSameContentAs(Path expected) {
    return hasSameTextualContentAs(expected);
  }

  /**
   * Verifies that the content of the actual {@code Path} is the same as the given one (both paths must be a readable
   * files).
   * The charset to use when reading the actual path can be provided with {@link #usingCharset(Charset)} or
   * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
   * {@link Charset#defaultCharset()}) will be used.
   * <p>
   * Examples:
   * <pre><code class="java"> // use the default charset
   * Path xFile = Files.write(Paths.get("xfile.txt"), "The Truth Is Out There".getBytes());
   * Path xFileUTF8 = Files.write(Paths.get("xfile-clone.txt"), "The Truth Is Out There".getBytes("UTF-8"));
   * Path xFileClone = Files.write(Paths.get("xfile-clone.txt"), "The Truth Is Out There".getBytes());
   * Path xFileFrench = Files.write(Paths.get("xfile-french.txt"), "La Vérité Est Ailleurs".getBytes());
   *
   * // The following assertion succeeds (default charset is used):
   * assertThat(xFile).hasSameTextualContentAs(xFileClone);
   * // The following assertion succeeds (UTF-8 charset is used to read xFile):
   * assertThat(xFileUTF8).usingCharset("UTF-8").hasContent(xFileClone);
   *
   * // The following assertion fails:
   * assertThat(xFile).hasSameTextualContentAs(xFileFrench);</code></pre>
   *
   * @param expected the given {@code Path} to compare the actual {@code Path} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code Path} is {@code null}.
   * @throws AssertionError if the actual or given {@code Path} is not an existing readable file.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @throws AssertionError if the content of the actual {@code Path} is not equal to the content of the given one.
   * @throws PathsException if an I/O error occurs.
   * @since 3.15
   */
  public SELF hasSameTextualContentAs(Path expected) {
    paths.assertHasSameTextualContentAs(info, actual, charset, expected, Charset.defaultCharset());
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code Path} is the same as the expected one, the expected {@code Path} being read with the given charset while
   * the charset used to read the actual path can be provided with {@link #usingCharset(Charset)} or
   * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
   * {@link Charset#defaultCharset()}) will be used.
   * <p>
   * Examples:
   * <pre><code class="java"> Path fileUTF8Charset = Files.write(Paths.get("actual"), Collections.singleton("Gerçek"), StandardCharsets.UTF_8);
   * Charset turkishCharset = Charset.forName("windows-1254");
   * Path fileTurkischCharset = Files.write(Paths.get("expected"), Collections.singleton("Gerçek"), turkishCharset);
   *
   * // The following assertion succeeds:
   * assertThat(fileUTF8Charset).usingCharset(StandardCharsets.UTF_8).hasSameTextualContentAs(fileTurkischCharset, turkishCharset);
   *
   * // The following assertion fails:
   * assertThat(fileUTF8Charset).usingCharset(StandardCharsets.UTF_8).hasSameTextualContentAs(fileTurkischCharset, StandardCharsets.UTF_8);</code></pre>
   *
   * @param expected the given {@code Path} to compare the actual {@code Path} to.
   * @param expectedCharset the {@link Charset} used to read the content of the expected Path.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code Path} is {@code null}.
   * @throws AssertionError if the actual or given {@code Path} is not an existing readable file.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @throws AssertionError if the content of the actual {@code Path} is not equal to the content of the given one.
   * @throws PathsException if an I/O error occurs.
   * @since 3.15
   */
  public SELF hasSameTextualContentAs(Path expected, Charset expectedCharset) {
    paths.assertHasSameTextualContentAs(info, actual, charset, expected, expectedCharset);
    return myself;
  }

  /**
   * <p>
   * Verifies that the content of the actual {@code Path} is the same as the expected one, the expected {@code Path} being read with the given charset while
   * the charset used to read the actual path can be provided with {@link #usingCharset(Charset)} or
   * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
   * {@link Charset#defaultCharset()}) will be used.
   * <p>
   * Examples:
   * <pre><code class="java"> Path fileUTF8Charset = Files.write(Paths.get("actual"), Collections.singleton("Gerçek"), StandardCharsets.UTF_8);
   * Charset turkishCharset = Charset.forName("windows-1254");
   * Path fileTurkischCharset = Files.write(Paths.get("expected"), Collections.singleton("Gerçek"), turkishCharset);
   *
   * // The following assertion succeeds:
   * assertThat(fileUTF8Charset).usingCharset(StandardCharsets.UTF_8).hasSameContentAs(fileTurkischCharset, turkishCharset);
   *
   * // The following assertion fails:
   * assertThat(fileUTF8Charset).usingCharset(StandardCharsets.UTF_8).hasSameContentAs(fileTurkischCharset, StandardCharsets.UTF_8);</code></pre>
   *
   * @param expected the given {@code Path} to compare the actual {@code Path} to.
   * @param expectedCharset the {@link Charset} used to read the content of the expected Path.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code Path} is {@code null}.
   * @throws AssertionError if the actual or given {@code Path} is not an existing readable file.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @throws AssertionError if the content of the actual {@code Path} is not equal to the content of the given one.
   * @throws PathsException if an I/O error occurs.
   *
   * @deprecated use {@link #hasSameTextualContentAs(Path, Charset)} instead
   */
  @Deprecated
  public SELF hasSameContentAs(Path expected, Charset expectedCharset) {
    return hasSameTextualContentAs(expected, expectedCharset);
  }

  /**
   * Verifies that the binary content of the actual {@code Path} is <b>exactly</b> equal to the given one.
   *
   * Examples:
   * <pre><code class="java"> // using the default charset, the following assertion succeeds:
   * Path xFile = Files.write(Paths.get("xfile.txt"), "The Truth Is Out There".getBytes());
   * assertThat(xFile).hasBinaryContent("The Truth Is Out There".getBytes());
   *
   * // using a specific charset
   * Charset turkishCharset = Charset.forName("windows-1254");
   * Path xFileTurkish = Files.write(Paths.get("xfile.turk"), Collections.singleton("Gerçek Başka bir yerde mi"), turkishCharset);
   *
   * // The following assertion succeeds:
   * String expectedContent = "Gerçek Başka bir yerde mi" + org.assertj.core.util.Compatibility.System.lineSeparator();
   * byte[] binaryContent = expectedContent.getBytes(turkishCharset.name());
   * assertThat(xFileTurkish).hasBinaryContent(binaryContent);
   *
   * // The following assertion fails ... unless you are in Turkey ;-):
   * assertThat(xFileTurkish).hasBinaryContent("Gerçek Başka bir yerde mi".getBytes());</code></pre>
   *
   * @param expected the expected binary content to compare the actual {@code Path}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given content is {@code null}.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @throws AssertionError if the actual {@code Path} is not an existing file.
   * @throws UncheckedIOException if an I/O error occurs.
   * @throws AssertionError if the content of the actual {@code Path} is not equal to the given binary content.
   */
  public SELF hasBinaryContent(byte[] expected) {
    paths.assertHasBinaryContent(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code Path} is equal to the content of the given one, the comparison is done at the binary level.<br>
   * For text files, use {@link #hasSameTextualContentAs(Path)} or {@link #hasSameTextualContentAs(Path, Charset)}.
   * <p>
   * Examples:
   * <pre><code class="java"> // The first two paths have the same contents, the third does not
   * Path aPath = Files.write(Paths.get("a-file.bin"), new byte[] { 42 });
   * Path bPath = Files.write(Paths.get("b-file.bin"), new byte[] { 42 });
   * Path cPath = Files.write(Paths.get("c-file.bin"), new byte[] { 24 });
   *
   * // The following assertion succeeds:
   * assertThat(aPath).hasSameBinaryContentAs(bPath);
   *
   * // The following assertion fails:
   * assertThat(aPath).hasSameBinaryContent(cPath);</code></pre>
   *
   * @param expected the given {@code Path} to compare the actual {@code Path} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code Path} is {@code null}.
   * @throws IllegalArgumentException if the given {@code Path} is not an existing file.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @throws AssertionError if the actual {@code Path} is not an existing file.
   * @throws UncheckedIOException if any I/O error occurs.
   * @throws AssertionError if the content of the actual {@code Path} is not equal to the content of the given one.
   * @since 3.15
   */
  public SELF hasSameBinaryContentAs(Path expected) {
    paths.assertHasSameBinaryContentAs(info, actual, expected);
    return myself;
  }

  /**
   * Specifies the name of the charset to use for text-based assertions on the path's contents (path must be a readable
   * file).
   *
   * Examples:
   * <pre><code class="java"> Charset turkishCharset = Charset.forName("windows-1254");
   * Path xFileTurkish = Files.write(Paths.get("xfile.turk"), Collections.singleton("Gerçek Başka bir yerde mi"), turkishCharset);
   *
   * // The following assertion succeeds:
   * assertThat(xFileTurkish).usingCharset("windows-1254").hasContent("Gerçek Başka bir yerde mi");</code></pre>
   *
   * @param charsetName the name of the charset to use.
   * @return {@code this} assertion object.
   * @throws IllegalArgumentException if the given encoding is not supported on this platform.
   */
  @CheckReturnValue
  public SELF usingCharset(String charsetName) {
    checkArgument(Charset.isSupported(charsetName), "Charset:<'%s'> is not supported on this system", charsetName);
    return usingCharset(Charset.forName(charsetName));
  }

  /**
   * Specifies the charset to use for text-based assertions on the path's contents (path must be a readable file).
   *
   * Examples:
   * <pre><code class="java"> Charset turkishCharset = Charset.forName("windows-1254");
   * Path xFileTurkish = Files.write(Paths.get("xfile.turk"), Collections.singleton("Gerçek Başka bir yerde mi"), turkishCharset);
   *
   * // The following assertion succeeds:
   * assertThat(xFileTurkish).usingCharset(turkishCharset).hasContent("Gerçek Başka bir yerde mi");</code></pre>
   *
   * @param charset the charset to use.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given charset is {@code null}.
   */
  @CheckReturnValue
  public SELF usingCharset(Charset charset) {
    this.charset = requireNonNull(charset, "The charset should not be null");
    return myself;
  }

  /**
   * Verifies that the text content of the actual {@code Path} (which must be a readable file) is <b>exactly</b> equal
   * to the given one <b>except for newlines wich are ignored</b>.
   * <p>
   * This will change in AssertJ 4.0 where newlines will be taken into account, in the meantime, to get this behavior
   * one can use {@link #content()} and then chain with {@link AbstractStringAssert#isEqualTo(String)}.
   * <p>
   * The charset to use when reading the actual path should be provided with {@link #usingCharset(Charset)} or
   * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
   * {@link Charset#defaultCharset()}) will be used.
   *
   * Examples:
   * <pre><code class="java"> // use the default charset
   * Path xFile = Files.write(Paths.get("xfile.txt"), "The Truth Is Out There".getBytes());
   *
   * // The following assertion succeeds (default charset is used):
   * assertThat(xFile).hasContent("The Truth Is Out There");
   *
   * // The following assertion fails:
   * assertThat(xFile).hasContent("La Vérité Est Ailleurs");
   *
   * // using a specific charset
   * Charset turkishCharset = Charset.forName("windows-1254");
   *
   * Path xFileTurkish = Files.write(Paths.get("xfile.turk"), Collections.singleton("Gerçek Başka bir yerde mi"), turkishCharset);
   *
   * // The following assertion succeeds:
   * assertThat(xFileTurkish).usingCharset(turkishCharset).hasContent("Gerçek Başka bir yerde mi");
   *
   * // The following assertion fails ... unless you are in Turkey ;-):
   * assertThat(xFileTurkish).hasContent("Gerçek Başka bir yerde mi");</code></pre>
   *
   * @param expected the expected text content to compare the actual {@code Path}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given content is {@code null}.
   * @throws UncheckedIOException if an I/O error occurs.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @throws AssertionError if the actual {@code Path} is not a {@link Files#isReadable(Path) readable} file.
   * @throws AssertionError if the content of the actual {@code Path} is not equal to the given content.
   */
  public SELF hasContent(String expected) {
    paths.assertHasTextualContent(info, actual, expected, charset);
    return myself;
  }

  /**
   * Verifies that the path has the given file system.
   * <p>
   * Examples:
   * <pre><code class="java"> Path jarFile = Paths.get("assertj-core.jar");
   * FileSystem mainFileSystem = jarFile.getFileSystem();
   *
   * try (FileSystem fs = FileSystems.newFileSystem(jarFile, (ClassLoader) null)) {
   *     Path manifestFile = fs.getPath("META-INF", "MANIFEST.MF");
   *
   *     // Succeeds
   *     assertThat(manifestFile).hasFileSystem(fs);
   *
   *     // Fails
   *     assertThat(manifestFile).hasFileSystem(mainFileSystem);
   * }</code></pre>
   *
   * @param expected the given {@code FileSystem} to compare the actual {@code Path} file system to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code FileSystem} is {@code null}.
   * @throws AssertionError if the actual {@code FileSystem} is {@code null}.
   * @since 3.23.0
   */
  public SELF hasFileSystem(FileSystem expected) {
    paths.assertHasFileSystem(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the path file system is the same as the given path file system.
   *
   * Examples:
   * <pre><code class="java"> Path jarFile = Paths.get("assertj-core.jar");
   * try (FileSystem fs = FileSystems.newFileSystem(jarFile, (ClassLoader) null)) {
   *     Path manifestFile = fs.getPath("META-INF", "MANIFEST.MF");
   *     Path abstractPathAssertFile = fs.getPath("org", "assertj", "core", "api", "AbstractPathAssert.class");
   *
   *     // Succeeds
   *     assertThat(manifestFile).hasSameFileSystemAs(abstractPathAssertFile);
   *
   *     // Fails
   *     assertThat(manifestFile).hasSameFileSystemAs(jarFile);
   * }</code></pre>
   *
   * @param expected the given {@code Path} to compare the actual {@code Path} file system to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code Path} is {@code null}.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @since 3.23.0
   */
  public SELF hasSameFileSystemAs(Path expected) {
    paths.assertHasSameFileSystemAs(info, actual, expected);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is a readable file, it checks that the file exists (according to
   * {@link Files#exists(Path, LinkOption...)}) and that it is readable(according to {@link Files#isReadable(Path)}).
   *
   * Examples:
   * <pre><code class="java"> // Create a file and set permissions to be readable by all.
   * Path readableFile = Paths.get("readableFile");
   * Set&lt;PosixFilePermission&gt; perms = PosixFilePermissions.fromString("r--r--r--");
   * Files.createFile(readableFile, PosixFilePermissions.asFileAttribute(perms));
   *
   * final Path symlinkToReadableFile = FileSystems.getDefault().getPath("symlinkToReadableFile");
   * Files.createSymbolicLink(symlinkToReadableFile, readableFile);
   *
   * // Create a file and set permissions not to be readable.
   * Path nonReadableFile = Paths.get("nonReadableFile");
   * Set&lt;PosixFilePermission&gt; notReadablePerms = PosixFilePermissions.fromString("-wx------");
   * Files.createFile(nonReadableFile, PosixFilePermissions.asFileAttribute(notReadablePerms));
   *
   * final Path nonExistentPath = FileSystems.getDefault().getPath("nonexistent");
   *
   * // The following assertions succeed:
   * assertThat(readableFile).isReadable();
   * assertThat(symlinkToReadableFile).isReadable();
   *
   * // The following assertions fail:
   * assertThat(nonReadableFile).isReadable();
   * assertThat(nonExistentPath).isReadable();</code></pre>
   *
   * @return self
   *
   * @see Files#isReadable(Path)
   */
  public SELF isReadable() {
    paths.assertIsReadable(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is a writable file, it checks that the file exists (according to
   * {@link Files#exists(Path, LinkOption...)}) and that it is writable(according to {@link Files#isWritable(Path)}).
   *
   * Examples:
   * <pre><code class="java"> Create a file and set permissions to be writable by all.
   * Path writableFile = Paths.get("writableFile");
   * Set&lt;PosixFilePermission&gt; perms = PosixFilePermissions.fromString("rw-rw-rw-");
   * Files.createFile(writableFile, PosixFilePermissions.asFileAttribute(perms));
   *
   * final Path symlinkToWritableFile = FileSystems.getDefault().getPath("symlinkToWritableFile");
   * Files.createSymbolicLink(symlinkToWritableFile, writableFile);
   *
   * // Create a file and set permissions not to be writable.
   * Path nonWritableFile = Paths.get("nonWritableFile");
   * perms = PosixFilePermissions.fromString("r--r--r--");
   * Files.createFile(nonWritableFile, PosixFilePermissions.asFileAttribute(perms));
   *
   * final Path nonExistentPath = FileSystems.getDefault().getPath("nonexistent");
   *
   * // The following assertions succeed:
   * assertThat(writableFile).isWritable();
   * assertThat(symlinkToWritableFile).isWritable();
   *
   * // The following assertions fail:
   * assertThat(nonWritableFile).isWritable();
   * assertThat(nonExistentPath).isWritable();</code></pre>
   *
   * @return self
   *
   * @see Files#isWritable(Path)
   */
  public SELF isWritable() {
    paths.assertIsWritable(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is an executable file, it checks that the file exists (according to
   * {@link Files#exists(Path, LinkOption...)}) and that it is executable(according to {@link Files#isExecutable(Path)}
   * ).
   *
   * Examples:
   * <pre><code class="java"> // Create a file and set permissions to be executable by all.
   * Path executableFile = Paths.get("executableFile");
   * Set&lt;PosixFilePermission&gt; perms = PosixFilePermissions.fromString("r-xr-xr-x");
   * Files.createFile(executableFile, PosixFilePermissions.asFileAttribute(perms));
   *
   * final Path symlinkToExecutableFile = FileSystems.getDefault().getPath("symlinkToExecutableFile");
   * Files.createSymbolicLink(symlinkToExecutableFile, executableFile);
   *
   * // Create a file and set permissions not to be executable.
   * Path nonExecutableFile = Paths.get("nonExecutableFile");
   * perms = PosixFilePermissions.fromString("rw-------");
   * Files.createFile(nonExecutableFile, PosixFilePermissions.asFileAttribute(perms));
   *
   * final Path nonExistentPath = FileSystems.getDefault().getPath("nonexistent");
   *
   * // The following assertions succeed:
   * assertThat(executableFile).isExecutable();
   * assertThat(symlinkToExecutableFile).isExecutable();
   *
   * // The following assertions fail:
   * assertThat(nonExecutableFile).isExecutable();
   * assertThat(nonExistentPath).isExecutable();</code></pre>
   *
   * @return self
   *
   * @see Files#isExecutable(Path)
   */
  public SELF isExecutable() {
    paths.assertIsExecutable(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} exists according to {@link Files#exists(Path, LinkOption...)
   * Files#exists(Path)})
   *
   * <p>
   * <strong>Note that this assertion will follow symbolic links before asserting the path's existence.</strong>
   * </p>
   *
   * <p>
   * On Windows system, this has no influence. On Unix systems, this means the assertion result will fail if the path is
   * a symbolic link whose target does not exist. If you want to assert the existence of the symbolic link itself, use
   * {@link #existsNoFollowLinks()} instead.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   * // Create a regular file, and a symbolic link pointing to it
   * final Path existingFile = fs.getPath("somefile");
   * Files.createFile(existingFile);
   * final Path symlinkToExistingFile = fs.getPath("symlinkToExistingFile");
   * Files.createSymbolicLink(symlinkToExistingFile, existingFile);
   *
   * // Create a symbolic link whose target does not exist
   * final Path nonExistentPath = fs.getPath("nonexistent");
   * final Path symlinkToNonExistentPath = fs.getPath("symlinkToNonExistentPath");
   * Files.createSymbolicLink(symlinkToNonExistentPath, nonExistentPath);
   *
   * // The following assertions succeed:
   * assertThat(existingFile).exists();
   * assertThat(symlinkToExistingFile).exists();
   *
   * // The following assertions fail:
   * assertThat(nonExistentPath).exists();
   * assertThat(symlinkToNonExistentPath).exists();</code></pre>
   *
   * @return self
   *
   * @see Files#exists(Path, LinkOption...)
   */
  public SELF exists() {
    paths.assertExists(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} exists, not following symbolic links, by calling
   * {@link Files#exists(Path, LinkOption...) Files#exists(Path, LinkOption.NOFOLLOW_LINKS)}).
   *
   * <p>
   * This assertion behaves like {@link #exists()}, with the difference that it can be used to assert the existence of a
   * symbolic link even if its target is invalid.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   * // Create a regular file, and a symbolic link pointing to it
   * final Path existingFile = fs.getPath("somefile");
   * Files.createFile(existingFile);
   * final Path symlinkToExistingFile = fs.getPath("symlink");
   * Files.createSymbolicLink(symlinkToExistingFile, existingFile);
   *
   * // Create a symbolic link whose target does not exist
   * final Path nonExistentPath = fs.getPath("nonexistent");
   * final Path symlinkToNonExistentPath = fs.getPath("symlinkToNonExistentPath");
   * Files.createSymbolicLink(symlinkToNonExistentPath, nonExistentPath);
   *
   * // The following assertions succeed
   * assertThat(existingFile).existsNoFollowLinks();
   * assertThat(symlinkToExistingFile).existsNoFollowLinks();
   * assertThat(symlinkToNonExistentPath).existsNoFollowLinks();
   *
   * // The following assertion fails
   * assertThat(nonExistentPath).existsNoFollowLinks();</code></pre>
   *
   * @return self
   *
   * @see Files#exists(Path, LinkOption...)
   */
  public SELF existsNoFollowLinks() {
    paths.assertExistsNoFollowLinks(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} does not exist.
   *
   * <p>
   * <strong>IMPORTANT NOTE:</strong> this method will NOT follow symbolic links (provided that the underlying
   * {@link FileSystem} of this path supports symbolic links at all).
   * </p>
   *
   * <p>
   * This means that even if the link exists this assertion will fail even if the link's target does not exists - note
   * that this is unlike the default behavior of {@link #exists()}.
   * </p>
   *
   * <p>
   * If you are a Windows user, the above does not apply to you; if you are a Unix user however, this is important.
   * Consider the following:
   * <pre><code class="java"> // fs is a FileSystem
   * // Create a regular file, and a symbolic link pointing to it
   * final Path existingFile = fs.getPath("somefile");
   * Files.createFile(existingFile);
   * final Path symlinkToExistingFile = fs.getPath("symlink");
   * Files.createSymbolicLink(symlinkToExistingFile, existingFile);
   *
   * // Create a symbolic link to a nonexistent target file.
   * final Path nonExistentPath = fs.getPath("nonExistentPath");
   * final Path symlinkToNonExistentPath = fs.getPath("symlinkToNonExistentPath");
   * Files.createSymbolicLink(symlinkToNonExistentPath, nonExistentPath);
   *
   * // The following assertion succeeds
   * assertThat(nonExistentPath).doesNotExist();
   *
   * // The following assertions fail:
   * assertThat(existingFile).doesNotExist();
   * assertThat(symlinkToExistingFile).doesNotExist();
   * // fail because symlinkToNonExistentPath exists even though its target does not.
   * assertThat(symlinkToNonExistentPath).doesNotExist();</code></pre>
   *
   * @return self
   *
   * @see Files#notExists(Path, LinkOption...)
   * @see LinkOption#NOFOLLOW_LINKS
   */
  public SELF doesNotExist() {
    paths.assertDoesNotExist(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is a regular file.
   *
   * <p>
   * <strong>Note that this method will follow symbolic links.</strong> If you are a Unix user and wish to assert that a
   * path is a symbolic link instead, use {@link #isSymbolicLink()}.
   * </p>
   *
   * <p>
   * This assertion first asserts the existence of the path (using {@link #exists()}) then checks whether the path is a
   * regular file.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   *
   * // Create a regular file, and a symbolic link to that regular file
   * final Path existingFile = fs.getPath("existingFile");
   * final Path symlinkToExistingFile = fs.getPath("symlinkToExistingFile");
   * Files.createFile(existingFile);
   * Files.createSymbolicLink(symlinkToExistingFile, existingFile);
   *
   * // Create a directory, and a symbolic link to that directory
   * final Path dir = fs.getPath("dir");
   * final Path dirSymlink = fs.getPath("dirSymlink");
   * Files.createDirectories(dir);
   * Files.createSymbolicLink(dirSymlink, dir);
   *
   * // Create a nonexistent entry, and a symbolic link to that entry
   * final Path nonExistentPath = fs.getPath("nonexistent");
   * final Path symlinkToNonExistentPath = fs.getPath("symlinkToNonExistentPath");
   * Files.createSymbolicLink(symlinkToNonExistentPath, nonExistentPath);
   *
   * // the following assertions succeed:
   * assertThat(existingFile).isRegularFile();
   * assertThat(symlinkToExistingFile).isRegularFile();
   *
   * // the following assertions fail because paths do not exist:
   * assertThat(nonExistentPath).isRegularFile();
   * assertThat(symlinkToNonExistentPath).isRegularFile();
   *
   * // the following assertions fail because paths exist but are not regular files:
   * assertThat(dir).isRegularFile();
   * assertThat(dirSymlink).isRegularFile();</code></pre>
   *
   * @return self
   */
  public SELF isRegularFile() {
    paths.assertIsRegularFile(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is a directory.
   * <p>
   * <strong>Note that this method will follow symbolic links.</strong> If you are a Unix user and wish to assert that a
   * path is a symbolic link instead, use {@link #isSymbolicLink()}.
   * </p>
   *
   * <p>
   * This assertion first asserts the existence of the path (using {@link #exists()}) then checks whether the path is a
   * directory.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   *
   * // Create a regular file, and a symbolic link to that regular file
   * final Path existingFile = fs.getPath("existingFile");
   * final Path symlinkToExistingFile = fs.getPath("symlinkToExistingFile");
   * Files.createFile(existingFile);
   * Files.createSymbolicLink(symlinkToExistingFile, existingFile);
   *
   * // Create a directory, and a symbolic link to that directory
   * final Path dir = fs.getPath("dir");
   * final Path dirSymlink = fs.getPath("dirSymlink");
   * Files.createDirectories(dir);
   * Files.createSymbolicLink(dirSymlink, dir);
   *
   * // Create a nonexistent entry, and a symbolic link to that entry
   * final Path nonExistentPath = fs.getPath("nonexistent");
   * final Path symlinkToNonExistentPath = fs.getPath("symlinkToNonExistentPath");
   * Files.createSymbolicLink(symlinkToNonExistentPath, nonExistentPath);
   *
   * // the following assertions succeed:
   * assertThat(dir).isDirectory();
   * assertThat(dirSymlink).isDirectory();
   *
   * // the following assertions fail because paths do not exist:
   * assertThat(nonExistentPath).isDirectory();
   * assertThat(symlinkToNonExistentPath).isDirectory();
   *
   * // the following assertions fail because paths exist but are not directories:
   * assertThat(existingFile).isDirectory();
   * assertThat(symlinkToExistingFile).isDirectory();</code></pre>
   *
   * @return self
   */
  public SELF isDirectory() {
    paths.assertIsDirectory(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is a symbolic link.
   * <p>
   * This assertion first asserts the existence of the path (using {@link #existsNoFollowLinks()}) then checks whether
   * the path is a symbolic link.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   *
   * // Create a regular file, and a symbolic link to that regular file
   * final Path existingFile = fs.getPath("existingFile");
   * final Path symlinkToExistingFile = fs.getPath("symlinkToExistingFile");
   * Files.createFile(existingFile);
   * Files.createSymbolicLink(symlinkToExistingFile, existingFile);
   *
   * // Create a directory, and a symbolic link to that directory
   * final Path dir = fs.getPath("dir");
   * final Path dirSymlink = fs.getPath("dirSymlink");
   * Files.createDirectories(dir);
   * Files.createSymbolicLink(dirSymlink, dir);
   *
   * // Create a nonexistent entry, and a symbolic link to that entry
   * final Path nonExistentPath = fs.getPath("nonexistent");
   * final Path symlinkToNonExistentPath = fs.getPath("symlinkToNonExistentPath");
   * Files.createSymbolicLink(symlinkToNonExistentPath, nonExistentPath);
   *
   * // the following assertions succeed:
   * assertThat(dirSymlink).isSymbolicLink();
   * assertThat(symlinkToExistingFile).isSymbolicLink();
   * assertThat(symlinkToNonExistentPath).isSymbolicLink();
   *
   * // the following assertion fails because the path does not exist:
   * assertThat(nonExistentPath).isSymbolicLink();
   *
   * // the following assertions fail because paths exist but are not symbolic links
   * assertThat(existingFile).isSymbolicLink();
   * assertThat(dir).isSymbolicLink();</code></pre>
   *
   * @return self
   */
  public SELF isSymbolicLink() {
    paths.assertIsSymbolicLink(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is absolute (the path does not have to exist).
   *
   * <p>
   * Note that the fact that a path is absolute does not mean that it is {@link Path#normalize() normalized}:
   * {@code /foo/..} is absolute, for instance, but it is not normalized.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // unixFs is a Unix FileSystem
   *
   * // The following assertion succeeds:
   * assertThat(unixFs.getPath("/foo/bar")).isAbsolute();
   *
   * // The following assertion fails:
   * assertThat(unixFs.getPath("foo/bar")).isAbsolute();
   *
   * // windowsFs is a Windows FileSystem
   *
   * // The following assertion succeeds:
   * assertThat(windowsFs.getPath("c:\\foo")).isAbsolute();
   *
   * // The following assertions fail:
   * assertThat(windowsFs.getPath("foo\\bar")).isAbsolute();
   * assertThat(windowsFs.getPath("c:foo")).isAbsolute();
   * assertThat(windowsFs.getPath("\\foo\\bar")).isAbsolute();</code></pre>
   *
   * @return self
   *
   * @see Path#isAbsolute()
   */
  public SELF isAbsolute() {
    paths.assertIsAbsolute(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is relative (opposite to {@link Path#isAbsolute()}).
   *
   * Examples:
   * <pre><code class="java"> // unixFs is a Unix FileSystem
   *
   * // The following assertions succeed:
   * assertThat(unixFs.getPath("./foo/bar")).isRelative();
   * assertThat(unixFs.getPath("foo/bar")).isRelative();
   *
   * // The following assertion fails:
   * assertThat(unixFs.getPath("/foo/bar")).isRelative();
   *
   * // windowsFs is a Windows FileSystem
   *
   * // The following assertion succeeds:
   * assertThat(windowsFs.getPath("foo\\bar")).isRelative();
   * assertThat(windowsFs.getPath("c:foo")).isRelative();
   * assertThat(windowsFs.getPath("\\foo\\bar")).isRelative();
   *
   * // The following assertions fail:
   * assertThat(windowsFs.getPath("c:\\foo")).isRelative();</code></pre>
   *
   * @return self
   *
   * @see Path#isAbsolute()
   */
  public SELF isRelative() {
    paths.assertIsRelative(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is normalized.
   *
   * <p>
   * A path is normalized if it has no redundant components; typically, on both Unix and Windows, this means that the
   * path has no "self" components ({@code .}) and that its only parent components ({@code ..}), if any, are at the
   * beginning of the path.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   *
   * // the following assertions succeed:
   * assertThat(fs.getPath("/usr/lib")).isNormalized();
   * assertThat(fs.getPath("a/b/c")).isNormalized();
   * assertThat(fs.getPath("../d")).isNormalized();
   *
   * // the following assertions fail:
   * assertThat(fs.getPath("/a/./b")).isNormalized();
   * assertThat(fs.getPath("c/b/..")).isNormalized();
   * assertThat(fs.getPath("/../../e")).isNormalized();</code></pre>
   *
   * @return self
   */
  public SELF isNormalized() {
    paths.assertIsNormalized(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} is canonical by comparing it to its {@link Path#toRealPath(LinkOption...) real
   * path}.
   *
   * <p>
   * For Windows users, this assertion is no different than {@link #isAbsolute()} expect that the file must exist. For
   * Unix users, this assertion ensures that the tested path is the actual file system resource, that is, it is not a
   * {@link Files#isSymbolicLink(Path) symbolic link} to the actual resource, even if the path is absolute.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   * // Create a directory
   * final Path basedir = fs.getPath("/tmp/foo");
   * Files.createDirectories(basedir);
   *
   * // Create a file in this directory
   * final Path existingFile = basedir.resolve("existingFile");
   * Files.createFile(existingFile);
   *
   * // Create a symbolic link to that file
   * final Path symlinkToExistingFile = basedir.resolve("symlinkToExistingFile");
   * Files.createSymbolicLink(symlinkToExistingFile, existingFile);
   *
   * // The following assertion succeeds:
   * assertThat(existingFile).isCanonical();
   *
   * // The following assertion fails:
   * assertThat(symlinkToExistingFile).isCanonical();</code></pre>
   *
   * @return self
   * @throws PathsException an I/O error occurred while evaluating the path
   *
   * @see Path#toRealPath(LinkOption...)
   * @see Files#isSameFile(Path, Path)
   */
  public SELF isCanonical() {
    paths.assertIsCanonical(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} last element String representation is equal to the given filename.
   *
   * <p>
   * Note that the path does not need to exist to check its file name.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   * final Path file = fs.getPath("/foo/foo.txt");
   * final Path symlink = fs.getPath("/home/symlink-to-foo");
   * Files.createSymbolicLink(symlink, file);
   *
   * // the following assertions succeed:
   * assertThat(fs.getPath("/dir1/file.txt")).hasFileName("file.txt");
   * assertThat(fs.getPath("/dir1/dir2")).hasFileName("dir2");
   * // you can check file name on non existent paths
   * assertThat(file).hasFileName("foo.txt");
   * assertThat(symlink).hasFileName("symlink-to-foo");
   *
   * // the following assertions fail:
   * assertThat(fs.getPath("/dir1/file.txt").hasFileName("other.txt");
   * // fail because, last element is "."
   * assertThat(fs.getPath("/dir1/.")).hasFileName("dir1");
   * // fail because a link filename is not the same as its target filename
   * assertThat(symlink).hasFileName("file.txt");</code></pre>
   *
   * @param fileName the expected filename
   * @return self
   *
   * @throws NullPointerException if the given fileName is null.
   * @see Path#getFileName()
   */
  public SELF hasFileName(final String fileName) {
    paths.assertHasFileName(info, actual, fileName);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} has the expected parent path.
   *
   * <p>
   * <em>This assertion will perform canonicalization of the tested path and of the given argument before performing the test; see the class
   * description for more details. If this is not what you want, use {@link #hasParentRaw(Path)} instead.</em>
   * </p>
   *
   * <p>
   * Checks that the tested path has the given parent. This assertion will fail both if the tested path has no parent,
   * or has a different parent than what is expected.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   * final Path actual = fs.getPath("/dir1/dir2/file");
   *
   * // the following assertion succeeds:
   * assertThat(actual).hasParent(fs.getPath("/dir1/dir2/."));
   * // this one too as this path will be normalized to "/dir1/dir2":
   * assertThat(actual).hasParent(fs.getPath("/dir1/dir3/../dir2/."));
   *
   * // the following assertion fails:
   * assertThat(actual).hasParent(fs.getPath("/dir1"));</code></pre>
   *
   * @param expected the expected parent path
   * @return self
   *
   * @throws NullPointerException if the given parent path is null.
   * @throws PathsException failed to canonicalize the tested path or the path given as an argument
   *
   * @see Path#getParent()
   */
  public SELF hasParent(final Path expected) {
    paths.assertHasParent(info, actual, expected);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} has the expected parent path.
   *
   * <p>
   * <em>This assertion will not perform any canonicalization of either the tested path or the path given as an argument;
   * see class description for more details. If this is not what you want, use {@link #hasParent(Path)} instead.</em>
   * </p>
   *
   * <p>
   * This assertion uses {@link Path#getParent()} with no modification, which means the only criterion for this
   * assertion's success is the path's components (its root and its name elements).
   * </p>
   *
   * <p>
   * This may lead to surprising results if the tested path and the path given as an argument are not normalized. For
   * instance, if the tested path is {@code /home/foo/../bar} and the argument is {@code /home}, the assertion will
   * <em>fail</em> since the parent of the tested path is not {@code /home} but... {@code /home/foo/..}.
   * </p>
   *
   * <p>
   * While this may seem counterintuitive, it has to be recalled here that it is not required for a {@link FileSystem}
   * to consider that {@code .} and {@code ..} are name elements for respectively the current directory and the parent
   * directory respectively. In fact, it is not even required that a {@link FileSystem} be hierarchical at all.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   * final Path actual = fs.getPath("/dir1/dir2/file");
   *
   * // the following assertion succeeds:
   * assertThat(actual).hasParentRaw(fs.getPath("/dir1/dir2"));
   *
   * // the following assertions fails:
   * assertThat(actual).hasParent(fs.getPath("/dir1"));
   * // ... and this one too as expected path is not canonicalized.
   * assertThat(actual).hasParentRaw(fs.getPath("/dir1/dir3/../dir2"));</code></pre>
   *
   * @param expected the expected parent path
   * @return self
   *
   * @throws NullPointerException if the given parent path is null.
   *
   * @see Path#getParent()
   */
  public SELF hasParentRaw(final Path expected) {
    paths.assertHasParentRaw(info, actual, expected);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} has no parent.
   *
   * <p>
   * <em>This assertion will first canonicalize the tested path before performing the test; if this is not what you want, use {@link #hasNoParentRaw()} instead.</em>
   * </p>
   *
   * <p>
   * Check that the tested path, after canonicalization, has no parent. See the class description for more information
   * about canonicalization.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   *
   * // the following assertion succeeds:
   * assertThat(fs.getPath("/")).hasNoParent();
   * // this one too as path will be normalized to "/"
   * assertThat(fs.getPath("/usr/..")).hasNoParent();
   *
   * // the following assertions fail:
   * assertThat(fs.getPath("/usr/lib")).hasNoParent();
   * assertThat(fs.getPath("/usr")).hasNoParent();</code></pre>
   *
   * @return self
   *
   * @throws PathsException failed to canonicalize the tested path
   *
   * @see Path#getParent()
   */
  public SELF hasNoParent() {
    paths.assertHasNoParent(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} has no parent.
   *
   * <p>
   * <em>This assertion will not canonicalize the tested path before performing the test;
   * if this is not what you want, use {@link #hasNoParent()} instead.</em>
   * </p>
   *
   * <p>
   * As canonicalization is not performed, this means the only criterion for this assertion's success is the path's
   * components (its root and its name elements).
   * </p>
   *
   * <p>
   * This may lead to surprising results. For instance, path {@code /usr/..} <em>does</em> have a parent, and this
   * parent is {@code /usr}.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   *
   * // the following assertions succeed:
   * assertThat(fs.getPath("/")).hasNoParentRaw();
   * assertThat(fs.getPath("foo")).hasNoParentRaw();
   *
   * // the following assertions fail:
   * assertThat(fs.getPath("/usr/lib")).hasNoParentRaw();
   * assertThat(fs.getPath("/usr")).hasNoParentRaw();
   * // this one fails as canonicalization is not performed, leading to parent being /usr
   * assertThat(fs.getPath("/usr/..")).hasNoParent();</code></pre>
   *
   * @return self
   *
   * @see Path#getParent()
   */
  public SELF hasNoParentRaw() {
    paths.assertHasNoParentRaw(info, actual);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} has the given size in bytes.
   * <p>
   * Note that the actual {@link Path} must exist and be a regular file.
   * </p>
   *
   * Examples:
   * <pre><code class="java">
   * Path foxPath = Files.write(Paths.get("/fox.txt"), "The Quick Brown Fox.".getBytes());
   * 
   * // this assertion succeeds
   * assertThat(foxPath).hasSize(20);
   *
   * // this assertion fails
   * assertThat(foxPath).hasSize(3);</code></pre>
   *
   * @param expectedSize the expected {@code Path} file size in bytes
   * @return {@code this} assertion object
   * @throws AssertionError is the actual {@code Path} is {@code null}.
   * @throws AssertionError if the actual {@code Path} does not exist.
   * @throws AssertionError if the actual {@code Path} is not a regular file.
   * @throws AssertionError if the actual {@code Path} file size is not equal to the expected size.
   * @throws UncheckedIOException if any I/O error occurs.
   * @since 3.21.0
   */
  public SELF hasSize(long expectedSize) {
    paths.assertHasSize(info, actual, expectedSize);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} starts with the given path.
   *
   * <p>
   * <em>This assertion will perform canonicalization of both the tested path and the path given as an argument;
   * see class description for more details. If this is not what you want, use {@link #startsWithRaw(Path)} instead.</em>
   * </p>
   *
   * <p>
   * Checks that the given {@link Path} starts with another path. Note that the name components matter, not the string
   * representation; this means that, for example, {@code /home/foobar/baz} <em>does not</em> start with
   * {@code /home/foo}.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   * final Path tested = fs.getPath("/home/joe/myfile");
   *
   * // the following assertion succeeds:
   * assertThat(tested).startsWith(fs.getPath("/home"));
   * assertThat(tested).startsWith(fs.getPath("/home/"));
   * assertThat(tested).startsWith(fs.getPath("/home/."));
   * // assertion succeeds because this path will be canonicalized to "/home/joe"
   * assertThat(tested).startsWith(fs.getPath("/home/jane/../joe/."));
   *
   * // the following assertion fails:
   * assertThat(tested).startsWith(fs.getPath("/home/harry"));</code></pre>
   *
   * @param other the other path
   * @return self
   *
   * @throws NullPointerException if the given path is null.
   * @throws PathsException failed to canonicalize the tested path or the path given as an argument
   *
   * @see Path#startsWith(Path)
   * @see Path#toRealPath(LinkOption...)
   */
  public SELF startsWith(final Path other) {
    paths.assertStartsWith(info, actual, other);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} starts with the given path.
   *
   * <p>
   * <em>This assertions does not perform canonicalization on either the
   * tested path or the path given as an argument; see class description for
   * more details. If this is not what you want, use {@link #startsWith(Path)}
   * instead.</em>
   * </p>
   *
   * <p>
   * Checks that the given {@link Path} starts with another path, without performing canonicalization on its arguments.
   * This means that the only criterion to determine whether a path starts with another is the tested path's, and the
   * argument's, name elements.
   * </p>
   *
   * <p>
   * This may lead to some surprising results: for instance, path {@code /../home/foo} does <em>not</em> start with
   * {@code /home} since the first name element of the former ({@code ..}) is different from the first name element of
   * the latter ({@code home}).
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   * final Path tested = fs.getPath("/home/joe/myfile");
   *
   * // the following assertion succeeds:
   * assertThat(tested).startsWithRaw(fs.getPath("/home/joe"));
   *
   * // the following assertion fails:
   * assertThat(tested).startsWithRaw(fs.getPath("/home/harry"));
   * // .... and this one too as given path is not canonicalized
   * assertThat(tested).startsWithRaw(fs.getPath("/home/joe/.."));</code></pre>
   *
   * @param other the other path
   * @return self
   *
   * @throws NullPointerException if the given path is null.
   *
   * @see Path#startsWith(Path)
   */
  public SELF startsWithRaw(final Path other) {
    paths.assertStartsWithRaw(info, actual, other);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} ends with the given path.
   *
   * <p>
   * This assertion will attempt to canonicalize the tested path and normalize the path given as an argument before
   * performing the actual test.
   * </p>
   *
   * <p>
   * Note that the criterion to determine success is determined by the path's name elements; therefore,
   * {@code /home/foobar/baz} does <em>not</em> end with {@code bar/baz}.
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem.
   * // the current directory is supposed to be /home.
   * final Path tested = fs.getPath("/home/joe/myfile");
   * // as tested will be canonicalized, it could have been written: /home/jane/../joe/myfile
   *
   * // the following assertion succeeds:
   * assertThat(tested).endsWith(fs.getPath("joe/myfile"));
   *
   * // the following assertions fail:
   * assertThat(tested).endsWith(fs.getPath("joe/otherfile"));
   * // this path will be normalized to joe/otherfile
   * assertThat(tested).endsWith(fs.getPath("joe/myfile/../otherfile"));</code></pre>
   *
   * @param other the other path
   * @return self
   *
   * @throws NullPointerException if the given path is null.
   * @throws PathsException failed to canonicalize the tested path (see class
   *           description)
   *
   * @see Path#endsWith(Path)
   * @see Path#toRealPath(LinkOption...)
   */
  public SELF endsWith(final Path other) {
    paths.assertEndsWith(info, actual, other);
    return myself;
  }

  /**
   * Assert that the tested {@link Path} ends with the given path.
   *
   * <p>
   * <em>This assertion will not perform any canonicalization (on the
   * tested path) or normalization (on the path given as an argument); see the
   * class description for more details. If this is not what you want, use
   * {@link #endsWith(Path)} instead.</em>
   * </p>
   *
   * <p>
   * This may lead to some surprising results; for instance, path {@code /home/foo} does <em>not</em> end with
   * {@code foo/.} since the last name element of the former ({@code foo}) is different from the last name element of
   * the latter ({@code .}).
   * </p>
   *
   * Examples:
   * <pre><code class="java"> // fs is a Unix filesystem
   * // the current directory is supposed to be /home.
   * final Path tested = fs.getPath("/home/joe/myfile");
   *
   * // The following assertion succeeds:
   * assertThat(tested).endsWithRaw(fs.getPath("joe/myfile"));
   *
   * // But the following assertion fails:
   * assertThat(tested).endsWithRaw(fs.getPath("harry/myfile"));
   * // and this one too as the given path is not normalized
   * assertThat(tested).endsWithRaw(fs.getPath("harry/../joe/myfile"));</code></pre>
   *
   * @param other the other path
   * @return self
   *
   * @throws NullPointerException if the given path is null.
   *
   * @see Path#endsWith(Path)
   */
  public SELF endsWithRaw(final Path other) {
    paths.assertEndsWithRaw(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the tested {@link Path} digest (calculated with the specified {@link MessageDigest}) is equal to the given one.
   * <p>
   * Note that the {@link Path} must be readable.
   * <p>
   * Examples:
   * <pre><code class="java"> // fs is a filesystem
   * // assume that the current directory contains https://repo1.maven.org/maven2/org/assertj/assertj-core/2.9.0/assertj-core-2.9.0.jar.
   * Path tested = Paths.get("assertj-core-2.9.0.jar");
   *
   * // The following assertions succeed:
   * assertThat(tested).hasDigest(MessageDigest.getInstance("SHA1"), new byte[]{92, 90, -28, 91, 88, -15, 32, 35, -127, 122, -66, 73, 36, 71, -51, -57, -111, 44, 26, 44});
   * assertThat(tested).hasDigest(MessageDigest.getInstance("MD5"), new byte[]{-36, -77, 1, 92, -46, -124, 71, 100, 76, -127, 10, -13, 82, -125, 44, 25});
   *
   * // The following assertions fail:
   * assertThat(tested).hasDigest(MessageDigest.getInstance("SHA1"), "93b9ced2ee5b3f0f4c8e640e77470dab031d4cad".getBytes());
   * assertThat(tested).hasDigest(MessageDigest.getInstance("MD5"), "3735dff8e1f9df0492a34ef075205b8f".getBytes()); </code></pre>
   *
   * @param digest the MessageDigest used to calculate the digests.
   * @param expected the expected binary content to compare the actual {@code Path}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given algorithm is {@code null}.
   * @throws NullPointerException if the given digest is {@code null}.
   * @throws AssertionError       if the actual {@code Path} is {@code null}.
   * @throws AssertionError       if the actual {@code Path} does not exist.
   * @throws AssertionError       if the actual {@code Path} is not an file.
   * @throws AssertionError       if the actual {@code Path} is not readable.
   * @throws UncheckedIOException if any I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code Path}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(MessageDigest digest, byte[] expected) {
    paths.assertHasDigest(info, actual, digest, expected);
    return myself;
  }

  /**
   * Verifies that the tested {@link Path} digest (calculated with the specified {@link MessageDigest}) is equal to the given one.
   * <p>
   * Note that the {@link Path} must be readable.
   * <p>
   * Examples:
   * <pre><code class="java"> // fs is a filesystem
   * // assume that the current directory contains https://repo1.maven.org/maven2/org/assertj/assertj-core/2.9.0/assertj-core-2.9.0.jar.
   * Path tested = Paths.get("assertj-core-2.9.0.jar");
   *
   * // The following assertions succeed:
   * assertThat(tested).hasDigest(MessageDigest.getInstance("SHA1"), "5c5ae45b58f12023817abe492447cdc7912c1a2c");
   * assertThat(tested).hasDigest(MessageDigest.getInstance("MD5"), "dcb3015cd28447644c810af352832c19");
   *
   * // The following assertions fail:
   * assertThat(tested).hasDigest(MessageDigest.getInstance("SHA1"), "93b9ced2ee5b3f0f4c8e640e77470dab031d4cad");
   * assertThat(tested).hasDigest(MessageDigest.getInstance("MD5"), "3735dff8e1f9df0492a34ef075205b8f"); </code></pre>
   *
   * @param digest the MessageDigest used to calculate the digests.
   * @param expected the expected binary content to compare the actual {@code Path}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given algorithm is {@code null}.
   * @throws NullPointerException if the given digest is {@code null}.
   * @throws AssertionError       if the actual {@code Path} is {@code null}.
   * @throws AssertionError       if the actual {@code Path} does not exist.
   * @throws AssertionError       if the actual {@code Path} is not an file.
   * @throws AssertionError       if the actual {@code Path} is not readable.
   * @throws UncheckedIOException if any I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code Path}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(MessageDigest digest, String expected) {
    paths.assertHasDigest(info, actual, digest, expected);
    return myself;
  }

  /**
   * Verifies that the tested {@link Path} digest (calculated with the specified algorithm) is equal to the given one.
   * <p>
   * Note that the {@link Path} must be readable.
   * <p>
   * Examples:
   * <pre><code class="java"> // fs is a filesystem
   * // assume that the current directory contains https://repo1.maven.org/maven2/org/assertj/assertj-core/2.9.0/assertj-core-2.9.0.jar.
   * Path tested = Paths.get("assertj-core-2.9.0.jar");
   *
   * // The following assertions succeed:
   * assertThat(tested).hasDigest("SHA1", new byte[]{92, 90, -28, 91, 88, -15, 32, 35, -127, 122, -66, 73, 36, 71, -51, -57, -111, 44, 26, 44});
   * assertThat(tested).hasDigest("MD5", new byte[]{-36, -77, 1, 92, -46, -124, 71, 100, 76, -127, 10, -13, 82, -125, 44, 25});
   *
   * // The following assertions fail:
   * assertThat(tested).hasDigest("SHA1", "93b9ced2ee5b3f0f4c8e640e77470dab031d4cad".getBytes());
   * assertThat(tested).hasDigest("MD5", "3735dff8e1f9df0492a34ef075205b8f".getBytes()); </code></pre>
   *
   * @param algorithm the algorithm used to calculate the digests to compare.
   * @param expected the expected binary content to compare the actual {@code Path}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given algorithm is {@code null}.
   * @throws NullPointerException if the given digest is {@code null}.
   * @throws AssertionError       if the actual {@code Path} is {@code null}.
   * @throws AssertionError       if the actual {@code Path} does not exist.
   * @throws AssertionError       if the actual {@code Path} is not an file.
   * @throws AssertionError       if the actual {@code Path} is not readable.
   * @throws UncheckedIOException if any I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code Path}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(String algorithm, byte[] expected) {
    paths.assertHasDigest(info, actual, algorithm, expected);
    return myself;
  }

  /**
   * Verifies that the tested {@link Path} digest (calculated with the specified algorithm) is equal to the given one.
   * <p>
   * Note that the {@link Path} must be readable.
   * <p>
   * Examples:
   * <pre><code class="java"> // assume that assertj-core-2.9.0.jar was downloaded from https://repo1.maven.org/maven2/org/assertj/assertj-core/2.9.0/assertj-core-2.9.0.jar
   * Path tested = Paths.get("assertj-core-2.9.0.jar");
   *
   * // The following assertions succeed:
   * assertThat(tested).hasDigest("SHA1", "5c5ae45b58f12023817abe492447cdc7912c1a2c");
   * assertThat(tested).hasDigest("MD5", "dcb3015cd28447644c810af352832c19");
   *
   * // The following assertions fail:
   * assertThat(tested).hasDigest("SHA1", "93b9ced2ee5b3f0f4c8e640e77470dab031d4cad");
   * assertThat(tested).hasDigest("MD5", "3735dff8e1f9df0492a34ef075205b8f"); </code></pre>
   *
   * @param algorithm the algorithm used to calculate the digests.
   * @param expected the expected digest to compare the actual {@code Path}'s digest to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given algorithm is {@code null}.
   * @throws NullPointerException if the given digest is {@code null}.
   * @throws AssertionError       if the actual {@code Path} is {@code null}.
   * @throws AssertionError       if the actual {@code Path} does not exist.
   * @throws AssertionError       if the actual {@code Path} is not an file.
   * @throws AssertionError       if the actual {@code Path} is not readable.
   * @throws UncheckedIOException if any I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code Path}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(String algorithm, String expected) {
    paths.assertHasDigest(info, actual, algorithm, expected);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} is a directory containing at least one file matching the given {@code Predicate<Path>}.
   * <p>
   * Note that the actual {@link Path} must exist and be a directory.
   * <p>
   * Given the following directory structure:
   * <pre><code class="text"> /root/
   * /root/sub-dir-1/
   * /root/sub-dir-1/file-1.ext
   * /root/sub-dir-1/file-2.ext
   * /root/sub-file-1.ext
   * /root/sub-file-2.ext</code></pre>
   *
   * Here are some assertions examples:
   * <pre><code class="java"> Path root = Paths.get("root");
   *
   * // The following assertions succeed:
   * assertThat(root).isDirectoryContaining(path -&gt; path.getFileName().toString().startsWith("sub-dir"))
   *                 .isDirectoryContaining(path -&gt; path.getFileName().toString().startsWith("sub-file"))
   *                 .isDirectoryContaining(path -&gt; path.getFileName().toString().endsWith(".ext"))
   *                 .isDirectoryContaining(Files::isDirectory);
   *
   * // The following assertions fail:
   * assertThat(root).isDirectoryContaining(file -&gt; file.getFileName().toString().startsWith("dir"));
   * assertThat(root).isDirectoryContaining(file -&gt; file.getFileName().toString().endsWith(".bin")); </code></pre>
   *
   * @param filter the filter for files located inside {@code actual}'s directory.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given filter is {@code null}.
   * @throws AssertionError       if actual is {@code null}.
   * @throws AssertionError       if actual does not exist.
   * @throws AssertionError       if actual is not a directory.
   * @throws AssertionError       if actual does not contain any files matching the given predicate.
   * @since 3.13.0
   */
  public SELF isDirectoryContaining(Predicate<Path> filter) {
    paths.assertIsDirectoryContaining(info, actual, filter);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} is a directory containing at least one file matching the given {@code String}
   * interpreted as a path matcher (as per {@link FileSystem#getPathMatcher(String)}).
   * <p>
   * Note that the actual {@link Path} must exist and be a directory.
   * <p>
   * Given the following directory structure:
   * <pre><code class="text"> /root/
   * /root/sub-dir-1/
   * /root/sub-dir-1/file-1.ext
   * /root/sub-dir-1/file-2.ext
   * /root/sub-file-1.ext
   * /root/sub-file-2.ext</code></pre>
   *
   * Here are some assertions examples:
   * <pre><code class="java"> Path root = Paths.get("root");
   *
   * // The following assertions succeed:
   * assertThat(root).isDirectoryContaining("glob:**sub-dir*")
   *                 .isDirectoryContaining("glob:**sub-file*")
   *                 .isDirectoryContaining("glob:**.ext")
   *                 .isDirectoryContaining("regex:.*ext")
   *                 .isDirectoryContaining("glob:**.{ext,bin");
   *
   * // The following assertions fail:
   * assertThat(root).isDirectoryContaining("glob:**dir");
   * assertThat(root).isDirectoryContaining("glob:**.bin");
   * assertThat(root).isDirectoryContaining("glob:**.{java,class}"); </code></pre>
   *
   * @param syntaxAndPattern the syntax and pattern for {@link java.nio.file.PathMatcher} as described in {@link FileSystem#getPathMatcher(String)}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given syntaxAndPattern is {@code null}.
   * @throws AssertionError       if actual is {@code null}.
   * @throws AssertionError       if actual does not exist.
   * @throws AssertionError       if actual is not a directory.
   * @throws AssertionError       if actual does not contain any files matching the given path matcher.
   * @see FileSystem#getPathMatcher(String)
   * @since 3.13.0
   */
  public SELF isDirectoryContaining(String syntaxAndPattern) {
    paths.assertIsDirectoryContaining(info, actual, syntaxAndPattern);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} directory or any of its subdirectories (recursively) contains at least one file
   * matching the given {@code String} interpreted as a path matcher (as per {@link FileSystem#getPathMatcher(String)}).
   * <p>
   * That methods performs the same assertion as {@link #isDirectoryContaining(String syntaxAndPattern)}  but recursively.
   * <p>
   * Note that the actual {@link Path} must exist and be a directory.
   * <p>
   * Examples given the following directory structure:
   * <pre><code class="text"> root
   * |—— foo
   * |    |—— foobar
   * |         |—— foo-file-1.ext
   * |—— foo-file-2.ext</code>
   * </pre>
   *
   * <pre><code class="java"> Path root = Paths.get("root");
   *
   * // The following assertions succeed:
   * assertThat(root).isDirectoryRecursivelyContaining("glob:**foo")
   *                 .isDirectoryRecursivelyContaining("glob:**ooba*")
   *                 .isDirectoryRecursivelyContaining("glob:**file-1.ext")
   *                 .isDirectoryRecursivelyContaining("regex:.*file-2.*")
   *                 .isDirectoryRecursivelyContaining("glob:**.{ext,dummy}");
   *
   * // The following assertions fail:
   * assertThat(root).isDirectoryRecursivelyContaining("glob:**fooba");
   * assertThat(root).isDirectoryRecursivelyContaining("glob:**.bin");
   * assertThat(root).isDirectoryRecursivelyContaining("glob:**.{java,class}"); </code></pre>
   *
   * @param syntaxAndPattern the syntax and pattern for {@link java.nio.file.PathMatcher} as described in {@link FileSystem#getPathMatcher(String)}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given syntaxAndPattern is {@code null}.
   * @throws AssertionError       if actual is {@code null}.
   * @throws AssertionError       if actual does not exist.
   * @throws AssertionError       if actual is not a directory.
   * @throws AssertionError       if actual does not contain recursively any files matching the given path matcher.
   * @see FileSystem#getPathMatcher(String)
   * @since 3.16.0
   */
  public SELF isDirectoryRecursivelyContaining(String syntaxAndPattern) {
    paths.assertIsDirectoryRecursivelyContaining(info, actual, syntaxAndPattern);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} directory or any of its subdirectories (recursively) contains at least one file
   * matching the given {@code Predicate<Path>}.
   * <p>
   * That methods performs the same assertion as {@link #isDirectoryContaining(Predicate filter)}  but recursively.
   * <p>
   * Note that the actual {@link Path} must exist and be a directory.
   * <p>
   * Examples given the following directory structure:
   * <pre><code class="text"> root
   * |—— foo
   * |    |—— foobar
   * |         |—— foo-file-1.ext
   * |—— foo-file-2.ext</code>
   * </pre>
   *
   * Here are some assertions examples:
   * <pre><code class="java"> Path root = Paths.get("root");
   *
   * // The following assertions succeed:
   * assertThat(root).isDirectoryRecursivelyContaining(file -&gt; file.getName().startsWith("foo-file-1"))
   *                 .isDirectoryRecursivelyContaining(file -&gt; file.getName().endsWith("file-2.ext"))
   *                 .isDirectoryRecursivelyContaining(file -&gt; file.getName().equals("foo"))
   *                 .isDirectoryRecursivelyContaining(file -&gt; file.getParentFile().getName().equals("foo"))
   *
   * // The following assertions fail:
   * assertThat(root).isDirectoryRecursivelyContaining(file -&gt; file.getName().equals("foo-file-1"))
   * assertThat(root).isDirectoryRecursivelyContaining(file -&gt; file.getName().equals("foo/foobar")); </code></pre>
   *
   * @param filter the filter for files located inside {@code actual}'s directory.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given filter is {@code null}.
   * @throws AssertionError       if actual is {@code null}.
   * @throws AssertionError       if actual does not exist.
   * @throws AssertionError       if actual is not a directory.
   * @throws AssertionError       if actual does not contain recursively any files matching the given predicate.
   * @since 3.16.0
   */
  public SELF isDirectoryRecursivelyContaining(Predicate<Path> filter) {
    paths.assertIsDirectoryRecursivelyContaining(info, actual, filter);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} is a directory that does not contain any files matching the given {@code Predicate<Path>}.
   * <p>
   * Note that the actual {@link Path} must exist and be a directory.
   * <p>
   * Given the following directory structure:
   * <pre><code class="text"> /root/
   * /root/sub-dir-1/
   * /root/sub-dir-1/file-1.ext
   * /root/sub-dir-1/file-2.ext
   * /root/sub-file-1.ext
   * /root/sub-file-2.ext</code></pre>
   *
   * Here are some assertions examples:
   * <pre><code class="java"> Path root = Paths.get("root");
   *
   * // The following assertions succeed:
   * assertThat(root).isDirectoryNotContaining(file -&gt; file.getFileName().toString().startsWith("dir"))
   *                 .isDirectoryNotContaining(file -&gt; file.getFileName().toString().endsWith(".bin"));
   *
   * // The following assertions fail:
   * assertThat(root).isDirectoryNotContaining(path -&gt; path.getFileName().toString().startsWith("sub-dir"));
   * assertThat(root).isDirectoryNotContaining(path -&gt; path.getFileName().toString().startsWith("sub-file"));
   * assertThat(root).isDirectoryNotContaining(path -&gt; path.getFileName().toString().endsWith(".ext"));
   * assertThat(root).isDirectoryNotContaining(Files::isDirectory); </code></pre>
   *
   * @param filter the filter for files located inside {@code actual}'s directory.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given filter is {@code null}.
   * @throws AssertionError       if actual is {@code null}.
   * @throws AssertionError       if actual does not exist.
   * @throws AssertionError       if actual is not a directory.
   * @throws AssertionError       if actual contains a file matching the given predicate.
   * @since 3.13.0
   */
  public SELF isDirectoryNotContaining(Predicate<Path> filter) {
    paths.assertIsDirectoryNotContaining(info, actual, filter);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} is a directory that does not contain any files matching the given {@code String}
   * interpreted as a path matcher (as per {@link FileSystem#getPathMatcher(String)}).
   * <p>
   * Note that the actual {@link Path} must exist and be a directory.
   * <p>
   * Given the following directory structure:
   * <pre><code class="text"> /root/
   * /root/sub-dir-1/
   * /root/sub-dir-1/file-1.ext
   * /root/sub-dir-1/file-2.ext
   * /root/sub-file-1.ext
   * /root/sub-file-2.ext</code></pre>
   *
   * Here are some assertions examples:
   * <pre><code class="java"> Path root = Paths.get("root");
   *
   * // The following assertions succeed:
   * assertThat(root).isDirectoryNotContaining("glob:**dir")
   *                 .isDirectoryNotContaining("glob:**.bin")
   *                 .isDirectoryNotContaining("regex:.*bin")
   *                 .isDirectoryNotContaining("glob:**.{java,class}");
   *
   * // The following assertions fail:
   * assertThat(root).isDirectoryNotContaining("glob:**sub-dir*");
   * assertThat(root).isDirectoryNotContaining("glob:**sub-file*");
   * assertThat(root).isDirectoryNotContaining("glob:**.ext");
   * assertThat(root).isDirectoryNotContaining("regex:.*ext");
   * assertThat(root).isDirectoryNotContaining("glob:**.{ext,bin"); </code></pre>
   *
   * @param syntaxAndPattern the syntax and pattern for {@link java.nio.file.PathMatcher} as described in {@link FileSystem#getPathMatcher(String)}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given syntaxAndPattern is {@code null}.
   * @throws AssertionError       if actual is {@code null}.
   * @throws AssertionError       if actual does not exist.
   * @throws AssertionError       if actual is not a directory.
   * @throws AssertionError       if actual contains a file matching the given path matcher.
   * @see FileSystem#getPathMatcher(String)
   * @since 3.13.0
   */
  public SELF isDirectoryNotContaining(String syntaxAndPattern) {
    paths.assertIsDirectoryNotContaining(info, actual, syntaxAndPattern);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} is an empty directory.
   * <p>
   * Note that the actual {@link Path} must exist and be a directory.
   * <p>
   * Given the following directory structure:
   * <pre><code class="text"> /root/
   * /root/sub-dir-1/
   * /root/sub-dir-1/file-1.ext
   * /root/sub-dir-1/file-2.ext
   * /root/sub-dir-2/
   * /root/sub-file-1.ext
   * /root/sub-file-2.ext</code></pre>
   *
   * Here are some assertions examples:
   * <pre><code class="java"> Path root = Paths.get("root");
   *
   * // The following assertion succeeds:
   * assertThat(root.resolve("sub-dir-2")).isEmptyDirectory();
   *
   * // The following assertions fail:
   * assertThat(root).isEmptyDirectory();
   * assertThat(root.resolve("sub-dir-1")).isEmptyDirectory(); </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if actual does not exist.
   * @throws AssertionError if actual is not a directory.
   * @throws AssertionError if actual is not empty.
   * @since 3.13.0
   */
  public SELF isEmptyDirectory() {
    paths.assertIsEmptyDirectory(info, actual);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} is a non empty directory.
   * <p>
   * Note that the actual {@link Path} must exist and be a directory.
   * <p>
   * Given the following directory structure:
   * <pre><code class="text"> /root/
   * /root/sub-dir-1/
   * /root/sub-dir-1/file-1.ext
   * /root/sub-dir-1/file-2.ext
   * /root/sub-dir-2/
   * /root/sub-file-1.ext
   * /root/sub-file-2.ext</code></pre>
   *
   * Here are some assertions examples:
   * <pre><code class="java"> Path root = Paths.get("root");
   *
   * // The following assertions succeed:
   * assertThat(root).isNotEmptyDirectory();
   * assertThat(root.resolve("sub-dir-1")).isNotEmptyDirectory();
   *
   * // The following assertion fails:
   * assertThat(root.resolve("sub-dir-2")).isNotEmptyDirectory(); </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if actual does not exist.
   * @throws AssertionError if actual is not a directory.
   * @throws AssertionError if actual is empty.
   * @since 3.13.0
   */
  public SELF isNotEmptyDirectory() {
    paths.assertIsNotEmptyDirectory(info, actual);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} is an empty regular file.
   * <p>
   * Note that the actual {@link Path} must exist and be a regular file.
   * <p>
   * Given the following path structure:
   * <pre><code class="text">
   * /root/sub-dir-1/file-1.ext (no content)
   * /root/sub-dir-1/file-2.ext (content)</code></pre>
   *
   * Here are some assertions examples:
   * <pre><code class="java"> Path withoutContent = Paths.get("/root/sub-dir-1/file-1.ext");
   * Path withContent = Paths.get("/root/sub-dir-1/file-2.ext");
   *
   * // The following assertion succeeds:
   * assertThat(withoutContent).isEmptyFile();
   *
   * // The following assertion fails:
   * assertThat(withContent).isEmptyFile();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if actual does not exist.
   * @throws AssertionError if actual is not empty.
   * @since 3.19.0
   */
  public SELF isEmptyFile() {
    paths.assertIsEmptyFile(info, actual);
    return myself;
  }

  /**
   * Verify that the actual {@code Path} is a non-empty regular file.
   * <p>
   * Note that the actual {@link Path} must exist and be a regular file.
   * <p>
   * Given the following path structure:
   * <pre><code class="text">
   * /root/sub-dir-1/file-1.ext (no content)
   * /root/sub-dir-1/file-2.ext (content)</code></pre>
   *
   * Here are some assertions examples:
   * <pre><code class="java"> Path withoutContent = Paths.get("/root/sub-dir-1/file-1.ext");
   * Path withContent = Paths.get("/root/sub-dir-1/file-2.ext");
   *
   * // The following assertion succeeds:
   * assertThat(withContent).isNotEmptyFile();
   *
   * // The following assertion fails:
   * assertThat(withoutContent).isNotEmptyFile();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if actual does not exist.
   * @throws AssertionError if actual is empty.
   * @since 3.19.0
   */
  public SELF isNotEmptyFile() {
    paths.assertIsNotEmptyFile(info, actual);
    return myself;
  }

  /**
   * Returns {@code ByteArray} assertions on the content of the actual {@code Path} read.
   * <p>
   * Example:
   * <pre><code class='java'> Path xFile = Files.write(Paths.get("xfile.txt"), "The Truth Is Out There".getBytes());
   *
   * // assertion succeeds
   * assertThat(xFile).binaryContent().isEqualTo("The Truth Is Out There".getBytes());
   *
   * // assertion fails:
   * assertThat(xFile).binaryContent().isEqualTo("Elsewhere".getBytes());</code></pre>
   *
   * @return a {@link AbstractByteArrayAssert} object with the binary content of the path.
   * @throws AssertionError if the actual {@code Path} is not readable as per {@link Files#isReadable(Path)}.
   * @throws UncheckedIOException when failing to read the actual {@code Path}.
   */
  public AbstractByteArrayAssert<?> binaryContent() {
    paths.assertIsReadable(info, actual);
    return new ByteArrayAssert(readPath()).withAssertionState(myself);
  }

  /**
   * Returns String assertions on the content of the actual {@code Path} read with the {@link Charset#defaultCharset() default charset}.
   * <p>
   * Example:
   * <pre><code class='java'> Path xFile = Files.write(Paths.get("xfile.txt"), "The Truth Is Out There".getBytes());
   *
   * // assertion succeeds (default charset is used to read xFile content):
   * assertThat(xFile).content().startsWith("The Truth Is ");
   *
   * // assertion fails:
   * assertThat(xFile).content().contains("Elsewhere");</code></pre>
   *
   * @return a StringAssert object with the content of the actual {@code Path} read with the default {@link Charset}.
   * @throws AssertionError if the actual {@code Path} is not readable as per {@link Files#isReadable(Path)}.
   * @throws UncheckedIOException when failing to read the actual {@code Path}.
   * @since 3.21.0
   */
  public AbstractStringAssert<?> content() {
    // does not call content(Charset.defaultCharset()) to avoid double proxying in soft assertions.
    return internalContent(Charset.defaultCharset());
  }

  /**
   * Returns String assertions on the content of the actual {@code Path} read with the given {@link Charset}.
   * <p>
   * Example:
   * <pre><code class='java'> Path utf8Path = Files.write(Paths.get("utf8.txt"), "é à".getBytes());
   *
   * // assertion succeeds:
   * assertThat(utf8Path).content(StandardCharsets.UTF_8).endsWith("é à");
   *
   * // assertion fails:
   * assertThat(utf8Path).content(StandardCharsets.UTF_8).contains("e");</code></pre>
   *
   * @param charset the {@link Charset} to use to read the actual {@link Path}.
   * @return a {@link StringAssert} object with the content of the actual {@code Path} read with the default {@link Charset}.
   * @throws AssertionError if the actual {@code Path} is not readable as per {@link Files#isReadable(Path)}.
   * @throws UncheckedIOException when failing to read the actual {@code Path}.
   * @since 3.21.0
   */
  public AbstractStringAssert<?> content(Charset charset) {
    return internalContent(charset);
  }

  // this method was introduced to avoid double proxying in soft assertions for content()
  private AbstractStringAssert<?> internalContent(Charset charset) {
    paths.assertIsReadable(info, actual);
    String pathContent = readPath(charset);
    return new StringAssert(pathContent).withAssertionState(myself);
  }

  private byte[] readPath() {
    try {
      return readAllBytes(actual);
    } catch (IOException e) {
      throw new UncheckedIOException(format("Failed to read %s binary content", actual), e);
    }
  }

  private String readPath(Charset charset) {
    try {
      return new String(readAllBytes(actual), charset);
    } catch (IOException e) {
      throw new UncheckedIOException(format("Failed to read %s content with %s charset", actual, charset), e);
    }
  }

  /**
   * Verifies that the actual {@code Path} has given extension.
   * <p>
   * Example:
   * <pre><code class='java'> Path path = Paths.get(&quot;file.java&quot;);
   *
   * // assertion succeeds
   * assertThat(path).hasExtension(&quot;java&quot;);
   *
   * // assertion fails
   * assertThat(path).hasExtension(&quot;png&quot;);</code></pre>
   *
   * @param expectedExtension the expected extension, without the {@code '.'}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the expected extension is {@code null}.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @throws AssertionError if the actual {@code Path} is not a regular file.
   * @throws AssertionError if the actual {@code Path} does not have the expected extension.
   * @since 3.21.0
   */
  public SELF hasExtension(String expectedExtension) {
    paths.assertHasExtension(info, actual, expectedExtension);
    return myself;
  }

  /**
   * Verifies that the actual {@code Path} has no extension.
   *
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(Paths.get(&quot;file&quot;)).hasNoExtension();
   * assertThat(Paths.get(&quot;file.&quot;)).hasNoExtension();
   *
   * // assertion fails
   * assertThat(Paths.get(&quot;file.txt&quot;)).hasNoExtension();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Path} is {@code null}.
   * @throws AssertionError if the actual {@code Path} is not a regular file.
   * @throws AssertionError if the actual {@code Path} does have an extension.
   */
  public SELF hasNoExtension() {
    paths.assertHasNoExtension(info, actual);
    return myself;
  }

}
