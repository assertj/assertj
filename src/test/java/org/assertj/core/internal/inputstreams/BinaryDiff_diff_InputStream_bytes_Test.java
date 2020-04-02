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

import static org.assertj.core.api.BDDAssertions.then;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.BinaryDiffResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BinaryDiff#diff(InputStream, byte[])}</code>.
 *
 * @author Olivier Michallat, Stefan Birkner
 */
@DisplayName("BinaryDiff diff(InputStream)")
public class BinaryDiff_diff_InputStream_bytes_Test {

  private static final BinaryDiff BINARY_DIFF = new BinaryDiff();

  private InputStream actual;
  private byte[] expected;

  @Test
  public void should_return_no_diff_if_inputstream_content_is_equal_to_byte_array() throws IOException {
    // GIVEN
    actual = stream(0xCA, 0xFE, 0xBA, 0xBE);
    expected = bytes(0xCA, 0xFE, 0xBA, 0xBE);
    // WHEN
    BinaryDiffResult result = BINARY_DIFF.diff(actual, expected);
    // THEN
    then(result.hasNoDiff()).isTrue();
  }

  @Test
  public void should_return_diff_if_inputstreams_differ_on_one_byte() throws IOException {
    // GIVEN
    actual = stream(0xCA, 0xFE, 0xBA, 0xBE);
    expected = bytes(0xCA, 0xFE, 0xBE, 0xBE);
    // WHEN
    BinaryDiffResult result = BINARY_DIFF.diff(actual, expected);
    // THEN
    then(result.hasDiff()).isTrue();
    then(result.offset).isEqualTo(2);
    then(result.actual).isEqualTo("0xBA");
    then(result.expected).isEqualTo("0xBE");
  }

  @Test
  public void should_return_diff_if_actual_is_shorter() throws IOException {
    // GIVEN
    expected = bytes(0xCA, 0xFE, 0xBA, 0xBE);
    actual = stream(0xCA, 0xFE, 0xBA);
    // WHEN
    BinaryDiffResult result = BINARY_DIFF.diff(actual, expected);
    // THEN
    then(result.hasDiff()).isTrue();
    then(result.offset).isEqualTo(3);
    then(result.actual).isEqualTo("EOF");
    then(result.expected).isEqualTo("0xBE");
  }

  @Test
  public void should_return_diff_if_expected_is_shorter() throws IOException {
    // GIVEN
    actual = stream(0xCA, 0xFE, 0xBA, 0xBE);
    expected = bytes(0xCA, 0xFE, 0xBA);
    // WHEN
    BinaryDiffResult result = BINARY_DIFF.diff(actual, expected);
    // THEN
    then(result.hasDiff()).isTrue();
    then(result.offset).isEqualTo(3);
    then(result.actual).isEqualTo("0xBE");
    then(result.expected).isEqualTo("EOF");
  }

  private static InputStream stream(int... contents) {
    byte[] byteContents = bytes(contents);
    return new ByteArrayInputStream(byteContents);
  }

  private static byte[] bytes(int... contents) {
    byte[] byteContents = new byte[contents.length];
    for (int i = 0; i < contents.length; i++) {
      byteContents[i] = (byte) contents[i];
    }
    return byteContents;
  }
}
