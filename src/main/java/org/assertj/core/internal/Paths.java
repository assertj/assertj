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
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

import java.nio.file.LinkOption;
import java.nio.file.Path;

import static org.assertj.core.error.ShouldBeAbsolutePath.shouldBeAbsolutePath;

import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldBeSymbolicLink.shouldBeSymbolicLink;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldExistNoFollow.shouldExistNoFollow;
import static org.assertj.core.error.ShouldNotExist.shouldNotExist;

/**
 * Core assertion class for {@link Path} assertions
 */
public class Paths
{
    @VisibleForTesting
    public static final String IOERROR_FORMAT
        = "I/O error attempting to process assertion for path: <%s>";

    private static final Paths INSTANCE = new Paths();

    @VisibleForTesting
    Failures failures = Failures.instance();

    public static Paths instance()
    {
        return INSTANCE;
    }

    public void assertExists(final AssertionInfo info, final Path actual)
    {
        assertNotNull(info, actual);

        if (!java.nio.file.Files.exists(actual))
            throw failures.failure(info, shouldExist(actual));
    }

    public void assertExistsNoFollow(final AssertionInfo info,
        final Path actual)
    {
        assertNotNull(info, actual);

        if (!java.nio.file.Files.exists(actual, LinkOption.NOFOLLOW_LINKS))
            throw failures.failure(info, shouldExistNoFollow(actual));
    }

    public void assertNotExists(final AssertionInfo info, final Path actual)
    {
        assertNotNull(info, actual);
        if (!java.nio.file.Files.notExists(actual, LinkOption.NOFOLLOW_LINKS))
            throw failures.failure(info, shouldNotExist(actual));
    }

    public void assertIsRegularFile(final AssertionInfo info, final Path actual)
    {
        assertExists(info, actual);

        if (!java.nio.file.Files.isRegularFile(actual))
            throw failures.failure(info, shouldBeRegularFile(actual));
    }

    public void assertIsDirectory(final AssertionInfo info, final Path actual)
    {
        assertExists(info, actual);

        if (!java.nio.file.Files.isDirectory(actual))
            throw failures.failure(info, shouldBeDirectory(actual));
    }

    public void assertIsSymbolicLink(final AssertionInfo info,
        final Path actual)
    {
        assertExistsNoFollow(info, actual);

        if (!java.nio.file.Files.isSymbolicLink(actual))
            throw failures.failure(info, shouldBeSymbolicLink(actual));
    }

    public void assertIsAbsolute(final AssertionInfo info, final Path actual)
    {
        assertNotNull(info, actual);
        if (!actual.isAbsolute())
            throw failures.failure(info, shouldBeAbsolutePath(actual));
    }

    private static void assertNotNull(final AssertionInfo info,
        final Path actual)
    {
        Objects.instance().assertNotNull(info, actual);
    }
}
