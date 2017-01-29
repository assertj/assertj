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

import static org.mockito.Mockito.mock;

import java.nio.charset.Charset;
import java.nio.file.Path;

import org.assertj.core.internal.Paths;


/**
 * Base class for {@link PathAssert} tests.
 */
public abstract class PathAssertBaseTest extends BaseTestTemplate<PathAssert, Path> {
  
  protected Paths paths;
  protected Charset defaultCharset;
  protected Charset otherCharset;

  @Override
  protected PathAssert create_assertions() {
    return new PathAssert(mock(Path.class));
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    paths = mock(Paths.class);
    assertions.paths = paths;
    defaultCharset = Charset.defaultCharset();
    for (Charset charset : Charset.availableCharsets().values()) {
      if (!charset.equals(defaultCharset)) otherCharset = charset;
    }
  }

  protected Charset getCharset(PathAssert someAssertions) {
    return someAssertions.charset;
  }

  protected Paths getPaths(PathAssert someAssertions) {
    return someAssertions.paths;
  }
}
