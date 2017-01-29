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

import java.nio.file.Path;

public class ShouldStartWithPath extends BasicErrorMessageFactory {
  
  @VisibleForTesting
  public static final String PATH_SHOULD_START_WITH = "%nExpected path:%n  <%s>%nto start with:%n  <%s>%nbut it did not.";

  public static ErrorMessageFactory shouldStartWith(final Path actual, final Path other) {
	return new ShouldStartWithPath(actual, other);
  }

  private ShouldStartWithPath(final Path actual, final Path other) {
	super(PATH_SHOULD_START_WITH, actual, other);
  }
}
