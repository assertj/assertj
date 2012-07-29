/*
 * Created on Jul 05, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.error;

import java.io.File;

/**
 * Creates an error message indicating that an assertion that verifies that a <code>{@link File}</code> is executable
 * failed.
 * 
 * @author Olivier Demeijer
 * 
 */
public class ShouldBeExecutable extends BasicErrorMessageFactory {
  private ShouldBeExecutable(File actual) {
    super("File:<%s> should be executable", actual);
  }

  /**
   * Creates a new <code>{@link ShouldBeExecutable}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeExecutable(File actual) {
    return new ShouldBeExecutable(actual);
  }

}
