/*
 * Created on Jan 29, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.error;

import java.io.File;

/**
 * Creates an error message indicating that an assertion that verifies that a <code>{@link File}</code> does not exist
 * failed.
 * 
 * @author Yvonne Wang
 */
public class ShouldNotExist extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotExist}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotExist(File actual) {
    return new ShouldNotExist(actual);
  }

  private ShouldNotExist(File actual) {
    super("\nExpecting file:<%s> not to exist", actual);
  }
}
