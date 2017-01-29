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

public class ShouldHavePort extends BasicErrorMessageFactory {

  private static final int NO_PORT = -1;
  private static final String SHOULD_HAVE_NO_PORT = "%nExpecting:%n  <%s>%nnot to have a port but had:%n  <%s>";
  private static final String SHOULD_HAVE_PORT = "%nExpecting port of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>";

  public static ErrorMessageFactory shouldHavePort(URI actual, int expectedPort) {
    return expectedPort == NO_PORT ? new ShouldHavePort(actual) : new ShouldHavePort(actual, expectedPort);
  }

  private ShouldHavePort(URI actual, int expectedPort) {
    super(SHOULD_HAVE_PORT, actual, expectedPort, actual.getPort());
  }

  private ShouldHavePort(URI actual) {
    super(SHOULD_HAVE_NO_PORT, actual, actual.getPort());
  }

  public static ErrorMessageFactory shouldHavePort(URL actual, int expectedPort) {
    return expectedPort == NO_PORT ? new ShouldHavePort(actual) : new ShouldHavePort(actual, expectedPort);
  }

  private ShouldHavePort(URL actual, int expectedPort) {
    super(SHOULD_HAVE_PORT, actual, expectedPort, actual.getPort());
  }

  private ShouldHavePort(URL actual) {
    super(SHOULD_HAVE_NO_PORT, actual, actual.getPort());
  }
}
