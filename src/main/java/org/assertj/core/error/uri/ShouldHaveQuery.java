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

public class ShouldHaveQuery extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_QUERY = "%nExpecting query of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>";
  private static final String SHOULD_NOT_HAVE_QUERY = "%nExpecting:%n  <%s>%nnot to have a query but had:%n  <%s>";

  public static ErrorMessageFactory shouldHaveQuery(URI actual, String expectedQuery) {
    return expectedQuery == null ? new ShouldHaveQuery(actual) : new ShouldHaveQuery(actual, expectedQuery);
  }

  private ShouldHaveQuery(URI actual, String expectedQuery) {
    super(SHOULD_HAVE_QUERY, actual, expectedQuery, actual.getQuery());
  }

  private ShouldHaveQuery(URI actual) {
    super(SHOULD_NOT_HAVE_QUERY, actual, actual.getQuery());
  }

  public static ErrorMessageFactory shouldHaveQuery(URL actual, String expectedQuery) {
    return expectedQuery == null ? new ShouldHaveQuery(actual) : new ShouldHaveQuery(actual, expectedQuery);
  }

  private ShouldHaveQuery(URL actual, String expectedQuery) {
    super(SHOULD_HAVE_QUERY, actual, expectedQuery, actual.getQuery());
  }

  private ShouldHaveQuery(URL actual) {
    super(SHOULD_NOT_HAVE_QUERY, actual, actual.getQuery());
  }
}
