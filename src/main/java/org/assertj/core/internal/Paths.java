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

import java.nio.file.Path;

import static org.assertj.core.error.ShouldExist.shouldExist;

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

    private static void assertNotNull(final AssertionInfo info,
        final Path actual)
    {
        Objects.instance().assertNotNull(info, actual);
    }
}
