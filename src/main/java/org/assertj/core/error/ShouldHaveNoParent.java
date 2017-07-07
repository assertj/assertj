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
 * Creates an error message when a {@code File} should not have a parent.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveNoParent extends BasicErrorMessageFactory {

  @VisibleForTesting
  public static final String PATH_HAS_PARENT = "%nExpected actual path:%n  <%s>%n not to have a parent, but parent was:%n  <%s>";

  @VisibleForTesting
  public static final String FILE_HAS_PARENT = "%nExpecting file (or directory):%n  <%s>%nnot to have a parent, but parent was:%n  <%s>";

  /**
   * Creates a new <code>{@link ShouldHaveNoParent}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldHaveNoParent shouldHaveNoParent(File actual) {
	return new ShouldHaveNoParent(actual);
  }

  public static ShouldHaveNoParent shouldHaveNoParent(Path actual) {
	return new ShouldHaveNoParent(actual);
  }

  private ShouldHaveNoParent(File actual) {
	super(FILE_HAS_PARENT, actual, actual.getParentFile());
  }

  private ShouldHaveNoParent(Path actual)
  {
	super(PATH_HAS_PARENT, actual, actual.getParent());
  }
}
