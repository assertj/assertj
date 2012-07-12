/*
 * Created on Sep 26, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import org.fest.assertions.description.Description;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements is not empty failed. A group of
 * elements can be a collection, an array or a {@code String}.
 * 
 * @author Alex Ruiz
 */
public class ShouldNotBeEmpty implements ErrorMessageFactory {

  private static final ShouldNotBeEmpty INSTANCE = new ShouldNotBeEmpty();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static ErrorMessageFactory shouldNotBeEmpty() {
    return INSTANCE;
  }

  private ShouldNotBeEmpty() {}

  /** {@inheritDoc} */
  public String create(Description d) {
    return MessageFormatter.instance().format(d, "expecting actual not to be empty");
  }
}
