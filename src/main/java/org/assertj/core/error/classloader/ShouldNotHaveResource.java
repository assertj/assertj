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

import java.net.URL;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * Creates an error message indicating that an assertion that ensures a class loader or class has
 * not got a given resource failed.
 *
 * @author Ashley Scopes
 */
public class ShouldNotHaveResource extends BasicErrorMessageFactory {

  private static final String CLASS_LOADER_SHOULD_NOT_HAVE_RESOURCE = String.join(
    "%n",
    "",
    "Expecting class loader",
    "  %s",
    "to have no valid resource at:",
    "  %s",
    "but found:",
    "  %s"
  );

  /**
   * Creates a new <code>{@link ShouldNotHaveResource}</code>.
   *
   * @param classLoader the class loader that should not have the resource.
   * @param path        the path to the resource that should not exist.
   * @param url         the URL that exists.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotHaveResource(
    ClassLoader classLoader,
    String path,
    URL url
  ) {
    return new ShouldNotHaveResource(classLoader, path, url);
  }

  private ShouldNotHaveResource(ClassLoader classLoader, String path, URL url) {
    super(
      CLASS_LOADER_SHOULD_NOT_HAVE_RESOURCE,
      requireNonNull(classLoader, "classLoader should not be null"),
      requireNonNull(path, "path should not be null"),
      requireNonNull(url, "url should not be null")
    );
  }
}
