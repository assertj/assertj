/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import java.io.File;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements is empty failed. A group of elements
 * can be a collection, an array, {@code String} or a {@code File}.
 * 
 * @author Alex Ruiz
 */
public class ShouldBeEmpty extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeEmpty}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEmpty(Object actual) {
    return new ShouldBeEmpty("%nExpecting empty but was:<%s>", actual);
  }

  /**
   * Creates a new <code>{@link ShouldBeEmpty}</code>.
   * @param actual the actual file in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEmpty(File actual) {
    return new ShouldBeEmpty("%nExpecting file <%s> to be empty", actual);
  }

  private ShouldBeEmpty(String format, Object... arguments) {
    super(format, arguments);
  }
}
