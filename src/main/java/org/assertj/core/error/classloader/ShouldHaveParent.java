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
 * Creates an error message indicating that an assertion that ensures a class loader has a non-null
 * parent class loader has failed.
 *
 * @author Ashley Scopes
 */
public class ShouldHaveParent extends BasicErrorMessageFactory {

  private static final String CLASS_LOADER_SHOULD_HAVE_NON_NULL_PARENT = String.join(
    "%n",
    "",
    "Expecting class loader",
    "  %s",
    "to have a non-null parent class loader, but one was not present"
  );

  /**
   * Creates a new <code>{@link ShouldHaveParent}</code>.
   *
   * @param child the child class loader that should have a parent.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveParent(ClassLoader child) {
    return new ShouldHaveParent(child);
  }

  private ShouldHaveParent(ClassLoader child) {
    super(
      CLASS_LOADER_SHOULD_HAVE_NON_NULL_PARENT,
      requireNonNull(child, "child should not be null")
    );
  }
}
