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
package org.assertj.core.error;

import java.io.File;
import java.nio.file.Path;

import org.assertj.core.util.VisibleForTesting;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link File} or {@link Path} is an
 * existing directory failed
 * 
 * @author Yvonne Wang
 * @author Francis Galiegue
 */
public class ShouldBeDirectory extends BasicErrorMessageFactory
{
  @VisibleForTesting
  public static final String PATH_SHOULD_BE_DIRECTORY = "%nExpecting path:%n  <%s>%nto be a directory.";

  @VisibleForTesting
  public static final String FILE_SHOULD_BE_DIRECTORY = "%nExpecting file:%n  <%s>%n to be an existing directory.";

  public static ErrorMessageFactory shouldBeDirectory(final Path actual) {
	return new ShouldBeDirectory(actual);
  }

  public static ErrorMessageFactory shouldBeDirectory(final File actual) {
	return new ShouldBeDirectory(actual);
  }

  private ShouldBeDirectory(final Path actual) {
	super(PATH_SHOULD_BE_DIRECTORY, actual);
  }

  private ShouldBeDirectory(final File actual) {
	super(FILE_SHOULD_BE_DIRECTORY, actual);
  }
}
