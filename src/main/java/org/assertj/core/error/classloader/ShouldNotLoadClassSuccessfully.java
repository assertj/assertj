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
 * Creates an error message indicating that an assertion that ensures a class loader can not load a
 * class failed.
 *
 * @author Ashley Scopes
 */
public class ShouldNotLoadClassSuccessfully extends BasicErrorMessageFactory {

  private static final String CLASS_LOADER_SHOULD_NOT_LOAD_CLASS_SUCCESSFULLY = String.join(
    "%n",
    "",
    "Expecting class loader",
    "  %s",
    "to not successfully load class with name:",
    "  %s",
    "but a class was successfully returned"
  );

  /**
   * Creates a new <code>{@link ShouldNotLoadClassSuccessfully}</code>.
   *
   * @param classLoader the class loader that should not load a class successfully.
   * @param binaryName  the binary name of the class that attempted to be loaded.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotLoadClassSuccessfully(
    ClassLoader classLoader,
    String binaryName
  ) {
    return new ShouldNotLoadClassSuccessfully(classLoader, binaryName);
  }

  private ShouldNotLoadClassSuccessfully(
    ClassLoader classLoader,
    String binaryName
  ) {
    super(
      CLASS_LOADER_SHOULD_NOT_LOAD_CLASS_SUCCESSFULLY,
      requireNonNull(classLoader, "classLoader should not be null"),
      requireNonNull(binaryName, "binaryName should not be null")
    );
  }
}
