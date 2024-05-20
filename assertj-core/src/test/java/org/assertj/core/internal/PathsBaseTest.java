/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static org.assertj.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import java.nio.file.Path;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

/**
 * Base class for {@link Paths} tests.
 */
public abstract class PathsBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected static Paths underTest = Paths.instance();

  protected static NioFilesWrapper nioFilesWrapper = spy(underTest.nioFilesWrapper);
  protected static Failures failures = spy(underTest.failures);
  protected static Diff diff = spy(underTest.diff);
  protected static BinaryDiff binaryDiff = spy(underTest.binaryDiff);

  @TempDir
  protected Path tempDir;

  @BeforeAll
  static void injectSpies() {
    underTest.nioFilesWrapper = nioFilesWrapper;
    underTest.failures = failures;
    underTest.diff = diff;
    underTest.binaryDiff = binaryDiff;
  }

  @AfterAll
  static void removeSpies() {
    underTest.nioFilesWrapper = getSpiedInstance(nioFilesWrapper);
    underTest.failures = getSpiedInstance(failures);
    underTest.diff = getSpiedInstance(diff);
    underTest.binaryDiff = getSpiedInstance(binaryDiff);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getSpiedInstance(Object spy) {
    return (T) mockingDetails(spy).getMockCreationSettings().getSpiedInstance();
  }

  @BeforeEach
  void resetSpies() {
    reset(nioFilesWrapper, failures, diff, binaryDiff);
  }

}
