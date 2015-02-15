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
package org.assertj.core.error;

import org.assertj.core.util.VisibleForTesting;

import java.nio.file.Path;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Path} is a regular file has failed.
 *
 */
public class ShouldBeRegularFile extends BasicErrorMessageFactory
{
  @VisibleForTesting
  public static final String SHOULD_BE_REGULAR_FILE = "%nExpecting path:%n  <%s>%nto be a regular file.";

  public static ErrorMessageFactory shouldBeRegularFile(final Path actual) {
	return new ShouldBeRegularFile(actual);
  }

  private ShouldBeRegularFile(final Path actual) {
	super(SHOULD_BE_REGULAR_FILE, actual);
  }
}
