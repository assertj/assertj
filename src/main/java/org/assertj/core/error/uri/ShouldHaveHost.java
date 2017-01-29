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

import java.net.URI;
import java.net.URL;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveHost extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_HOST = "%nExpecting host of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>";

  public static ErrorMessageFactory shouldHaveHost(URI actual, String expectedHost) {
    return new ShouldHaveHost(actual, expectedHost);
  }

  private ShouldHaveHost(URI actual, String expectedHost) {
    super(SHOULD_HAVE_HOST, actual, expectedHost, actual.getHost());
  }

  public static ErrorMessageFactory shouldHaveHost(URL actual, String expectedHost) {
    return new ShouldHaveHost(actual, expectedHost);
  }

  private ShouldHaveHost(URL actual, String expectedHost) {
    super(SHOULD_HAVE_HOST, actual, expectedHost, actual.getHost());
  }
}
