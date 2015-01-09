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
}
