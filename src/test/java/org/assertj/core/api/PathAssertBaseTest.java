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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.internal.Paths;

import java.nio.file.Path;

import static org.mockito.Mockito.mock;


/**
 * Base class for {@link PathAssert} tests.
 */
public abstract class PathAssertBaseTest
    extends BaseTestTemplate<PathAssert, Path>
{
  protected Paths paths;

  @Override
  protected PathAssert create_assertions() {
    return new PathAssert(mock(Path.class));
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    paths = mock(Paths.class);
    assertions.paths = paths;
  }

  protected Paths getPaths(PathAssert someAssertions) {
    return someAssertions.paths;
  }
}
