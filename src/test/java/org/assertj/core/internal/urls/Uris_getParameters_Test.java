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
package org.assertj.core.internal.urls;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.Uris.getParameters;

public class Uris_getParameters_Test {

  @Test
  public void should_return_empty_for_empty_query() {
    assertThat(getParameters("")).isEmpty();
  }

  @Test
  public void should_return_empty_for_null_query() {
    assertThat(getParameters(null)).isEmpty();
  }

  @Test
  public void should_accept_parameter_with_no_value() {
    Map<String, List<String>> parameters = getParameters("foo");

    assertThat(parameters).containsKey("foo");
    assertThat(parameters.get("foo")).hasSize(1)
                                     .containsNull();
  }

  @Test
  public void should_accept_parameter_with_value() {
    Map<String, List<String>> parameters = getParameters("foo=bar");

    assertThat(parameters).containsKey("foo");
    assertThat(parameters.get("foo")).containsExactly("bar");
  }

  @Test
  public void should_decode_name() {
    assertThat(getParameters("foo%3Dbar=baz")).containsKey("foo=bar");
  }

  @Test
  public void should_decode_value() {
    assertThat(getParameters("foo=bar%3Dbaz").get("foo")).contains("bar=baz");
  }

  @Test
  public void should_accept_duplicate_names() {
    Map<String, List<String>> parameters = getParameters("foo&foo=bar");

    assertThat(parameters).containsKey("foo");
    assertThat(parameters.get("foo")).containsOnly(null, "bar");
  }

  @Test
  public void should_accept_duplicate_values() {
    Map<String, List<String>> parameters = getParameters("foo=bar&foo=bar");

    assertThat(parameters).containsKey("foo");
    assertThat(parameters.get("foo")).containsExactly("bar", "bar");
  }
}
