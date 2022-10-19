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
package org.assertj.core.internal;

import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.spy;

import java.nio.file.Path;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

/**
 * Base class for {@link Paths} tests.
 */
public abstract class PathsBaseTest {

  @TempDir
  protected Path tempDir;

  protected Failures failures;
  protected Paths paths;
  protected NioFilesWrapper nioFilesWrapper;
  protected AssertionInfo info;

  protected Diff diff;
  protected BinaryDiff binaryDiff;

  @BeforeEach
  void setUp() {
    paths = Paths.instance();
    nioFilesWrapper = spy(paths.nioFilesWrapper);
    paths.nioFilesWrapper = nioFilesWrapper;
    failures = spy(paths.failures);
    paths.failures = failures;
    diff = spy(paths.diff);
    paths.diff = diff;
    binaryDiff = spy(paths.binaryDiff);
    paths.binaryDiff = binaryDiff;
    info = someInfo();
  }

}
