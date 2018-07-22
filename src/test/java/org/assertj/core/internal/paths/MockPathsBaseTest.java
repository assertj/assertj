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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.BeforeEach;

public class MockPathsBaseTest extends PathsBaseTest {

  static final AssertionInfo INFO = someInfo();

  Path actual;
  Path other;

  @BeforeEach
  public void init() {
    actual = mock(Path.class);
    other = mock(Path.class);
  }

  static void failIfStreamIsOpen(InputStream stream) {
    try {
      assertThat(stream.read()).as("Stream should be closed").isNegative();
    } catch (IOException e) {
      assertThat(e).hasNoCause().hasMessage("Stream closed");
    }
  }

}
