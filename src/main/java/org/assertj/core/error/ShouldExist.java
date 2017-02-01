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
 * Creates an error message indicating that an assertion that verifies that a {@link File} or {@link Path} exists
 * failed.
 * 
 * @author Yvonne Wang
 */
public class ShouldExist extends BasicErrorMessageFactory {

  @VisibleForTesting
  public static final String PATH_SHOULD_EXIST = "%nExpecting path:%n  <%s>%nto exist (symbolic links were followed).";
  public static final String PATH_SHOULD_EXIST_NO_FOLLOW_LINKS = "%nExpecting path:%n  <%s>%nto exist (symbolic links were not followed).";
  public static final String FILE_SHOULD_EXIST = "%nExpecting file:%n  <%s>%nto exist.";

  /**
   * Creates a new <code>{@link ShouldExist}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldExist(File actual) {
	return new ShouldExist(actual);
  }

  public static ErrorMessageFactory shouldExist(final Path actual) {
	return new ShouldExist(actual, true);
  }

  public static ErrorMessageFactory shouldExistNoFollowLinks(final Path actual) {
	return new ShouldExist(actual, false);
  }

  private ShouldExist(File actual) {
	super(FILE_SHOULD_EXIST, actual);
  }

  private ShouldExist(final Path actual, boolean followLinks) {
	super(followLinks ? PATH_SHOULD_EXIST : PATH_SHOULD_EXIST_NO_FOLLOW_LINKS, actual);
  }
}
