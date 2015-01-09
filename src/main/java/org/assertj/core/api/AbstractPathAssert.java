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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.internal.Paths;
import org.assertj.core.util.PathsException;
import org.assertj.core.util.VisibleForTesting;

import java.nio.file.ClosedFileSystemException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.spi.FileSystemProvider;

/**
 * Assertions for {@link Path} objects
 *
 * <p>Note that some assertions have two versions: a normal one and a "raw" one
 * (for instance, {@code hasParent()} and {@code hasParentRaw()}. The difference
 * is that normal assertions will {@link Path#toRealPath(LinkOption...)
 * canonicalize} or {@link Path#normalize() normalize} the tested path and,
 * where applicable, the path argument, before performing the actual test.
 * Canonicalization includes normalization.</p>
 *
 * <p>Canonicalization may lead to an I/O error if a path does not exist, in
 * which case the given assertions will fail with a {@link PathsException}. Also
 * note that {@link Files#isSymbolicLink(Path) symbolic links} will be followed
 * if the filesystem supports them. Finally, if a path is not {@link
 * Path#isAbsolute()} absolute}, canonicalization will resolve the path against
 * the process' current working directory.</p>
 *
 * <p>These assertions are filesystem independent. You may use them on {@code
 * Path} instances issued from the default filesystem (ie, instances you get
 * when using {@link java.nio.file.Paths#get(String, String...)}) or from other
 * filesystems. For more information, see the {@link FileSystem javadoc for
 * {@code FileSystem}}.</p>
 *
 * <p>Furthermore:</p>
 *
 * <ul>
 *     <li>Unless otherwise noted, assertions which accept arguments will not
 *     accept {@code null} arguments; if a null argument is passed, these
 *     assertions will throw a {@link NullPointerException}.</li>
 *     <li>It is the caller's responsibility to ensure that paths used in
 *     assertions are issued from valid filesystems which are not {@link
 *     FileSystem#close() closed}. If a filesystems is closed, assertions will
 *     throw a {@link ClosedFileSystemException}.</li>
 *     <li>Some assertions take another {@link Path} as an argument. If this
 *     path is not issued from the same {@link FileSystemProvider provider} as
 *     the tested path, assertions will throw a {@link
 *     ProviderMismatchException}.</li>
 *     <li>Some assertions may need to perform I/O on the path's underlying
 *     filesystem; if an I/O error occurs when accessing the filesystem, these
 *     assertions will throw a {@link PathsException}.</li>
 * </ul>
 *
 * @param <S> self type
 *
 * @see Path
 * @see java.nio.file.Paths#get(String, String...)
 * @see FileSystem
 * @see FileSystem#getPath(String, String...)
 * @see FileSystems#getDefault()
 * @see Files
 */
