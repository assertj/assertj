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

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Creates an error message indicating that an assertion that verifies the text on files/inputStreams/paths
 * is not encoded in a given charset.
 *
 * @author Ludovic VIEGAS
 */
public class ShouldNotBeEncodedIn extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotBeEncodedIn}</code>.
   * @param actual the actual file in the failed assertion.
   * @param charset the unexpected text encoding in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeEncodedIn(File actual, Charset charset) {
    return new ShouldNotBeEncodedIn(actual, charset);
  }

  /**
   * Creates a new <code>{@link ShouldNotBeEncodedIn}</code>.
   * @param actual the actual input-stream in the failed assertion.
   * @param charset the unexpected text encoding in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeEncodedIn(InputStream actual, Charset charset) {
    return new ShouldNotBeEncodedIn(actual, charset);
  }

  /**
   * Creates a new <code>{@link ShouldNotBeEncodedIn}</code>.
   * @param actual the actual file in the failed assertion.
   * @param charset the unexpected text encoding in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeEncodedIn(Path actual, Charset charset) {
    return new ShouldNotBeEncodedIn(actual, charset);
  }

  private ShouldNotBeEncodedIn(File actual, Charset charset) {
    super("%nFile should not be encoded in %s: %s%n%n", charset, actual);
  }

  private ShouldNotBeEncodedIn(InputStream actual, Charset charset) {
    super("%nInputStream should not be encoded in %s%n%n", charset);
  }

  private ShouldNotBeEncodedIn(Path actual, Charset charset) {
    super("%nPath should not be encoded in %s: %s%n%n", charset, actual);
  }
}
