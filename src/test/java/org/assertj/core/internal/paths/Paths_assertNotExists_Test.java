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
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.error.ShouldNotExist.shouldNotExist;
import static org.assertj.core.test.TestFailures.wasExpectingAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

public class Paths_assertNotExists_Test
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

    public static Path existing;
    public static Path symlink;
    public static Path nonExisting;

    @BeforeClass
    public static void initPaths()
        throws IOException
    {
        final FileSystem fs = resource.getFileSystem();

        existing = fs.getPath("/existing");
        Files.createFile(existing);

        nonExisting = fs.getPath("/nonExisting");

        symlink = fs.getPath("/symlink");
        Files.createSymbolicLink(symlink, nonExisting);
    }

    @Test
    public void should_fail_if_actual_is_null()
    {
        thrown.expectAssertionError(actualIsNull());
        paths.assertNotExists(info, null);
    }

    @Test
    public void should_fail_if_actual_exists()
    {
        try {
            paths.assertNotExists(info, existing);
            wasExpectingAssertionError();
        } catch (AssertionError e) {
            verify(failures).failure(info, shouldNotExist(existing));
        }
    }

    @Test
    public void should_fail_even_if_actual_is_dangling_symlink()
    {
        try {
            paths.assertNotExists(info, symlink);
            wasExpectingAssertionError();
        } catch (AssertionError e) {
            verify(failures).failure(info, shouldNotExist(symlink));
        }
    }

    @Test
    public void should_pass_if_actual_does_not_exist()
    {
        paths.assertNotExists(info, nonExisting);
    }
}
