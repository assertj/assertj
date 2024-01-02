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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import java.util.Collection;

/**
 * Creates an error message indicating that an assertion that verifies that a class has no permitted subclasses failed.
 *
 */
public class ShouldHaveNoPermittedSubclasses extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveNoPermittedSubclasses}</code>.
   *
   * @param actual the actual value in the failed assertion
   * @param found permitted subclasses found for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveNoPermittedSubclasses(Class<?> actual, Collection<Class<?>> found) {
    return new ShouldHaveNoPermittedSubclasses(actual, found);
  }

  private ShouldHaveNoPermittedSubclasses(Class<?> actual, Collection<Class<?>> found) {
    super("%nExpecting%n  %s%nto have no permitted subclasses, but the following permitted subclasses were found:%n  %s",
          actual, found);
  }
}
