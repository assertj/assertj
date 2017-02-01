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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;

/**
 * Base test class for {@link Path} instances
 *
 * <p>
 * Assertion on {@link Path} objects are of two categories:
 * </p>
 *
 * <ul>
 * <li>assertions on the {@link Path} object themselves: those do not require a {@link FileSystem};</li>
 * <li>assertions inducing filesystem I/O: those <em>do</em> require a {@link FileSystem}.</li>
 * </ul>
 *
 * <p>
 * An advantage when compared with {@link File} is that we do not need to pollute the developer's machine with temporary
 * files/directories/etc for I/O bound tests; all that is required is a filesystem implementation.
 * </p>
 *
 * <p>
 * <a href="https://github.com/marschall/memoryfilesystem">memoryfilesystem</a> is chosen for its great support of
 * getting/setting file attributes etc, and for its emulation of both Unix and Windows filesystems.
 * </p>
 *
 * @see Path
 * @see FileSystem
 * @see Files
 */
public abstract class PathsBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Failures failures;
  protected Paths paths;
  protected NioFilesWrapper nioFilesWrapper;
  protected AssertionInfo info;

  protected Diff diff;
  protected BinaryDiff binaryDiff;

  @Before
  public void setUp() {
	failures = spy(new Failures());
	nioFilesWrapper = mock(NioFilesWrapper.class);
	paths = new Paths(nioFilesWrapper);
	paths.failures = failures;
	info = someInfo();
	diff = mock(Diff.class);
	paths.diff = diff;
	binaryDiff = mock(BinaryDiff.class);
	paths.binaryDiff = binaryDiff;
  }

  /**
   * A {@link FileSystem} for test classes which need them
   *
   * <p>
   * For test classes which do need a filesystem to test assertions and not only paths, declare a {@code static}
   * instance field of this class as a {@link ClassRule} and initialize at declaration time.
   * </p>
   */
  public static class FileSystemResource extends ExternalResource {

	private final FileSystem fs;

	public FileSystemResource() {
	  try {
		fs = MemoryFileSystemBuilder.newLinux().build("PathsTest");
	  } catch (IOException e) {
		throw new RuntimeException("failed to initialize filesystem", e);
	  }
	}

	public FileSystem getFileSystem() {
	  return fs;
	}

	@Override
	protected void after() {
	  try {
		fs.close();
	  } catch (IOException e) {
		throw new RuntimeException("failed to close filesystem", e);
	  }
	}
  }
}