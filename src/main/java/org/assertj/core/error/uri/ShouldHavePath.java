/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error.uri;

import static org.assertj.core.util.Strings.isNullOrEmpty;

import java.net.URI;
import java.net.URL;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHavePath extends BasicErrorMessageFactory {

  private static final String SHOULD_NOT_HAVE_PATH = "%nExpecting:%n  <%s>%nnot to have a path but had:%n  <%s>";
  private static final String SHOULD_HAVE_PATH = "%nExpecting path of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>";

  public static ErrorMessageFactory shouldHavePath(URI actual, String expectedPath) {
    return expectedPath == null ? new ShouldHavePath(actual) : new ShouldHavePath(actual, expectedPath);
  }

  private ShouldHavePath(URI actual, String expectedPath) {
    super(SHOULD_HAVE_PATH, actual, expectedPath, actual.getPath());
  }

  private ShouldHavePath(URI actual) {
    super(SHOULD_NOT_HAVE_PATH, actual, actual.getPath());
  }

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
