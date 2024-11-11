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
package org.assertj.core.error.uri;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveUserInfo.shouldHaveUserInfo;

import java.net.URI;
import java.net.URL;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveUserInfo_create_Test {

  @Test
  void should_create_error_message_for_uri_has_user_info() {
    // GIVEN
    URI uri = URI.create("http://test:pass@assertj.org/news");
    // WHEN
    String error = shouldHaveUserInfo(uri, "test:success").create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting user info of%n" +
                                 "  <http://test:pass@assertj.org/news>%n" +
                                 "to be:%n" +
                                 "  <\"test:success\">%n" +
                                 "but was:%n" +
                                 "  <\"test:pass\">"));
  }

  @Test
  void should_create_error_message_for_uri_has_no_user_info() {
    // GIVEN
    URI uri = URI.create("http://test:pass@assertj.org/news");
    // WHEN
    String error = shouldHaveUserInfo(uri, null).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  <http://test:pass@assertj.org/news>%n" +
                                 "not to have user info but had:%n" +
                                 "  <\"test:pass\">"));
  }

  @Test
  void should_create_error_message_for_url_has_user_info() throws Exception {
    // GIVEN
    URL url = new URL("http://test:pass@assertj.org/news");
    // WHEN
    String error = shouldHaveUserInfo(url, "test:success").create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting user info of%n" +
                                 "  <http://test:pass@assertj.org/news>%n" +
                                 "to be:%n" +
                                 "  <\"test:success\">%n" +
                                 "but was:%n" +
                                 "  <\"test:pass\">"));
  }

  @Test
  void should_create_error_message_for_url_has_no_user_info() throws Exception {
    // GIVEN
    URL url = new URL("http://test:pass@assertj.org/news");
    // WHEN
    String error = shouldHaveUserInfo(url, null).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  <http://test:pass@assertj.org/news>%n" +
                                 "not to have user info but had:%n" +
                                 "  <\"test:pass\">"));
  }

}
