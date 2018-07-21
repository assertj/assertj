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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error.uri;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.uri.ShouldHavePort.shouldHavePort;

import java.net.URI;
import java.net.URL;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

public class ShouldHavePort_create_Test {

  @Test
  public void should_create_error_message_for_uri() throws Exception {
    String error = shouldHavePort(new URI("http://assertj.org:8080/news"), 8888).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting port of%n" +
                                       "  <http://assertj.org:8080/news>%n" +
                                       "to be:%n" +
                                       "  <8888>%n" +
                                       "but was:%n" +
                                       "  <8080>"));
  }

  @Test
  public void should_create_error_message_for_uri_has_no_port() throws Exception {
    URI uri = new URI("http://assertj.org:8080/news");
    String error = shouldHavePort(uri, -1).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org:8080/news>%n" +
                                       "not to have a port but had:%n" +
                                       "  <8080>"));
  }

  @Test
  public void should_create_error_message_for_url() throws Exception {
    String error = shouldHavePort(new URL("http://assertj.org:8080/news"), 8888).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting port of%n" +
                                       "  <http://assertj.org:8080/news>%n" +
                                       "to be:%n" +
                                       "  <8888>%n" +
                                       "but was:%n" +
                                       "  <8080>"));
  }

  @Test
  public void should_create_error_message_for_url_has_no_port() throws Exception {
    URL url = new URL("http://assertj.org:8080/news");
    String error = shouldHavePort(url, -1).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org:8080/news>%n" +
                                       "not to have a port but had:%n" +
                                       "  <8080>"));
  }
}
