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
package org.assertj.core.error.uri;

import static org.assertj.core.util.Strings.isNullOrEmpty;

import java.net.URI;
import java.net.URL;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/** Creates errors for URIs and URLs with an unexpected path. */
public class ShouldHavePath extends BasicErrorMessageFactory {

  private static final String SHOULD_NOT_HAVE_PATH = "%nExpecting actual:%n  <%s>%nnot to have a path but had:%n  <%s>";
  private static final String SHOULD_HAVE_PATH = "%nExpecting path of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>";

  /**
   * Creates an error for a URI with the wrong path.
   *
   * @param actual the actual URI
   * @param expectedPath the expected path
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldHavePath(URI actual, String expectedPath) {
    return expectedPath == null ? new ShouldHavePath(actual) : new ShouldHavePath(actual, expectedPath);
  }

  private ShouldHavePath(URI actual, String expectedPath) {
    super(SHOULD_HAVE_PATH, actual, expectedPath, actual.getPath());
  }

  private ShouldHavePath(URI actual) {
    super(SHOULD_NOT_HAVE_PATH, actual, actual.getPath());
  }

  /**
   * Creates an error for a URL with the wrong path.
   *
   * @param actual the actual URL
   * @param expectedPath the expected path
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldHavePath(URL actual, String expectedPath) {
    return isNullOrEmpty(expectedPath) ? new ShouldHavePath(actual) : new ShouldHavePath(actual, expectedPath);
  }

  private ShouldHavePath(URL actual, String expectedPath) {
    super(SHOULD_HAVE_PATH, actual, expectedPath, actual.getPath());
  }

  private ShouldHavePath(URL actual) {
    super(SHOULD_NOT_HAVE_PATH, actual, actual.getPath());
  }

}
