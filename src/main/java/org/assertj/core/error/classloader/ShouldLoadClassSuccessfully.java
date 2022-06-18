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
 * Creates an error message indicating that an assertion that ensures a class loader can load a
 * class successfully.
 *
 * <p>This can apply to {@link ClassNotFoundException} instances, or it can apply to
 * {@link LinkageError} instances that may be thrown depending on the implementation. It may also
 * apply to {@link RuntimeException} types that do not enforce checked exceptions. All other
 * {@link Error} types are not allowed, as this may be indicative of a bug in a test instead of
 * desired behaviour.
 *
 * @author Ashley Scopes
 */
public class ShouldLoadClassSuccessfully extends BasicErrorMessageFactory {

  private static final String CLASS_LOADER_SHOULD_LOAD_CLASS_SUCCESSFULLY = String.join(
    "%n",
    "",
    "Expecting class loader",
    "  %s",
    "to be able to successfully load class with binary name:",
    "  %s",
    "but it threw an exception:",
    "%s"
  );

  /**
   * Creates a new <code>{@link ShouldLoadClassSuccessfully}</code>.
   *
   * @param classLoader the class loader that should load a class successfully.
   * @param binaryName  the binary name of the class that attempted to be loaded.
   * @param ex          the {@link ClassNotFoundException} that was thrown.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldLoadClassSuccessfully(
    ClassLoader classLoader,
    String binaryName,
    Throwable ex
  ) {
    return new ShouldLoadClassSuccessfully(classLoader, binaryName, ex);
  }

  private ShouldLoadClassSuccessfully(
    ClassLoader classLoader,
    String binaryName,
    Throwable ex
  ) {
    super(
      CLASS_LOADER_SHOULD_LOAD_CLASS_SUCCESSFULLY,
      requireNonNull(classLoader, "classLoader should not be null"),
      requireNonNull(binaryName, "binaryName should not be null"),
      requireNonNull(ex, "ex should not be null")
    );
  }
}
