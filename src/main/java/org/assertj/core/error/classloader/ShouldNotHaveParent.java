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
 * Creates an error message indicating that an assertion that ensures a class loader has not got a
 * non-null parent class loader has failed.
 *
 * @author Ashley Scopes
 */
public class ShouldNotHaveParent extends BasicErrorMessageFactory {

  private static final String CLASS_LOADER_SHOULD_NOT_HAVE_PARENT = String.join(
    "%n",
    "",
    "Expecting class loader",
    "  %s",
    "to not have a parent class loader, but found:",
    "  %s"
  );


  /**
   * Creates a new <code>{@link ShouldNotHaveParent}</code>.
   *
   * @param child  the child class loader that should not have a parent.
   * @param parent the parent class loader that was found.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotHaveParent(ClassLoader child, ClassLoader parent) {
    return new ShouldNotHaveParent(child, parent);
  }

  private ShouldNotHaveParent(ClassLoader child, ClassLoader parent) {
    super(
      CLASS_LOADER_SHOULD_NOT_HAVE_PARENT,
      requireNonNull(child, "child should not be null"),
      requireNonNull(parent, "parent should not be null")
    );
  }
}
