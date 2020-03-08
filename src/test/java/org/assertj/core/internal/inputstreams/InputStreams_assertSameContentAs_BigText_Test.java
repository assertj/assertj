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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.inputstreams;

import static java.nio.file.Files.newBufferedWriter;
import static java.util.stream.LongStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Files.newTemporaryFile;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.assertj.core.internal.Diff;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.InputStreams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InputStreams_assertSameContentAs_BigText_Test {
  private static final long ACTUAL_SIZE = 100_000L; // test with VM options "-Xms30m -Xmx30m"
  private static File actualBig;

  @BeforeAll
  public static void setUpOnce() throws IOException {
    actualBig = createBigFile(ACTUAL_SIZE);
  }

  private InputStreams inputStreams;

  @BeforeEach
  public void setUp() {
    inputStreams = new InputStreams();
    inputStreams.diff = new Diff();
    inputStreams.failures = new Failures();
  }

  @Test
  public void should_fail_if_big_inputstreams_do_not_have_equal_content() throws IOException {
    InputStream expectedBig = new FileInputStream(createBigFile(ACTUAL_SIZE / 1_000));
    Throwable error = catchThrowable(() -> inputStreams.assertSameContentAs(someInfo(), new FileInputStream(actualBig),
                                                                            expectedBig));
    assertThat(error).isInstanceOf(AssertionError.class);
  }

  private static File createBigFile(long endExclusive) throws IOException {
    Path tempFile = newTemporaryFile().toPath();
    tempFile.toFile().deleteOnExit();
    try (BufferedWriter writer = newBufferedWriter(tempFile)) {
      range(1, endExclusive).forEach(i -> {
        try {
          writer.write(Integer.toHexString((int) i));
          writer.newLine();
        } catch (IOException e) {
          fail(e);
        }
      });
    }
    return tempFile.toFile();
  }
}
