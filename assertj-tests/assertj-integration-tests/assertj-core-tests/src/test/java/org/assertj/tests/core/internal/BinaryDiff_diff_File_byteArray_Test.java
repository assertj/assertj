/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.internal;

import static java.lang.System.lineSeparator;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/// Tests for [BinaryDiff#diff(File, byte\[\])].
///
/// @author Olivier Michallat
/// @author Joel Costigliola
class BinaryDiff_diff_File_byteArray_Test {

  private final BinaryDiff binaryDiff = new BinaryDiff();

  @TempDir
  Path tempDir;

  @Test
  void should_return_no_diff_if_file_and_array_have_equal_content() throws IOException {
    // GIVEN
    File actual = createFile("test" + lineSeparator());
    byte[] expected = ("test" + lineSeparator()).getBytes();
    // WHEN
    BinaryDiffResult result = binaryDiff.diff(actual, expected);
    // THEN
    then(result.hasNoDiff()).isTrue();
  }

  @Test
  void should_return_diff_if_inputstreams_differ_on_one_byte() throws IOException {
    // GIVEN
    File actual = createFile("test" + lineSeparator());
    byte[] expected = ("fest" + lineSeparator()).getBytes();
    // WHEN
    BinaryDiffResult result = binaryDiff.diff(actual, expected);
    // THEN
    then(result.offset).isZero();
    then(result.actual).isEqualTo("0x74");
    then(result.expected).isEqualTo("0x66");
  }

  @Test
  void should_return_diff_if_actual_is_shorter() throws IOException {
    // GIVEN
    File actual = createFile("foo" + lineSeparator());
    byte[] expected = ("foo" + lineSeparator() + "bar").getBytes();
    // WHEN
    BinaryDiffResult result = binaryDiff.diff(actual, expected);
    // THEN
    then(result.offset).isEqualTo(3 + lineSeparator().length());
    then(result.actual).isEqualTo("EOF");
    then(result.expected).isEqualTo("0x62");
  }

  @Test
  void should_return_diff_if_expected_is_shorter() throws IOException {
    // GIVEN
    File actual = createFile("foobar" + lineSeparator());
    byte[] expected = "foo".getBytes();
    // WHEN
    BinaryDiffResult result = binaryDiff.diff(actual, expected);
    // THEN
    then(result.offset).isEqualTo(3);
    then(result.actual).isEqualTo("0x62");
    then(result.expected).isEqualTo("EOF");
  }

  private File createFile(String content) throws IOException {
    return Files.write(PathFactory.createPath(tempDir), content.getBytes()).toFile();
  }

}
