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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error.uri;

import java.net.URI;
import java.net.URL;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveNoHost extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_NO_HOST = "%n" +
                                                    "Expecting no host for:%n" +
                                                    "  %s%n" +
                                                    "but found:%n" +
                                                    "  %s";

  public static ErrorMessageFactory shouldHaveNoHost(URI actual) {
    return new ShouldHaveNoHost(actual, actual.getHost());
  }

  public static ErrorMessageFactory shouldHaveNoHost(URL actual) {
    return new ShouldHaveNoHost(actual, actual.getHost());
  }

  private ShouldHaveNoHost(Object actual, String host) {
    super(SHOULD_HAVE_NO_HOST, actual, host);
  }

}
