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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.error.uri;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHavePath.shouldHavePath;

import java.net.URI;
import java.net.URL;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHavePath_create_Test {

  @Test
  void should_create_error_message_for_uri() {
    // WHEN
    String error = shouldHavePath(URI.create("http://assertj.org/news"), "/foo").create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting path of%n" +
                                 "  <http://assertj.org/news>%n" +
                                 "to be:%n" +
                                 "  <\"/foo\">%n" +
                                 "but was:%n" +
                                 "  <\"/news\">"));
  }

  @Test
  void should_create_error_message_for_url() throws Exception {
    // WHEN
    String error = shouldHavePath(new URL("http://assertj.org/news"), "/foo").create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting path of%n" +
                                 "  <http://assertj.org/news>%n" +
                                 "to be:%n" +
                                 "  <\"/foo\">%n" +
                                 "but was:%n" +
                                 "  <\"/news\">"));
  }

  @Test
  void should_create_error_message_for_uri_has_no_path() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?type=beta");
    // WHEN
    String error = shouldHavePath(uri, null).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  <http://assertj.org/news?type=beta>%n" +
                                 "not to have a path but had:%n" +
                                 "  <\"/news\">"));
  }

  @Test
  void should_create_error_message_for_url_has_no_path() throws Exception {
    // GIVEN
    URL url = new URL("http://assertj.org/news?type=beta");
    // WHEN
    String error = shouldHavePath(url, null).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  <http://assertj.org/news?type=beta>%n" +
                                 "not to have a path but had:%n" +
                                 "  <\"/news\">"));

    error = shouldHavePath(url, "").create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  <http://assertj.org/news?type=beta>%n" +
                                 "not to have a path but had:%n" +
                                 "  <\"/news\">"));
  }
}
