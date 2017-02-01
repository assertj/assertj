/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.util.CheckReturnValue;

import java.nio.file.Path;

public abstract class AbstractBDDSoftAssertions extends Java6AbstractBDDSoftAssertions {

  /**
   * Creates a new, proxied instance of a {@link PathAssert}
   *
   * @param actual the path
   * @return the created assertion object
   */
  @CheckReturnValue
  public PathAssert then(Path actual) {
    return proxy(PathAssert.class, Path.class, actual);
  }
}
