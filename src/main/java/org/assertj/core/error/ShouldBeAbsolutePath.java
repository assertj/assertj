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

import org.assertj.core.util.VisibleForTesting;

import java.io.File;
import java.nio.file.Path;

/**
 * Creates an error message indicating that an assertion that verifies that a
 * {@link File} or {@link Path} is an absolute path
 * failed.
 * 
 * @author Yvonne Wang
 * @author Francis Galiegue
 */
public class ShouldBeAbsolutePath extends BasicErrorMessageFactory {

  @VisibleForTesting
  public static final String SHOULD_BE_ABSOLUTE_PATH = "%nExpecting:%n  <%s>%nto be an absolute path.";

  /**
   * Creates a new <code>{@link ShouldBeAbsolutePath}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeAbsolutePath(File actual) {
    return new ShouldBeAbsolutePath(actual);
  }

  public static ErrorMessageFactory shouldBeAbsolutePath(final Path actual) {
    return new ShouldBeAbsolutePath(actual);
  }

  private ShouldBeAbsolutePath(File actual) {
    super(SHOULD_BE_ABSOLUTE_PATH, actual);
  }

  private ShouldBeAbsolutePath(final Path actual) {
    super(SHOULD_BE_ABSOLUTE_PATH, actual);
  }
}
