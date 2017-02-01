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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.util.Lists.newArrayList;

import java.net.URI;
import java.net.URL;

import org.assertj.core.internal.TestDescription;
import org.junit.Test;

public class ShouldHaveParameter_create_Test {

  @Test
  public void should_create_error_message_for_missing_uri_parameter() throws Exception {
    URI uri = new URI("http://assertj.org/news");
    String error = shouldHaveParameter(uri, "article").create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "but was missing"));
  }

  @Test
  public void should_create_error_message_for_uri_parameter_without_value_that_is_missing() throws Exception {
    URI uri = new URI("http://assertj.org/news");
    String error = shouldHaveParameter(uri, "article", null).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with no value, but parameter was missing"));
  }

  @Test
  public void should_create_error_message_for_missing_uri_parameter_with_an_expected_value() throws Exception {
    URI uri = new URI("http://assertj.org/news");
    String error = shouldHaveParameter(uri, "article", "10").create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but parameter was missing"));
  }

  @Test
  public void should_create_error_message_for_uri_parameter_without_value_that_has_one() throws Exception {
    URI uri = new URI("http://assertj.org/news?article=10");
    String error = shouldHaveParameter(uri, "article", null, newArrayList("10")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with no value, but parameter had value:%n" +
                                       "  <\"10\">"));
  }

  @Test
  public void should_create_error_message_for_uri_parameter_without_value_that_has_multiple_values() throws Exception {
    URI uri = new URI("http://assertj.org/news?article=10");
    String error = shouldHaveParameter(uri, "article", null,
                                       newArrayList("10", "11")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with no value, but parameter had values:%n" +
                                       "  <\"[10, 11]\">"));
  }

  @Test
  public void should_create_error_message_for_uri_parameter_with_value_that_has_no_value() throws Exception {
    URI uri = new URI("http://assertj.org/news?article");
    String error = shouldHaveParameter(uri, "article", "10",
                                       newArrayList((String) null)).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but parameter had no value"));
  }

  @Test
  public void should_create_error_message_for_uri_with_wrong_parameter_value() throws Exception {
    URI uri = new URI("http://assertj.org/news?article=11");
    String error = shouldHaveParameter(uri, "article", "10", newArrayList("11")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=11>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but had value:%n" +
                                       "  <\"11\">"));
  }

  @Test
  public void should_create_error_message_for_uri_with_wrong_parameter_values() throws Exception {
    URI uri = new URI("http://assertj.org/news?article=11");
    String error = shouldHaveParameter(uri, "article", "10",
                                       newArrayList("11", "12")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=11>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but had values:%n" +
                                       "  <\"[11, 12]\">"));
  }

  @Test
  public void should_create_error_message_for_uri_with_no_parameter_that_has_one_even_without_value() throws Exception {
    URI uri = new URI("http://assertj.org/news?article");
    String error = shouldHaveNoParameter(uri, "article", null).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "but parameter was present with no value"));
  }

  @Test
  public void should_create_error_message_for_uri_with_no_parameter_that_has_one_with_value() throws Exception {
    URI uri = new URI("http://assertj.org/news?article=10");
    String error = shouldHaveNoParameter(uri, "article", newArrayList("10")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "but parameter was present with value:%n" +
                                       "  <\"10\">"));
  }

  @Test
  public void should_create_error_message_for_uri_with_no_parameter_that_has_one_with_multiple_values() throws Exception {
    URI uri = new URI("http://assertj.org/news?article=10");
    String error = shouldHaveNoParameter(uri, "article", newArrayList("10", "11")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "but parameter was present with values:%n" +
                                       "  <\"[10, 11]\">"));
  }

  @Test
  public void should_create_error_message_for_uri_with_no_parameter_that_has_one_without_value() throws Exception {
    URI uri = new URI("http://assertj.org/news?article");
    String error = shouldHaveNoParameter(uri, "article", null, null).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with no value, but did"));
  }

  @Test
  public void should_create_error_message_for_uri_no_parameter_value_but_found() throws Exception {
    URI uri = new URI("http://assertj.org/news?article=10");
    String error = shouldHaveNoParameter(uri, "article", "10", newArrayList("10")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but did"));
  }

  // URL

  @Test
  public void should_create_error_message_for_missing_url_parameter() throws Exception {
    URL url = new URL("http://assertj.org/news");
    String error = shouldHaveParameter(url, "article").create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "but was missing"));
  }

  @Test
  public void should_create_error_message_for_url_parameter_without_value_that_is_missing() throws Exception {
    URL url = new URL("http://assertj.org/news");
    String error = shouldHaveParameter(url, "article", null).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with no value, but parameter was missing"));
  }

  @Test
  public void should_create_error_message_for_missing_url_parameter_with_an_expected_value() throws Exception {
    URL url = new URL("http://assertj.org/news");
    String error = shouldHaveParameter(url, "article", "10").create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but parameter was missing"));
  }

  @Test
  public void should_create_error_message_for_url_parameter_without_value_that_has_one() throws Exception {
    URL url = new URL("http://assertj.org/news?article=10");
    String error = shouldHaveParameter(url, "article", null, newArrayList("10")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with no value, but parameter had value:%n" +
                                       "  <\"10\">"));
  }

  @Test
  public void should_create_error_message_for_url_parameter_without_value_that_has_multiple_values() throws Exception {
    URL url = new URL("http://assertj.org/news?article=10");
    String error = shouldHaveParameter(url, "article", null,
                                       newArrayList("10", "11")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with no value, but parameter had values:%n" +
                                       "  <\"[10, 11]\">"));
  }

  @Test
  public void should_create_error_message_for_url_parameter_with_value_that_has_no_value() throws Exception {
    URL url = new URL("http://assertj.org/news?article");
    String error = shouldHaveParameter(url, "article", "10",
                                       newArrayList((String) null)).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but parameter had no value"));
  }

  @Test
  public void should_create_error_message_for_url_with_wrong_parameter_value() throws Exception {
    URL url = new URL("http://assertj.org/news?article=11");
    String error = shouldHaveParameter(url, "article", "10", newArrayList("11")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=11>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but had value:%n" +
                                       "  <\"11\">"));
  }

  @Test
  public void should_create_error_message_for_url_with_wrong_parameter_values() throws Exception {
    URL url = new URL("http://assertj.org/news?article=11");
    String error = shouldHaveParameter(url, "article", "10",
                                       newArrayList("11", "12")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=11>%n" +
                                       "to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but had values:%n" +
                                       "  <\"[11, 12]\">"));
  }

  @Test
  public void should_create_error_message_for_url_with_no_parameter_that_has_one_even_without_value() throws Exception {
    URL url = new URL("http://assertj.org/news?article");
    String error = shouldHaveNoParameter(url, "article", newArrayList((String)null)).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "but parameter was present with no value"));
  }

  @Test
  public void should_create_error_message_for_url_with_no_parameter_that_has_one_with_value() throws Exception {
    URL url = new URL("http://assertj.org/news?article=10");
    String error = shouldHaveNoParameter(url, "article", newArrayList("10")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "but parameter was present with value:%n" +
                                       "  <\"10\">"));
  }

  @Test
  public void should_create_error_message_for_url_with_no_parameter_that_has_one_with_multiple_values() throws Exception {
    URL url = new URL("http://assertj.org/news?article=10");
    String error = shouldHaveNoParameter(url, "article", newArrayList("10", "11")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "but parameter was present with values:%n" +
                                       "  <\"[10, 11]\">"));
  }

  @Test
  public void should_create_error_message_for_url_with_no_parameter_that_has_one_without_value() throws Exception {
    URL url = new URL("http://assertj.org/news?article");
    String error = shouldHaveNoParameter(url, "article", null, null).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with no value, but did"));
  }

  @Test
  public void should_create_error_message_for_url_no_parameter_value_but_found() throws Exception {
    URL url = new URL("http://assertj.org/news?article=10");
    String error = shouldHaveNoParameter(url, "article", "10", newArrayList("10")).create(new TestDescription("TEST"));

    assertThat(error).isEqualTo(format("[TEST] %n" +
                                       "Expecting:%n" +
                                       "  <http://assertj.org/news?article=10>%n" +
                                       "not to have parameter:%n" +
                                       "  <\"article\">%n" +
                                       "with value:%n" +
                                       "  <\"10\">%n" +
                                       "but did"));
  }


}
