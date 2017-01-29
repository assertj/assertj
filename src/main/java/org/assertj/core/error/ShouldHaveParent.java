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
 * Creates an error message indicating that a {@code File} should have a parent.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveParent extends BasicErrorMessageFactory {

  @VisibleForTesting
  public static final String PATH_NO_PARENT
      = "%nExpecting path%n  <%s>%nto have parent:%n  <%s>%nbut did not have one.";

  @VisibleForTesting
  public static final String PATH_NOT_EXPECTED_PARENT
      = "%nExpecting path%n  <%s>%nto have parent:%n  <%s>%nbut had:%n  <%s>.";

  @VisibleForTesting
  public static final String FILE_NO_PARENT
      = "%nExpecting file%n  <%s>%nto have parent:%n  <%s>%nbut did not have one.";

  @VisibleForTesting
  public static final String FILE_NOT_EXPECTED_PARENT
      = "%nExpecting file%n  <%s>%nto have parent:%n  <%s>%nbut had:%n  <%s>.";

  public static ShouldHaveParent shouldHaveParent(File actual, File expected) {
    return actual.getParentFile() == null ? new ShouldHaveParent(actual, expected) : new ShouldHaveParent(actual,
        actual.getParentFile(), expected);
  }

  public static ShouldHaveParent shouldHaveParent(Path actual, Path expected) {
    final Path actualParent = actual.getParent();
    return actualParent == null
        ? new ShouldHaveParent(actual, expected)
        : new ShouldHaveParent(actual, actualParent, expected);
  }

  public static ShouldHaveParent shouldHaveParent(Path actual, Path actualParent, Path expected) {
    return new ShouldHaveParent(actual, actualParent, expected);
  }

  private ShouldHaveParent(File actual, File expected) {
    super(FILE_NO_PARENT, actual, expected);
  }

  private ShouldHaveParent(File actual, File actualParent, File expected) {
    super(FILE_NOT_EXPECTED_PARENT, actual, expected, actualParent);
  }

  private ShouldHaveParent(Path actual, Path expected) {
    super(PATH_NO_PARENT, actual, expected);
  }

  private ShouldHaveParent(Path actual, Path actualParent, Path expected) {
    super(PATH_NOT_EXPECTED_PARENT, actual, expected, actualParent);
  }
}
