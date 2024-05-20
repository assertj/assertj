/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error.uri;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveQuery.shouldHaveQuery;

import java.net.URI;
import java.net.URL;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveQuery_create_Test {

  @Test
  void should_create_error_message_for_uri_has_query() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?type=beta");
    // WHEN
    String error = shouldHaveQuery(uri, "type=final").create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting query of%n" +
                                 "  <http://assertj.org/news?type=beta>%n" +
                                 "to be:%n" +
                                 "  <\"type=final\">%n" +
                                 "but was:%n" +
                                 "  <\"type=beta\">"));
  }

  @Test
  void should_create_error_message_for_url_has_query() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?type=beta");
    // WHEN
    String error = shouldHaveQuery(uri, "type=final").create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting query of%n" +
                                 "  <http://assertj.org/news?type=beta>%n" +
                                 "to be:%n" +
                                 "  <\"type=final\">%n" +
                                 "but was:%n" +
                                 "  <\"type=beta\">"));
  }

  @Test
  void should_create_error_message_for_uri_has_no_query() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?type=beta");
    // WHEN
    String error = shouldHaveQuery(uri, null).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  <http://assertj.org/news?type=beta>%n" +
                                 "not to have a query but had:%n" +
                                 "  <\"type=beta\">"));
  }

  @Test
  void should_create_error_message_for_url_has_no_query() throws Exception {
    // GIVEN
    URL url = new URL("http://assertj.org/news?type=beta");
    // WHEN
    String error = shouldHaveQuery(url, null).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  <http://assertj.org/news?type=beta>%n" +
                                 "not to have a query but had:%n" +
                                 "  <\"type=beta\">"));
  }
}
