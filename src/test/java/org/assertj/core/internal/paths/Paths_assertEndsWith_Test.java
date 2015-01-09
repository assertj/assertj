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
package org.assertj.core.internal.paths;

import org.assertj.core.internal.PathsBaseTest;
import org.assertj.core.util.PathsException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import static junit.framework.Assert.assertSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.assertj.core.error.ShouldEndWithPath.shouldEndWith;
import static org.assertj.core.test.TestFailures.wasExpectingAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("AutoBoxing")
public class Paths_assertEndsWith_Test
    extends PathsBaseTest
{
    @ClassRule
    public static FileSystemResource resource;

    static {
        try {
            resource = new FileSystemResource();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);

        }
    }

    public static Path nonExistingActual;
    public static Path nonExistingParent;
    public static Path existingActual;
    public static Path existingGoodParent;
    public static Path existingBadParent;

    @BeforeClass
    public static void initPaths()
    {
        final FileSystem fs = resource.getFileSystem();

        nonExistingActual = fs.getPath("/noActual");
        nonExistingParent = fs.getPath("/noParent");
    }

    private Path actual;
    private Path other;

    @Before
    public void initMocks()
    {
        actual = mock(Path.class);
        other = mock(Path.class);
    }

    @Test
    public void should_fail_if_actual_is_null()
    {
        thrown.expectAssertionError(actualIsNull());
        paths.assertEndsWith(info, null, other);
    }

    @Test
    public void should_fail_if_other_is_null()
    {
        try {
            paths.assertEndsWith(info, actual, null);
            fail("expected a NullPointerException here");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "other should not be null");
        }
    }

    @Test
    public void should_fail_with_PathsException_if_actual_cannot_resolve()
        throws IOException
    {
        final IOException exception = new IOException();
        when(actual.toRealPath()).thenThrow(exception);

        try {
            paths.assertEndsWith(info, actual, other);
            fail("expected a PathsException here");
        } catch (PathsException e) {
            assertEquals("cannot resolve actual path", e.getMessage());
            assertSame(exception, e.getCause());
        }
    }

    @Test
    public void should_fail_if_canonical_actual_does_not_end_with_normalized_other()
        throws IOException
    {
        final Path canonicalActual = mock(Path.class);
        final Path normalizedOther = mock(Path.class);

        when(actual.toRealPath()).thenReturn(canonicalActual);
        when(other.normalize()).thenReturn(normalizedOther);

        // This is the default, but...
        when(canonicalActual.endsWith(normalizedOther)).thenReturn(false);

        try {
            paths.assertEndsWith(info, actual, other);
            wasExpectingAssertionError();
        } catch (AssertionError e) {
            verify(failures).failure(info, shouldEndWith(actual, other));
        }
    }

    @Test
    public void should_succeed_if_canonical_actual_ends_with_normalized_other()
        throws IOException
    {
        final Path canonicalActual = mock(Path.class);
        final Path normalizedOther = mock(Path.class);

        when(actual.toRealPath()).thenReturn(canonicalActual);
        when(other.normalize()).thenReturn(normalizedOther);

        // We want the canonical versions to be compared, not the arguments
        when(canonicalActual.endsWith(normalizedOther)).thenReturn(true);

        paths.assertEndsWith(info, actual, other);
    }
}
