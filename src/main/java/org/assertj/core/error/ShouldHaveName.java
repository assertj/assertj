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
 * Creates an error message indicating that a {@code File} should have name.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveName extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_NAME = "%nExpecting%n  <%s>%nto have name:%n  <%s>%nbut had:%n  <%s>";

  public static ShouldHaveName shouldHaveName(File actual, String expectedName) {
    return new ShouldHaveName(actual, expectedName);
  }

  private ShouldHaveName(File actual, String expectedName) {
    super(SHOULD_HAVE_NAME, actual, expectedName, actual.getName());
  }
  
  public static ShouldHaveName shouldHaveName(Path actual, String expectedName) {
	return new ShouldHaveName(actual, expectedName);
  }
  
  private ShouldHaveName(Path actual, String expectedName) {
	super(SHOULD_HAVE_NAME, actual, expectedName, actual.getFileName());
  }
}
