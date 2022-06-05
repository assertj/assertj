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
 * Copyright 2012-2022 the original author or authors.
 */

package org.assertj.core.error.classloader;

import static java.util.Objects.requireNonNull;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * Creates an error message indicating that an assertion that ensures a class loader is registered
 * as being {@link ClassLoader#isRegisteredAsParallelCapable() parallel capable} has failed.
 *
 * @author Ashley Scopes
 */
public class ShouldBeParallelCapable extends BasicErrorMessageFactory {

  private static final String CLASS_LOADER_SHOULD_BE_PARALLEL_CAPABLE = String.join(
    "%n",
    "",
    "Expecting class loader",
    "  %s",
    "to be parallel capable, but it was not"
  );

  /**
   * Creates a new <code>{@link ShouldBeParallelCapable}</code>.
   *
   * @param classLoader the class loader that should be parallel capable.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeParallelCapable(ClassLoader classLoader) {
    return new ShouldBeParallelCapable(classLoader);
  }

  private ShouldBeParallelCapable(ClassLoader classLoader) {
    super(
      CLASS_LOADER_SHOULD_BE_PARALLEL_CAPABLE,
      requireNonNull(classLoader, "classLoader should not be null")
    );
  }
}
