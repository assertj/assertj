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

/**
 * Creates an error message indicating that an assertion that verifies that a <code>{@link File}</code> is executable
 * failed.
 * 
 * @author Olivier Demeijer
 * 
 */
public class ShouldBeExecutable extends BasicErrorMessageFactory {
  static final String SHOULD_BE_EXECUTABLE = "%nExpecting:%n  <%s>%nto be executable.";

  private ShouldBeExecutable(File actual) {
	super(SHOULD_BE_EXECUTABLE, actual);
  }

  private ShouldBeExecutable(Path actual) {
	super(SHOULD_BE_EXECUTABLE, actual);
  }

  /**
   * Creates a new <code>{@link ShouldBeExecutable}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeExecutable(File actual) {
	return new ShouldBeExecutable(actual);
  }

  public static ErrorMessageFactory shouldBeExecutable(Path actual) {
	return new ShouldBeExecutable(actual);
  }

}
