/*
 * Created on Jan 24, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.error;

import org.fest.assertions.description.Description;

/**
 * Creates an error message that indicates an assertion that verifies that two images should not be {@code null} failed.
 * 
 * @author Yvonne Wang
 */
public class ShouldNotBeEqualImages implements ErrorMessageFactory {

  private static final ShouldNotBeEqualImages INSTANCE = new ShouldNotBeEqualImages();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static ErrorMessageFactory shouldNotBeEqualImages() {
    return INSTANCE;
  }

  private ShouldNotBeEqualImages() {}

  /** {@inheritDoc} */
  public String create(Description d) {
    return MessageFormatter.instance().format(d, "expecting images not to be equal");
  }
}
