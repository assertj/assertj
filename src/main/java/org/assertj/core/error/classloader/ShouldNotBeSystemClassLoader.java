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
 * Creates an error message indicating that an assertion that ensures a class loader is not the
 * {@link ClassLoader#getSystemClassLoader() system class loader}.
 *
 * @author Ashley Scopes
 */
public class ShouldNotBeSystemClassLoader extends BasicErrorMessageFactory {

  private static final String CLASS_LOADER_SHOULD_NOT_BE_SYSTEM_CLASS_LOADER = String.join(
    "%n",
    "",
    "Expecting class loader",
    "  %s",
    "to not be the system class loader"
  );

  /**
   * Creates a new <code>{@link ShouldNotBeSystemClassLoader}</code>.
   *
   * @param classLoader the class loader that should not be the system class loader.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeSystemClassLoader(ClassLoader classLoader) {
    return new ShouldNotBeSystemClassLoader(classLoader);
  }

  private ShouldNotBeSystemClassLoader(ClassLoader classLoader) {
    super(
      CLASS_LOADER_SHOULD_NOT_BE_SYSTEM_CLASS_LOADER,
      requireNonNull(classLoader, "classLoader should not be null")
    );
  }
}
