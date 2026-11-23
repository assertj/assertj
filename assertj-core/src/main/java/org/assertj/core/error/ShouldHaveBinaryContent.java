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
package org.assertj.core.error;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

import org.assertj.core.internal.BinaryDiffResult;

/**
 * Creates an error message indicating that an assertion that verifies that a file/path has a given binary content failed.
 *
 * @author Olivier Michallat
 */
public class ShouldHaveBinaryContent extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveBinaryContent}</code>.
   * @param actual the actual file in the failed assertion.
   * @param diff the differences between {@code actual} and the given binary content.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveBinaryContent(File actual, BinaryDiffResult diff) {
    return new ShouldHaveBinaryContent(actual, diff);
  }

  /**
   * Creates a new <code>{@link ShouldHaveBinaryContent}</code>.
   * @param actual the actual path in the failed assertion.
   * @param diff the differences between {@code actual} and the given binary content.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveBinaryContent(Path actual, BinaryDiffResult diff) {
    return new ShouldHaveBinaryContent(actual, diff);
  }

  /**
   * Creates a new <code>{@link ShouldHaveBinaryContent}</code>.
   * @param actual the actual input stream in the failed assertion.
   * @param diff the differences between {@code actual} and the given binary content.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveBinaryContent(InputStream actual, BinaryDiffResult diff) {
    return new ShouldHaveBinaryContent(actual, diff);
  }

  /// Creates an error message for a byte difference between two files.
  ///
  /// @param actual the actual [File] or [Path]
  /// @param offset the zero-based offset of the difference
  /// @param expectedByte the expected byte, or -1 for end of file
  /// @param actualByte the actual byte, or -1 for end of file
  /// @return the created [ErrorMessageFactory]
  public static ErrorMessageFactory shouldHaveBinaryContent(Object actual, long offset, int expectedByte, int actualByte) {
    String type = actual instanceof File ? "File" : "Path";
    Object offsetForMessage = offset <= Integer.MAX_VALUE ? (int) offset : unquotedString(Long.toString(offset));
    return new ShouldHaveBinaryContent(type, actual, offsetForMessage, describe(expectedByte), describe(actualByte));
  }

  private ShouldHaveBinaryContent(File actual, BinaryDiffResult diff) {
    this("File", actual, diff.offset, diff.expected, diff.actual);
  }

  private ShouldHaveBinaryContent(Path actual, BinaryDiffResult diff) {
    this("Path", actual, diff.offset, diff.expected, diff.actual);
  }

  private ShouldHaveBinaryContent(String type, Object actual, Object offset, String expectedByte, String actualByte) {
    super("%n" + type + ":%n  %s%ndoes not have expected binary content at offset %s, expecting:%n  %s%nbut was:%n  %s",
          actual, offset, expectedByte, actualByte);
  }

  private static String describe(int value) {
    return value == -1 ? "EOF" : "0x" + Integer.toHexString(value).toUpperCase();
  }

  private ShouldHaveBinaryContent(InputStream actual, BinaryDiffResult diff) {
    super("%nInputStream%n  %s%ndoes not have expected binary content at offset %s, expecting:%n  %s%nbut was:%n  %s",
          actual, diff.offset, diff.expected, diff.actual);
  }
}