public abstract class AbstractPathAssert<S extends AbstractPathAssert<S>>
    extends AbstractAssert<S, Path>
{
    @VisibleForTesting
    protected Paths paths = Paths.instance();

    protected AbstractPathAssert(final Path actual, final Class<?> selfType)
    {
        super(actual, selfType);
    }

    /**
     * Assert that a given path exists
     *
     * <p><strong>Note that this assertion will follow symbolic links before
     * asserting the path's existence.</strong></p>
     *
     * <p>On Windows system, this has no influence. On Unix systems, this means
     * the assertion result will fail if the path is a symbolic link whose
     * target does not exist. If you want to assert the existence of the
     * symbolic link itself, use {@link #existsNoFollow()} instead.</p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // fs is a Unix filesystem
     * // Create a symbolic link whose target does not exist
     * final Path nonExistent = fs.getPath("nonexistent");
     * final Path dangling = fs.getPath("dangling");
     * Files.createSymbolicLink(dangling, nonExistent);
     *
     * // Create a regular file, and a symbolic link pointing to it
     * final Path someFile = fs.getPath("somefile");
     * Files.createFile(someFile);
     * final Path symlink = fs.getPath("symlink");
     * Files.createSymbolicLink(symlink, someFile);
     *
     * // The following assertion succeeds
     * assertThat(symlink).exists();
     *
     * // The following assertion fails
     * assertThat(dangling).exists();
     * </code></pre>
     *
     * @return self
     *
     * @see Files#exists(Path, LinkOption...)
     */
    public S exists()
    {
        paths.assertExists(info, actual);
        return myself;
    }

    /**
     * Assert that a given path exists, not following symbolic links
     *
     * <p>This assertion behaves like {@link #exists()}, with the difference
     * that it can be used to assert the existence of a symbolic link even if
     * its target is invalid.</p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // fs is a Unix filesystem
     * // Create a symbolic link whose target does not exist
     * final Path nonExistent = fs.getPath("nonexistent");
     * final Path dangling = fs.getPath("dangling");
     * Files.createSymbolicLink(dangling, nonExistent);
     *
     * // The following assertions succeed
     * assertThat(symlink).existsNoFollow();
     * assertThat(dangling).existsNoFollow();
     * </code></pre>
     *
     * @return self
     *
     * @see Files#exists(Path, LinkOption...)
     */
    public S existsNoFollow()
    {
        paths.assertExistsNoFollow(info, actual);
        return myself;
    }

    /**
     * Assert that a given path does not exist
     *
     * <p><strong>IMPORTANT NOTE:</strong> this method will NOT follow symbolic
     * links (provided that the underlying {@link FileSystem} of this path
     * supports symbolic links at all).</p>
     *
     * <p>This means that even if the path to test is a symbolic link whose
     * target does not exist, this assertion will consider that the path exists
     * (note that this is unlike the default behavior of {@link #exists()}).</p>
     *
     * <p>If you are a Windows user, the above does not apply to you; if you are
     * a Unix user however, this is important. Consider the following:</p>
     *
     * <pre><code class="java">
     * // fs is a FileSystem
     * // Create a symbolic link "foo" whose nonexistent target is "bar"
     * final Path foo = fs.getPath("foo");
     * final Path bar = fs.getPath("bar");
     * Files.createSymbolicLink(foo, bar);
     *
     * // The following assertion fails:
     * assertThat(foo).doesNotExist();
     * </code></pre>
     *
     * @return self
     *
     * @see Files#notExists(Path, LinkOption...)
     * @see LinkOption#NOFOLLOW_LINKS
     */
    public S doesNotExist()
    {
        paths.assertNotExists(info, actual);
        return myself;
    }

    /**
     * Assert that a given path is a regular file
     *
     * <p><strong>Note that this method will follow symbolic links.</strong> If
     * you are a Unix user and wish to assert that a path is a symbolic link
     * instead, use {@link #isSymbolicLink()}.</p>
     *
     * <p>This assertion first asserts the existence of the path (using {@link
     * #exists()}) then checks whether the path is a regular file.</p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // fs is a Unix filesystem
     *
     * // Create a regular file, and a symbolic link to that regular file
     * final Path regularFile = fs.getPath("regularFile");
     * final Path symlink = fs.getPath("symlink");
     * Files.createFile(regularFile);
     * Files.createSymbolicLink(symlink, regularFile);
     *
     * // Create a directory, and a symbolic link to that directory
     * final Path dir = fs.getPath("dir");
     * final Path dirSymlink = fs.getPath("dirSymlink");
     * Files.createDirectories(dir);
     * Files.createSymbolicLink(dirSymlink, dir);
     *
     * // Create a nonexistent entry, and a symbolic link to that entry
     * final Path nonExistent = fs.getPath("nonexistent");
     * final Path dangling = fs.getPath("dangling");
     * Files.createSymbolicLink(dangling, nonExistent);
     *
     * // the following assertions fail because paths do not exist:
     * assertThat(nonExistent).isRegularFile();
     * assertThat(dangling).isRegularFile();
     *
     * // the following assertions fail because paths exist but are not regular
     * // files:
     * assertThat(dir).isRegularFile();
     * assertThat(dirSymlink).isRegularFile();
     *
     * // the following assertions succeed:
     * assertThat(regularFile).isRegularFile();
     * assertThat(symlink).isRegularFile();
     * </code></pre>
     *
     * @return self
     */
    public S isRegularFile()
    {
        paths.assertIsRegularFile(info, actual);
        return myself;
    }

    /**
     * Assert that a given path is a directory
     *
     * <p><strong>Note that this method will follow symbolic links.</strong> If
     * you are a Unix user and wish to assert that a path is a symbolic link
     * instead, use {@link #isSymbolicLink()}.</p>
     *
     * <p>This assertion first asserts the existence of the path (using {@link
     * #exists()}) then checks whether the path is a directory.</p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // fs is a Unix filesystem
     *
     * // Create a regular file, and a symbolic link to that regular file
     * final Path regularFile = fs.getPath("regularFile");
     * final Path symlink = fs.getPath("symlink");
     * Files.createFile(regularFile);
     * Files.createSymbolicLink(symlink, regularFile);
     *
     * // Create a directory, and a symbolic link to that directory
     * final Path dir = fs.getPath("dir");
     * final Path dirSymlink = fs.getPath("dirSymlink");
     * Files.createDirectories(dir);
     * Files.createSymbolicLink(dirSymlink, dir);
     *
     * // Create a nonexistent entry, and a symbolic link to that entry
     * final Path nonExistent = fs.getPath("nonexistent");
     * final Path dangling = fs.getPath("dangling");
     * Files.createSymbolicLink(dangling, nonExistent);
     *
     * // the following assertions fail because paths do not exist:
     * assertThat(nonExistent).isRegularFile();
     * assertThat(dangling).isRegularFile();
     *
     * // the following assertions fail because paths exist but are not
     * // directories:
     * assertThat(regularFile).isRegularFile();
     * assertThat(symlink).isRegularFile();
     *
     * // the following assertions succeed:
     * assertThat(dir).isRegularFile();
     * assertThat(dirSymlink).isRegularFile();
     * </code></pre>
     *
     * @return self
     */
    public S isDirectory()
    {
        paths.assertIsDirectory(info, actual);
        return myself;
    }

    /**
     * Assert that a given path is a symbolic link
     *
     * <p>This assertion first asserts the existence of the path (using {@link
     * #existsNoFollow()}) then checks whether the path is a symbolic link.</p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // fs is a Unix filesystem
     *
     * // Create a regular file, and a symbolic link to that regular file
     * final Path regularFile = fs.getPath("regularFile");
     * final Path symlink = fs.getPath("symlink");
     * Files.createFile(regularFile);
     * Files.createSymbolicLink(symlink, regularFile);
     *
     * // Create a directory, and a symbolic link to that directory
     * final Path dir = fs.getPath("dir");
     * final Path dirSymlink = fs.getPath("dirSymlink");
     * Files.createDirectories(dir);
     * Files.createSymbolicLink(dirSymlink, dir);
     *
     * // Create a nonexistent entry, and a symbolic link to that entry
     * final Path nonExistent = fs.getPath("nonexistent");
     * final Path dangling = fs.getPath("dangling");
     * Files.createSymbolicLink(dangling, nonExistent);
     *
     * // the following assertion fails because the path does not exist:
     * assertThat(nonExistent).isSymbolicLink();
     *
     * // the following assertions fail because paths exist but are not symbolic
     * // links
     * assertThat(regularFile).isSymbolicLink();
     * assertThat(dirSymlink).isSymbolicLink();
     *
     * // the following assertions succeed:
     * assertThat(symlink).isSymbolicLink();
     * assertThat(dangling).isSymbolicLink();
     * assertThat(dir).isSymbolicLink();
     * </code></pre>
     *
     * @return self
     */
    public S isSymbolicLink()
    {
        paths.assertIsSymbolicLink(info, actual);
        return myself;
    }

    /**
     * Assert that a given path is absolute
     *
     * <p>Note that the fact that a path is absolute does not mean that it is
     * {@link Path#normalize() normalized}: {@code /foo/..} is absolute, for
     * instance, but it is not normalized.</p>
     *
     * <p>Sample use:</p>
     *
     * <pre><code class="java">
     * // unixFs is a Unix FileSystem
     * final Path path1 = unixFs.getPath("/foo/bar");
     * final Path path2 = unixFs.getPath("foo/bar");
     *
     * // The following assertion succeeds:
     * assertThat(path1).isAbsolute();
     *
     * // The following assertion fails:
     * assertThat(path2).isAbsolute();
     *
     * // windowsFs is a Windows FileSystem
     * final Path path3 = windowsFs.getPath("c:\\foo");
     * final Path path4 = windowsFs.getPath("foo\\bar");
     * final Path path5 = windowsFs.getPath("c:foo");
     * final Path path6 = windowsFs.getPath("\\foo\\bar");
     *
     * // The following assertion succeeds:
     * assertThat(path3).isAbsolute();
     *
     * // The following assertion fails...
     * assertThat(path4).isAbsolute();
     *
     * // ... And these one also fail!
     * assertThat(path5).isAbsolute();
     * assertThat(path6).isAbsolute();
     * </code></pre>
     *
     * @return self
     *
     * @see Path#isAbsolute()
     * @see Path#toRealPath(LinkOption...)
     */
    public S isAbsolute()
    {
        paths.assertIsAbsolute(info, actual);
        return myself;
    }

    /**
     * Assert that a given {@link Path} is normalized
     *
     * <p>A path is normalized if it has no redundant components; typically, on
     * both Unix and Windows, this means that the path has no "self" components
     * ({@code .}) and that its only parent components ({@code ..}), if any, are
     * at the beginning of the path.</p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // fs is a Unix filesystem
     * final Path normalized1 = fs.getPath("/usr/lib");
     * final Path normalized2 = fs.getPath("/../../e");
     * final Path normalized3 = fs.getPath("a/b/c");
     * final Path normalized4 = fs.getPath("../d");
     * final Path notNormalized1 = fs.getPath("/a/./b");
     * final Path notNormalized2 = fs.getPath("c/..");
     *
     * // the following assertions succeed:
     * assertThat(normalized1).isNormalized();
     * assertThat(normalized2).isNormalized();
     * assertThat(normalized3).isNormalized();
     * assertThat(normalized4).isNormalized();
     *
     * // the following assertions fail:
     * assertThat(notNormalized1).isNormalized();
     * assertThat(notNormalized2).isNormalized();
     * </code></pre>
     *
     * @return self
     */
    public S isNormalized()
    {
        paths.assertIsNormalized(info, actual);
        return myself;
    }

    /**
     * Assert whether the tested path is canonical
     *
     * <p>For Windows users, this assertion is no different than {@link
     * #isAbsolute()}. For Unix users, this assertion ensures that the tested
     * path is the actual file system resource, that is, it is not a {@link
     * Files#isSymbolicLink(Path) symbolic link} to the actual resource, even if
     * the path is absolute.</p>
     *
     * <p>Example:</p>
     *
     * <pre><code class="java">
     * // fs is a Unix filesystem
     * // Create a directory
     * final Path basedir = fs.getPath("/tmp/foo");
     * Files.createDirectories(basedir);
     * // Create a file in this directory
     * final Path file = basedir.resolve("file");
     * Files.createFile(file);
     * // Create a symbolic link to that file
     * final Path symlink = basedir.resolve("symlink");
     * Files.createSymbolicLink(symlink, file);
     *
     * // The following assertion succeeds
     * assertThat(file).isCanonical();
     *
     * // The following assertion fails
     * assertThat(symlink).isCanonical();
     * </code></pre>
     *
     * @throws PathsException an I/O error occurred while evaluating the path
     *
     * @see Path#toRealPath(LinkOption...)
     * @see Files#isSameFile(Path, Path)
     */
    public S isCanonical()
    {
        paths.assertIsCanonical(info, actual);
        return myself;
    }

    /**
     * Assert whether the tested path has the expected parent path
     *
     * <p><em>This assertion will perform canonicalization of the tested path
     * and of the given argument before performing the test; see the class
     * description for more details. If this is not what you want, use {@link
     * #hasParentRaw(Path)} instead.</em></p>
     *
     * <p>Checks that the tested path has the given parent. This assertion will
     * fail both if the tested path has no parent, or has a different parent
     * than what is expected.</p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // fs is a Unix filesystem
     * final Path actual = fs.getPath("/dir1/dir2/file");
     * // this path will be normalized to "/dir1/dir2"
     * final Path good = fs.getPath("/dir1/dir3/../dir2/.");
     * final Path bad = fs.getPath("/dir1");
     *
     * // the following assertion succeeds
     * assertThat(actual).hasParent(good);
     *
     * // the following assertion fails
     * assertThat(actual).hasParent(bad);
     * </code></pre>
     *
     * @param expected the expected parent path
     * @return self
     *
     * @see Path#getParent()
     */
    public S hasParent(final Path expected)
    {
        paths.assertHasParent(info, actual, expected);
        return myself;
    }

    /**
     * Assert whether the tested path has the expected parent path
     *
     * <p><em>This assertion will not perform any canonicalization of either the
     * tested path or the path given as an argument; see class description for
     * more details. If this is not what you want, use {@link #hasParent(Path)}
     * instead.</em></p>
     *
     * <p>This assertion uses {@link Path#getParent()} with no modification,
     * which means the only criterion for this assertion's success is the path's
     * components (its root and its name elements).</p>
     *
     * <p>This may lead to surprising results if the tested path and the path
     * given as an argument are not normalized. For instance, if the tested
     * path is {@code /home/foo/../bar} and the argument is {@code /home}, the
     * assertion will <em>fail</em> since the parent of the tested path is not
     * {@code /home} but... {@code /home/foo/..}.</p>
     *
     * <p>While this may seem counterintuitive, it has to be recalled here that
     * it is not required for a {@link FileSystem} to consider that {@code .}
     * and {@code ..} are name elements for respectively the current directory
     * and the parent directory respectively. In fact, it is not even required
     * that a {@link FileSystem} be hierarchical at all.</p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // fs is a Unix filesystem
     * final Path actual = fs.getPath("/dir1/dir2/file");
     * final Path good = fs.getPath("/dir1/dir2");
     * final Path bad = fs.getPath("/dir1/dir3/../dir2");
     *
     * // the following assertion succeeds
     * assertThat(actual).hasParentRaw(good);
     *
     * // the following assertion fails
     * assertThat(actual).hasParentRaw(bad);
     * </code></pre>
     *
     * @param expected the expected parent path
     * @return self
     *
     * @see Path#getParent()
     */
    public S hasParentRaw(final Path expected)
    {
        paths.assertHasParentRaw(info, actual, expected);
        return myself;
    }

    /**
     * Assert whether the tested path has no parent
     *
     * <p><em>This assertion will first canonicalize the tested path before
     * performing the test; if this is not what you want, use {@link
     * #hasNoParentRaw()} instead.</em></p>
     *
     * <p>Check that the tested path, after canonicalization, has no parent. See
     * the class description for more information about canonicalization.</p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // unixFs is a Unix filesystem
     * final Path path1 = fs.getPath("/usr");
     * // this path will be normalized to "/"
     * final Path path2 = fs.getPath("/foo/..");
     * final Path path3 = fs.getPath("/usr/lib");
     *
     * // the following assertions succeed
     * assertThat(path1).hasNoParent();
     * assertThat(path2).hasNoParent();
     *
     * // the following assertion fails
     * assertThat(path3).hasNoParent();
     * </code></pre>
     *
     * @return self
     *
     * @throws PathsException failed to canonicalize the tested path
     *
     * @see Path#getParent()
     */
    public S hasNoParent()
    {
        paths.assertHasNoParent(info, actual);
        return myself;
    }

    /**
     * Assert whether the tested path has no parent
     *
     * <p><em>This assertion will not canonicalize the tested path before
     * performing the test; if this is not what you want, use {@link
     * #hasNoParent()} instead.</em></p>
     *
     * <p>As canonicalization is not performed, this means the only criterion
     * for this assertion's success is the path's components (its root and its
     * name elements).</p>
     *
     * <p>This may lead to surprising results. For instance, path {@code
     * /usr/..} <em>does</em> have a parent, and this parent is {@code /usr}.
     * </p>
     *
     * <p>Examples:</p>
     *
     * <pre><code class="java">
     * // unixFs is a Unix filesystem
     * final Path path1 = fs.getPath("/usr");
     * final Path path2 = fs.getPath("/");
     * final Path path3 = fs.getPath("foo");
     * final Path path4 = fs.getPath("/usr/lib");
     *
     * // the following assertions succeed
     * assertThat(path1).hasNoParentRaw();
     * assertThat(path2).hasNoParentRaw();
     * assertThat(path3).hasNoParentRaw();
     *
     * // the following assertion fails
     * assertThat(path4).hasNoParentRaw();
     * </code></pre>
     *
     * @return self
     *
     * @see Path#getParent()
     */
    public S hasNoParentRaw()
    {
        paths.assertHasNoParentRaw(info, actual);
        return myself;
    }
}
