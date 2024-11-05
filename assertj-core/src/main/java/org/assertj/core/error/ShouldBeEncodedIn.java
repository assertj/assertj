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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.EncodingIssue;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Creates an error message indicating that an assertion that verifies the text encoding on files/inputStreams/paths
 * is properly encoded.
 *
 * @author Ludovic VIEGAS
 */
public class ShouldBeEncodedIn extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeEncodedIn}</code>.
   * @param actual the actual file in the failed assertion.
   * @param charset the expected text encoding in the failed assertion.
   * @param issues the encoding issues.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEncodedIn(File actual, Charset charset, List<EncodingIssue> issues) {
    return new ShouldBeEncodedIn(actual, charset, issuesAsString(issues));
  }

  /**
   * Creates a new <code>{@link ShouldBeEncodedIn}</code>.
   * @param actual the actual input-stream in the failed assertion.
   * @param charset the expected text encoding in the failed assertion.
   * @param issues the encoding issues.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEncodedIn(InputStream actual, Charset charset, List<EncodingIssue> issues) {
    return new ShouldBeEncodedIn(actual, charset, issuesAsString(issues));
  }

  /**
   * Creates a new <code>{@link ShouldBeEncodedIn}</code>.
   * @param actual the actual file in the failed assertion.
   * @param charset the expected text encoding in the failed assertion.
   * @param issues the encoding issues.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEncodedIn(Path actual, Charset charset, List<EncodingIssue> issues) {
    return new ShouldBeEncodedIn(actual, charset, issuesAsString(issues));
  }

  private ShouldBeEncodedIn(File actual, Charset charset, String issues) {
    super("%nFile has %s encoding issues%n  File: %s%n  At lines:%n%s%n%n", charset, actual, issues);
  }

  private ShouldBeEncodedIn(InputStream actual, Charset charset, String issues) {
    super("%nInputStream has %s encoding issues%n  At lines:%n%s%n%n", charset, issues);
  }

  private ShouldBeEncodedIn(Path actual, Charset charset, String issues) {
    super("%nPath has %s encoding issues:%n  Path: %s%n  At lines:%n%s%n%n", charset, actual, issues);
  }

  private static String issuesAsString(List<EncodingIssue> encodingIssues) {
    return encodingIssues.stream().map(EncodingIssue::toString).collect(joining(System.lineSeparator()));
  }
}
