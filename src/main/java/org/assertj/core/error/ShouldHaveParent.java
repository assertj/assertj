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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.error;

import java.io.File;

/**
 * Creates an error message indicating that a {@code File} should have a parent.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveParent extends BasicErrorMessageFactory {

  public static ShouldHaveParent shouldHaveParent(File actual, File expected) {
    return actual.getParentFile() == null ? new ShouldHaveParent(actual, expected) : new ShouldHaveParent(actual,
        actual.getParentFile(), expected);
  }

  private ShouldHaveParent(File actual, File expected) {
    super("%nExpecting file%n  <%s>%nto have parent:%n  <%s>%nbut did not have one.", actual, expected);
  }

  private ShouldHaveParent(File actual, File actualParent, File expected) {
    super("%nExpecting file%n  <%s>%nto have parent:%n  <%s>%nbut had:%n  <%s>.", actual, expected, actualParent);
  }
}
