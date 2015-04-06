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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

import static org.assertj.core.util.Objects.areEqual;

import java.net.URI;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.internal.Comparables.assertNotNull;

/**
 * Core assertion class for {@link java.net.URI} assertions
 */
public class Uris {

    private static final Uris INSTANCE = new Uris();

    @VisibleForTesting
    Failures failures = Failures.instance();

    public static Uris instance() {
        return INSTANCE;
    }

    Uris() {
    }

    /**
     * Verifies that actual {@code URI}s has the same scheme as expected.
     *
     * @param info     contains information about the assertion.
     * @param actual   the actual {@code URI}.
     * @param expected the given {@code String}.
     * @throws AssertionError       if the given {@code String} is {@code null}.
     * @throws AssertionError       if the actual {@code URI} has not the given scheme {@code String}.
     */
    public void assertHasScheme(final AssertionInfo info, final URI actual, final String expected) {
        assertNotNull(info, actual);
        String scheme = actual.getScheme();
        if (!areEqual(scheme, expected))
            throw failures.failure(info, shouldBeEqual(scheme, expected, info.representation()));
    }

    /**
     * Verifies that actual {@code URI}s has the same path as expected.
     *
     * @param info     contains information about the assertion.
     * @param actual   the actual {@code URI}.
     * @param expected the given {@code String}.
     * @throws AssertionError       if the given {@code String} is {@code null}.
     * @throws AssertionError       if the actual {@code URI} has not the given path {@code String}.
     */
    public void assertHasPath(AssertionInfo info, URI actual, String expected) {
        assertNotNull(info, actual);
        String path = actual.getPath();
        if (!path.equals(expected))
            throw failures.failure(info, shouldBeEqual(path, expected, info.representation()));
    }
}
