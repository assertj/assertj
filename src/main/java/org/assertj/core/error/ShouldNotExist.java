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
 * Creates an error message indicating that an assertion that verifies that a {@link File} or {@link Path} does not
 * exist failed.
 * 
 * @author Yvonne Wang
 * @author Francis Galiegue
 */
public class ShouldNotExist extends BasicErrorMessageFactory {

  @VisibleForTesting
  public static final String PATH_SHOULD_NOT_EXIST = "%nExpecting path:%n  <%s>%nnot to exist";
  @VisibleForTesting
  public static final String FILE_SHOULD_NOT_EXIST = "%nExpecting file:%n  <%s>%nnot to exist";
  @VisibleForTesting
  public static final String PATH_SHOULD_NOT_EXIST_NO_FOLLOW_LINKS = "%nExpecting path:%n  <%s>%nnot to exist (symbolic links were not followed).";

  /**
   * Creates a new <code>{@link ShouldNotExist}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotExist(File actual) {
	return new ShouldNotExist(actual);
  }

  public static ErrorMessageFactory shouldNotExist(final Path actual) {
	return new ShouldNotExist(actual);
  }

  public static ErrorMessageFactory shouldExistNoFollowLinks(final Path actual) {
	return new ShouldNotExist(actual);
  }

  private ShouldNotExist(File actual) {
	super(FILE_SHOULD_NOT_EXIST, actual);
  }

  private ShouldNotExist(final Path actual) {
	super(PATH_SHOULD_NOT_EXIST, actual);
  }

  private ShouldNotExist(final Path actual, boolean followLinks) {
	super(followLinks ? PATH_SHOULD_NOT_EXIST : PATH_SHOULD_NOT_EXIST_NO_FOLLOW_LINKS, actual);
  }
}
